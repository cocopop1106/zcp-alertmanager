package com.skcc.cloudz.zcp.alert.dao.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
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
		
		List<RuleData> ruleList = new ArrayList<RuleData>();
		FileWriter writer = null;
		
		try {
			ApiClient client = Config.defaultClient();
			Configuration.setDefaultApiClient(client);
	
			CoreV1Api api = new CoreV1Api();
			V1ConfigMap configMap;
			
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
			
			RuleData ruleData = new RuleData();
			
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
				    
				    ruleData.setRuleAlert(maplistRules.get("alert").toString());
				    ruleData.setRuleExpr(maplistRules.get("expr").toString());
				    ruleData.setRuleFor(maplistRules.get("for").toString());
				    ruleData.setRuleSeverity(maplistLabels.get("severity").toString());
				    ruleData.setRuleChannel(maplistLabels.get("channel").toString());
				    
					ruleList.add(ruleData);
					
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
			System.out.println(configMap);
			
			String rules = configMap.getData().get("users-rules.rules");
			System.out.println(rules);
			
			// 기존 룰 맵으로 변환 
			File file = new File("readRule.yaml");
	        
	        writer = new FileWriter(file, false);
	        writer.write(configMap.getData().get("users-rules.rules"));
	        writer.flush();
	        
	        YamlReader reader = new YamlReader(new FileReader("readRule.yaml"));
            Object object = reader.read();
            
			
			Map<String, Map<String, Object>> mapGroups = (Map)object;
			List listGroups = (List)mapGroups.get("groups");
			
			Iterator iteratorData = listGroups.iterator();
			Map<String, Object> maplistGroups;
			
			// 새로운 룰 생성 
			HashMap<String, String> labels = new HashMap<String, String>();
			labels.put("severity", createRuleVo.getRuleSeverity());
			labels.put("channel", createRuleVo.getRuleChannel());
			
			HashMap<String, String> annotations = new HashMap<String, String>();
			createRuleVo.setRuleDescription(message.get("NodeCPUUsage"));
			annotations.put("description", createRuleVo.getRuleDescription());
			
			HashMap<String, Object> newRules = new HashMap<String, Object>();
			
			newRules.put("alert", createRuleVo.getRuleAlert());
			newRules.put("expr", createRuleVo.getRuleExpr());
			newRules.put("for", createRuleVo.getRuleFor());
			newRules.put("labels", labels);
			newRules.put("annotations", annotations);
			
			HashMap<String, Object> groups = new HashMap<String, Object>();
			groups.put("name", "users-rules.rules");
			groups.put("rules", newRules);
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("groups", groups);
			
			YamlConfig config = new YamlConfig();
	        config.writeConfig.setWriteRootTags(false);
	        config.writeConfig.setWriteRootElementTags(false);
	        
			YamlWriter ywriter = new YamlWriter(new FileWriter("createRule.yaml"), config);
			ywriter.write(newRules);
			ywriter.close();
			
			YamlReader yReader = new YamlReader(new FileReader("createRule.yaml"));
            Object newRule = yReader.read();
            System.out.println(newRule);
			
			while (iteratorData.hasNext()) {
				maplistGroups = (Map) iteratorData.next();
				System.out.println(maplistGroups);
				maplistGroups.put("rules", newRule);
				System.out.println("#### "+maplistGroups);
				
			}
			
			
    		
//			File file = new File("readRule.yaml");
//	        
//	        writer = new FileWriter(file, false);
//	        writer.write(configMap.getData().get("users-rules.rules"));
//	        writer.flush();
//	        
//	        YamlReader reader = new YamlReader(new FileReader("readRule.yaml"));
//            Object object = reader.read();
//            System.out.println(object);
//            
//            Map<String, Map<String, Object>> mapGroups = (Map)object;
//			List listGroups = (List)mapGroups.get("groups");
			
            
//            JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonObject.toString());
//            String jsonAsYaml = new YAMLMapper().writeValueAsString(jsonNodeTree);
//            System.out.println(jsonAsYaml);
			
//			File file = new File("rule.yaml");
//	        
//	        writer = new FileWriter(file, false);
//	        writer.write(configMap.getData().get("users-rules.rules"));
//	        writer.flush();
//	        
//	        YamlReader reader = new YamlReader(new FileReader("rule.yaml"));
//            Object object = reader.read();
//			
//			Map<String, Map<String, Object>> mapGroups = (Map)object;
//			List listGroups = (List)mapGroups.get("groups");
//			
//			Iterator iteratorData = listGroups.iterator();
//			Map<String, Object> maplistGroups;
//			
//			RuleData ruleData = new RuleData();
//			
//			while (iteratorData.hasNext()) {
//				maplistGroups = (Map) iteratorData.next();
//			    
//			    /*rules*/
//			    Map<String, Object> maplistRules;
//				List listRules = (List)maplistGroups.get("rules");
//				
//				Iterator iteratorRules = listRules.iterator();
//				
//				while (iteratorRules.hasNext()) {
//				    maplistRules = (Map) iteratorRules.next();
//				    
//				    /*labels*/
//				    Map<String, Object> maplistLabels;
//				    maplistLabels = (Map<String, Object>) maplistRules.get("labels");
//				    
//				    /*annotations*/
//				    Map<String, Object> maplistAnnotations;
//				    maplistAnnotations = (Map<String, Object>) maplistRules.get("annotations");
//				    
//				    ruleData.setRuleAlert(maplistRules.get("alert").toString());
//				    ruleData.setRuleExpr(maplistRules.get("expr").toString());
//				    ruleData.setRuleFor(maplistRules.get("for").toString());
//				    ruleData.setRuleSeverity(maplistLabels.get("severity").toString());
//				    ruleData.setRuleChannel(maplistLabels.get("channel").toString());
//				    
//					ruleList.add(ruleData);
//				}
//				ruleData.setRuleAlert(createRuleVo.getRuleAlert());
//				ruleData.setRuleExpr(createRuleVo.getRuleExpr());
//				ruleData.setRuleFor(createRuleVo.getRuleFor());
//				ruleData.setRuleSeverity(createRuleVo.getRuleSeverity());
//				ruleData.setRuleChannel(createRuleVo.getRuleChannel());
//				
//				ruleList.add(ruleData);
//				
//			}
			
			
			
			
			
			
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
			
//			HashMap<String, String> labels = new HashMap<String, String>();
//			labels.put("severity", createRuleVo.getRuleSeverity());
//			labels.put("channel", createRuleVo.getRuleChannel());
//			
//			HashMap<String, String> annotations = new HashMap<String, String>();
//			createRuleVo.setRuleDescription(message.get("NodeCPUUsage"));
//			annotations.put("description", createRuleVo.getRuleDescription());
//			
//			HashMap<String, Object> rules = new HashMap<String, Object>();
//			
//			rules.put("alert", createRuleVo.getRuleAlert());
//			rules.put("expr", createRuleVo.getRuleExpr());
//			rules.put("for", createRuleVo.getRuleFor());
//			rules.put("labels", labels);
//			rules.put("annotations", annotations);
			
//			HashMap<String, Object> groups = new HashMap<String, Object>();
//			groups.put("name", "users-rules.rules");
//			groups.put("rules", rules);
			
//			HashMap<String, String> labels2 = new HashMap<String, String>();
//			labels2.put("severity", createRuleVo.getRuleSeverity());
//			labels2.put("channel", createRuleVo.getRuleChannel());
//			
//			HashMap<String, String> annotations2 = new HashMap<String, String>();
//			createRuleVo.setRuleDescription(message.get("NodeCPUUsage"));
//			annotations2.put("description", createRuleVo.getRuleDescription());
//			
//			HashMap<String, Object> rules2 = new HashMap<String, Object>();
//			
//			rules2.put("alert", createRuleVo.getRuleAlert());
//			rules2.put("expr", createRuleVo.getRuleExpr());
//			rules2.put("for", createRuleVo.getRuleFor());
//			rules2.put("labels", labels2);
//			rules2.put("annotations", annotations2);
			
//			HashMap<String, Object> map = new HashMap<String, Object>();
//			map.put("groups", groups);
			
//			YamlConfig config = new YamlConfig();
//	        config.writeConfig.setWriteRootTags(false);
//	        config.writeConfig.setWriteRootElementTags(false);
//	        
//			YamlWriter ywriter = new YamlWriter(new FileWriter("createRule.yaml"), config);
//			ywriter.write(rules);
//			ywriter.write(rules2);
//			ywriter.close();
			
//			System.out.println(configMap.getApiVersion());
//			System.out.println(configMap.getKind());
//			System.out.println(configMap.getMetadata());
			
			/*DumperOptions options = new DumperOptions();
			options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
			options.setPrettyFlow(true);
			
			Yaml yaml = new Yaml(options);
			FileWriter fileWriter = null;
			
			fileWriter = new FileWriter(new File("createRule.yaml"));
		    yaml.dump(map, fileWriter);
		    fileWriter.close();
			
    		String yamlString = FileUtils.readFileToString(new File("createRule.yaml"), "utf8");
    		System.out.println(yamlString);*/
			
//			HashMap<String, String> data = new HashMap<String, String>();
//			data.put("users-rules.rules", yamlString);
//			
//			configMap.setData(data);
//			System.out.println(configMap);
			
//			V1ConfigMap replacedConfigmap = api.replaceNamespacedConfigMap("prometheus-user-rules", "monitoring", configMap, null);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return createRuleVo;
	}
	
}
