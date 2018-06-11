package com.skcc.cloudz.zcp.alert.dao.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.skcc.cloudz.zcp.common.yamlbeans.YamlConfig;
import com.skcc.cloudz.zcp.common.yamlbeans.YamlException;
import com.skcc.cloudz.zcp.common.yamlbeans.YamlReader;
import com.skcc.cloudz.zcp.common.yamlbeans.YamlWriter;
import com.skcc.cloudz.zcp.alert.dao.RuleDao;
import com.skcc.cloudz.zcp.alert.service.impl.RuleServiceImpl;
import com.skcc.cloudz.zcp.alert.vo.RuleData;
import com.skcc.cloudz.zcp.common.util.JsonUtil;
import com.skcc.cloudz.zcp.common.util.Message;


import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1ConfigMap;
import io.kubernetes.client.util.Config;
import net.minidev.json.JSONObject;



@Repository("ruleDao")
public class RuleDaoImpl implements RuleDao {
	private static Logger logger = Logger.getLogger(RuleDao.class);
	
	@Autowired
    Message message;
	
	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.dao.RuleDao#getRuleListDao()
	 */
	@Override
	public List<RuleData> getRuleListDao()  {
		// TODO Auto-generated method stub
		
		FileWriter writer = null;
		List listRules = null;
		
		try {
			ApiClient client = Config.defaultClient();
			Configuration.setDefaultApiClient(client);
	
			CoreV1Api api = new CoreV1Api();
			V1ConfigMap configMap;
			
			configMap = api.readNamespacedConfigMap("prometheus-user-rules", "monitoring", null, null, null);
			
			File file = new File("rule.yaml");
	        
	        writer = new FileWriter(file, false);
	        writer.write(configMap.getData().get("users-rules.rules"));
	        writer.flush();
	        
	        YamlReader reader = new YamlReader(new FileReader("rule.yaml"));
            Object object = reader.read();
            
			Map<String, Map<String, Object>> mapGroups = (Map)object;
			
			List listGroups = (List)mapGroups.get("groups");
			
			Map<String, Object> maplistGroups;
			Iterator iteratorData = listGroups.iterator();
			
			RuleData ruleData = new RuleData();
			maplistGroups = (Map) iteratorData.next();
		    
		    Map<String, Object> maplistRules;
			listRules = (List)maplistGroups.get("rules");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            try {
                if(writer != null) writer.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
		return listRules;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.dao.RuleDao#createRule(com.skcc.cloudz.zcp.alert.vo.Rule)
	 */
	@SuppressWarnings({ "null", "unchecked", "static-access" })
	@Override
	public RuleData createRule(RuleData createRuleVo) {
		// TODO Auto-generated method stub
		
		List<RuleData> ruleList = new ArrayList<RuleData>();
		FileWriter writer = null;
		
		try {
			ApiClient client = Config.defaultClient();
			Configuration.setDefaultApiClient(client);
	
			CoreV1Api api = new CoreV1Api();
			V1ConfigMap configMap = new V1ConfigMap();
			
			configMap = api.readNamespacedConfigMap("prometheus-user-rules", "monitoring", null, null, null);
			String rules = configMap.getData().get("users-rules.rules");
			
			File file = new File("rule.yaml");
	        
	        writer = new FileWriter(file, false);
	        writer.write(configMap.getData().get("users-rules.rules"));
	        writer.flush();
	        
	        HashMap<String, String> labels = new HashMap<String, String>();
			labels.put("severity", createRuleVo.getRuleSeverity());
			labels.put("channel", createRuleVo.getRuleChannel());
			
			HashMap<String, String> annotations = new HashMap<String, String>();
			createRuleVo.setRuleDescription(message.get("NodeCPUUsage"));
			annotations.put("description", createRuleVo.getRuleDescription());
			
			HashMap<String, Object> newRules = new LinkedHashMap<String, Object>();
			
			newRules.put("alert", createRuleVo.getRuleAlert());
			newRules.put("expr", createRuleVo.getRuleExpr());
			newRules.put("for", createRuleVo.getRuleFor());
			newRules.put("labels", labels);
			newRules.put("annotations", annotations);
	        
	        YamlReader reader = new YamlReader(new FileReader("rule.yaml"));
            Object object = reader.read();
			
			Map<String, Map<String, Object>> mapGroups = (Map)object;
			List listGroups = (List)mapGroups.get("groups");
			
			Iterator iteratorData = listGroups.iterator();
			Map<String, Object> maplistGroups = null;
			
			while (iteratorData.hasNext()) {
				maplistGroups = (Map) iteratorData.next();
			}
			
			Map<String, Object> maplistRules;
			List listRules = (List)maplistGroups.get("rules");
			listRules.add(newRules);
			
			Map<String, Object> groups = new HashMap<String, Object>();

			groups.put("name", "users-rules.rules");
			groups.put("rules", listRules);
			
			List groupList = new ArrayList();
			groupList.add(groups);
			
			Map<String, Object> groupMap = new HashMap<String, Object>();
			groupMap.put("groups", groupList);
			
			YamlConfig config = new YamlConfig();
			YamlWriter ywriter = new YamlWriter(new FileWriter("rule.yaml"), config);
			ywriter.write(groupMap);
			ywriter.close();
			
			String yamlString = FileUtils.readFileToString(new File("rule.yaml"), "utf8");
    		
    		Map<String, String> data = new HashMap<String, String>();
    		data.put("users-rules.rules", yamlString);
    		
    		configMap.setData(data);
			V1ConfigMap replacedConfigmap = api.replaceNamespacedConfigMap("prometheus-user-rules", "monitoring", configMap, null);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return createRuleVo;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.dao.RuleDao#deleteRule(java.lang.Long)
	 */
	@Override
	public Boolean deleteRule(int ruleId) {
		// TODO Auto-generated method stub
		List<RuleData> ruleList = new ArrayList<RuleData>();
		FileWriter writer = null;
		
		try {
			ApiClient client = Config.defaultClient();
			Configuration.setDefaultApiClient(client);
	
			CoreV1Api api = new CoreV1Api();
			V1ConfigMap configMap;
			
			configMap = api.readNamespacedConfigMap("prometheus-user-rules", "monitoring", null, null, null);
			
			File file = new File("rule.yaml");
	        
	        writer = new FileWriter(file, false);
	        writer.write(configMap.getData().get("users-rules.rules"));
	        writer.flush();
	        
	        YamlReader reader = new YamlReader(new FileReader("rule.yaml"));
            Object object = reader.read();

			Map<String, Map<String, Object>> mapGroups = (Map)object;
			List listGroups = (List)mapGroups.get("groups");
			
			Iterator iteratorData = listGroups.iterator();
			Map<String, Object> maplistGroups;
			
			RuleData ruleData = new RuleData();
			List listRules = new ArrayList();
			
			while (iteratorData.hasNext()) {
				maplistGroups = (Map) iteratorData.next();
				listRules = (List)maplistGroups.get("rules");
				for(int cnt=0; cnt<listRules.size(); cnt++) {
					if(cnt == ruleId) {
						listRules.remove(cnt);
					} 
				}
			}
			
			Map<String, Object> groups = new HashMap<String, Object>();

			groups.put("name", "users-rules.rules");
			groups.put("rules", listRules);
			
			List groupList = new ArrayList();
			groupList.add(groups);
			
			Map<String, Object> groupMap = new HashMap<String, Object>();
			groupMap.put("groups", groupList);
			
			YamlConfig config = new YamlConfig();
			YamlWriter ywriter = new YamlWriter(new FileWriter("rule.yaml"), config);
			ywriter.write(groupMap);
			ywriter.close();
			
			String yamlString = FileUtils.readFileToString(new File("rule.yaml"), "utf8");
    		
    		Map<String, String> data = new HashMap<String, String>();
    		data.put("users-rules.rules", yamlString);
    		
    		configMap.setData(data);
			V1ConfigMap replacedConfigmap = api.replaceNamespacedConfigMap("prometheus-user-rules", "monitoring", configMap, null);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            try {
                if(writer != null) writer.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
		return true;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.dao.RuleDao#updateRule(int, com.skcc.cloudz.zcp.alert.vo.RuleData)
	 */
//	@Override
//	public RuleData updateRule(int id, RuleData rule) {
//		// TODO Auto-generated method stub
//		
//		FileWriter writer = null;
//		
//		try {
//			ApiClient client = Config.defaultClient();
//			Configuration.setDefaultApiClient(client);
//	
//			CoreV1Api api = new CoreV1Api();
//			V1ConfigMap configMap;
//			
////			configMap = api.readNamespacedConfigMap("prometheus-user-rules", "monitoring", null, null, null);
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//            try {
//                if(writer != null) writer.close();
//            } catch(IOException e) {
//                e.printStackTrace();
//            }
//        }
//		return null;
//	}

	
	
}
