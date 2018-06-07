package com.skcc.cloudz.zcp.alert.dao.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import com.skcc.cloudz.zcp.alert.dao.RuleDao;
import com.skcc.cloudz.zcp.alert.service.impl.RuleServiceImpl;
import com.skcc.cloudz.zcp.alert.vo.RuleDto;
import com.skcc.cloudz.zcp.common.util.Message;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1ConfigMap;
import io.kubernetes.client.util.Config;

@Repository("ruleDao")
public class RuleDaoImpl implements RuleDao {
	private static Logger logger = Logger.getLogger(RuleDao.class);
	
	@Autowired
    Message message;
	
	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.dao.RuleDao#getRuleListDao()
	 */
	@Override
	public List<RuleDto> getRuleListDao()  {
		// TODO Auto-generated method stub
		
		List<RuleDto> ruleList = new ArrayList<RuleDto>();
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

            /*groups*/
			Map<String, Map<String, Object>> mapGroups = (Map)object;
			List listGroups = (List)mapGroups.get("groups");
			
			Iterator iteratorData = listGroups.iterator();
			Map<String, Object> maplistGroups;
			
			RuleDto ruleDto = new RuleDto();
			
			while (iteratorData.hasNext()) {
				maplistGroups = (Map) iteratorData.next();
			    
			    /*rules*/
			    Map<String, Object> maplistRules;
				List listRules = (List)maplistGroups.get("rules");
				
				Iterator iteratorRules = listRules.iterator();
				
				while (iteratorRules.hasNext()) {
				    maplistRules = (Map) iteratorRules.next();
				    
				    /*labels*/
				    Map<String, Object> maplistLabels;
				    maplistLabels = (Map<String, Object>) maplistRules.get("labels");
				    
				    /*annotations*/
				    Map<String, Object> maplistAnnotations;
				    maplistAnnotations = (Map<String, Object>) maplistRules.get("annotations");
				    
				    ruleDto.setRuleDataAlert(maplistRules.get("alert").toString());
				    ruleDto.setRuleDataExpr(maplistRules.get("expr").toString());
				    ruleDto.setRuleDataFor(maplistRules.get("for").toString());
				    ruleDto.setRuleDataSeverity(maplistLabels.get("severity").toString());
				    ruleDto.setRuleDataChannel(maplistLabels.get("channel").toString());
				    
					ruleList.add(ruleDto);
					
				}
			}
			
//			V1ConfigMap replacedConfigmap = api.replaceNamespacedConfigMap("prometheus-user-rules", "monitoring", configMap, null);
			
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
		
		
		
		
		return ruleList;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.dao.RuleDao#createRule(com.skcc.cloudz.zcp.alert.vo.Rule)
	 */
	@SuppressWarnings({ "null", "unchecked" })
	@Override
	public RuleDto createRule(RuleDto createRuleVo) {
		// TODO Auto-generated method stub
		
		List<RuleDto> ruleList = new ArrayList<RuleDto>();
		FileWriter writer = null;
		
		try {
			ApiClient client = Config.defaultClient();
			Configuration.setDefaultApiClient(client);
	
			CoreV1Api api = new CoreV1Api();
			V1ConfigMap configMap = new V1ConfigMap();
			
			configMap = api.readNamespacedConfigMap("prometheus-user-rules", "monitoring", null, null, null);
			System.out.println(configMap);
			
			File file = new File("rule.yaml");
	        
	        writer = new FileWriter(file, false);
	        writer.write(configMap.getData().get("users-rules.rules"));
	        writer.flush();
	        
	        YamlReader reader = new YamlReader(new FileReader("rule.yaml"));
            Object object = reader.read();
			
			/*groups*/
			Map<String, Map<String, Object>> mapGroups = (Map)object;
			List listGroups = (List)mapGroups.get("groups");
			
			Iterator iteratorData = listGroups.iterator();
			Map<String, Object> maplistGroups;
			
			RuleDto ruleDto = new RuleDto();
			
			while (iteratorData.hasNext()) {
				maplistGroups = (Map) iteratorData.next();
			    
			    /*rules*/
			    Map<String, Object> maplistRules;
				List listRules = (List)maplistGroups.get("rules");
				
				Iterator iteratorRules = listRules.iterator();
				
				while (iteratorRules.hasNext()) {
				    maplistRules = (Map) iteratorRules.next();
				    
				    /*labels*/
				    Map<String, Object> maplistLabels;
				    maplistLabels = (Map<String, Object>) maplistRules.get("labels");
				    
				    /*annotations*/
				    Map<String, Object> maplistAnnotations;
				    maplistAnnotations = (Map<String, Object>) maplistRules.get("annotations");
				    
				    ruleDto.setRuleDataAlert(maplistRules.get("alert").toString());
				    ruleDto.setRuleDataExpr(maplistRules.get("expr").toString());
				    ruleDto.setRuleDataFor(maplistRules.get("for").toString());
				    ruleDto.setRuleDataSeverity(maplistLabels.get("severity").toString());
				    ruleDto.setRuleDataChannel(maplistLabels.get("channel").toString());
				    
					ruleList.add(ruleDto);
				}
				ruleDto.setRuleDataAlert(createRuleVo.getRuleDataAlert());
				ruleDto.setRuleDataExpr(createRuleVo.getRuleDataExpr());
				ruleDto.setRuleDataFor(createRuleVo.getRuleDataFor());
				ruleDto.setRuleDataSeverity(createRuleVo.getRuleDataSeverity());
				ruleDto.setRuleDataChannel(createRuleVo.getRuleDataChannel());
				
				ruleList.add(ruleDto);
				
			}
//			HashMap<String, Object> rule = new LinkedHashMap<String, Object>();
//			HashMap<String, Object> groups = new HashMap<String, Object>();
//			List<RuleDto> valueList = new ArrayList<RuleDto>();
//			
//			groups.put("name", "users-rules.rules");
//			
//			Iterator<RuleDto> it = ruleList.iterator();
//			while(it.hasNext()) {
//				RuleDto value = it.next();
//				
//				rule.put("alert", value.getRuleDataAlert());
//				rule.put("expr", value.getRuleDataExpr());
//				rule.put("for", value.getRuleDataFor());
//				
//				HashMap<String, String> labels = new HashMap<String, String>();
//				labels.put("severity", value.getRuleDataSeverity());
//				labels.put("channel", value.getRuleDataChannel());
//				
//				rule.put("label", labels);
//				
//				HashMap<String, String> annotations = new HashMap<String, String>();
//				annotations.put("description", value.getRuleDataDescription());
//				
//				rule.put("annotations", annotations);
//				
//				valueList.add(rule);
//				groups.put("rules", valueList);
//				
//				DumperOptions options = new DumperOptions();
//				options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
//				options.setPrettyFlow(true);
//				
//				Yaml yaml = new Yaml(options);
//				FileWriter fileWriter = null;
//				
//				fileWriter = new FileWriter(new File("createRule.yaml"));
//			    yaml.dump(map, fileWriter);
//			    fileWriter.close();
//			}
//			
//			HashMap<String, Object> map = new HashMap<String, Object>();
//			map.put("groups", groups);
//			
//			DumperOptions options = new DumperOptions();
//			options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
//			options.setPrettyFlow(true);
//			
//			Yaml yaml = new Yaml(options);
//			FileWriter fileWriter = null;
//			
//			fileWriter = new FileWriter(new File("createRule.yaml"));
//		    yaml.dump(map, fileWriter);
//		    fileWriter.close();
			
//			while (iteratorRule.hasNext()) {
//				rulelistGroups = (Map) iteratorRule.next();
//				
//				Map<String, Object> rulelistLabels;
//				rulelistLabels = (Map<String, Object>) rulelistGroups.get("labels");
//			    
//				System.out.println(rulelistGroups.get("labels"));
//			}
			
			HashMap<String, String> labels = new HashMap<String, String>();
			labels.put("severity", createRuleVo.getRuleDataSeverity());
			labels.put("channel", createRuleVo.getRuleDataChannel());
			
			HashMap<String, String> annotations = new HashMap<String, String>();
			createRuleVo.setRuleDataDescription(message.get("NodeCPUUsage"));
			
			annotations.put("description", createRuleVo.getRuleDataDescription());
			
			HashMap<String, Object> rules = new LinkedHashMap<String, Object>();
			
			rules.put("alert", createRuleVo.getRuleDataAlert());
			rules.put("expr", createRuleVo.getRuleDataExpr());
			rules.put("for", createRuleVo.getRuleDataFor());
			rules.put("labels", labels);
			rules.put("annotations", annotations);
			
			HashMap<String, Object> groups = new HashMap<String, Object>();
			groups.put("name", "users-rules.rules");
			groups.put("rules", rules);
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("groups", groups);
			
			/*
			YamlConfig config = new YamlConfig();
	        config.writeConfig.setWriteRootTags(false);
	        config.writeConfig.setWriteRootElementTags(false);
	        
			YamlWriter writer = new YamlWriter(new FileWriter("createRule.yaml"), config);
			writer.write(map);
			writer.close(); */
			
			DumperOptions options = new DumperOptions();
			options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
			options.setPrettyFlow(true);
			
			Yaml yaml = new Yaml(options);
			FileWriter fileWriter = null;
			
			fileWriter = new FileWriter(new File("createRule.yaml"));
		    yaml.dump(map, fileWriter);
		    fileWriter.close();
			
    		String yamlString = FileUtils.readFileToString(new File("createRule.yaml"), "utf8");
    		System.out.println(yamlString);
			
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("users-rules.rules", yamlString);
			
			configMap.setData(data);
			System.out.println(configMap);
			
//			V1ConfigMap replacedConfigmap = api.replaceNamespacedConfigMap("prometheus-user-rules", "monitoring", configMap, null);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return createRuleVo;
	}
	
}
