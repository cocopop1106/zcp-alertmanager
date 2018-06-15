package com.skcc.cloudz.zcp.manager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.skcc.cloudz.zcp.common.vo.ChannelData;
import com.skcc.cloudz.zcp.common.vo.ChannelVo;
import com.skcc.cloudz.zcp.common.vo.RuleData;
import com.skcc.cloudz.zcp.common.util.Message;
import com.skcc.cloudz.zcp.common.yamlbeans.YamlConfig;
import com.skcc.cloudz.zcp.common.yamlbeans.YamlReader;
import com.skcc.cloudz.zcp.common.yamlbeans.YamlWriter;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1ConfigMap;
import io.kubernetes.client.util.Config;

@Component
public class KubeCoreManager {
	@SuppressWarnings("unused")
	private final Logger logger = (Logger) LoggerFactory.getLogger(KubeCoreManager.class);

	@Autowired
	Message message;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ChannelData> getChannelList() {

		FileWriter writer = null;
		List listChannel = null;

		try {
			ApiClient client = Config.defaultClient();
			Configuration.setDefaultApiClient(client);

			CoreV1Api api = new CoreV1Api();
			V1ConfigMap configMap;

			configMap = api.readNamespacedConfigMap("test-alertmanager", "monitoring", null, null, null);

			File file = new File("channel.yaml");

			writer = new FileWriter(file, false);
			writer.write(configMap.getData().get("config.yml"));
			writer.flush();

			YamlReader reader = new YamlReader(new FileReader("channel.yaml"));
			Object object = reader.read();

			Map<String, Map<String, Object>> mapGlobal = (Map)object;
			listChannel = (List)mapGlobal.get("receivers");

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				if(writer != null) writer.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}

		return listChannel;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public ChannelData createChannel(ChannelData channel) {

		List<RuleData> ruleList = new ArrayList<RuleData>();
		FileWriter writer = null;
		List receiverList = null;
		Map<String, Object> routeMap = new HashMap<String, Object>();
		Map<String, Object> newRouteMap = new LinkedHashMap<String, Object>();
		List routesList = null;

		try {
			ApiClient client = Config.defaultClient();
			Configuration.setDefaultApiClient(client);

			CoreV1Api api = new CoreV1Api();
			V1ConfigMap configMap = new V1ConfigMap();

			configMap = api.readNamespacedConfigMap("test-alertmanager", "monitoring", null, null, null);
			File file = new File("channel.yaml");

			writer = new FileWriter(file, false);
			writer.write(configMap.getData().get("config.yml"));
			writer.flush();

			YamlReader reader = new YamlReader(new FileReader("channel.yaml"));
			Object object = reader.read();

			Map<String, Map<String, Object>> mapGlobal = (Map)object;

			routeMap = mapGlobal.get("route");
			routesList = (List)routeMap.get("routes");

			HashMap<String, Object> matchMap = new LinkedHashMap<String, Object>();
			matchMap.put("channel", channel.getChannel());
			matchMap.put("receiver", channel.getChannel());

			HashMap<String, Object> routesMap = new HashMap<String, Object>();
			routesMap.put("match", matchMap);

			routesList.add(routesMap);

			newRouteMap.put("group_by", routeMap.get("group_by"));
			newRouteMap.put("group_wait", routeMap.get("group_wait"));
			newRouteMap.put("group_interval", routeMap.get("group_interval"));
			newRouteMap.put("repeat_interval", routeMap.get("repeat_interval"));
			newRouteMap.put("receiver", routeMap.get("receiver"));
			newRouteMap.put("routes", routesList);

			receiverList = (List)mapGlobal.get("receivers");

			HashMap<String, Object> newReceiver = new LinkedHashMap<String, Object>();
			newReceiver.put("name", channel.getChannel());

			receiverList.add(newReceiver);

			Map<String, Object> channelMap = new LinkedHashMap<String, Object>();
			channelMap.put("global", mapGlobal.get("global"));
			channelMap.put("templates", mapGlobal.get("templates"));
			channelMap.put("route", newRouteMap);
			channelMap.put("receivers", receiverList);

			YamlConfig config = new YamlConfig();
			YamlWriter ywriter = new YamlWriter(new FileWriter("channel.yaml"), config);
			ywriter.write(channelMap);
			ywriter.close();

			String yamlString = FileUtils.readFileToString(new File("channel.yaml"), "utf8");

			Map<String, String> data = new HashMap<String, String>();
			data.put("config.yml", yamlString);

			configMap.setData(data);
			V1ConfigMap replacedConfigmap = api.replaceNamespacedConfigMap("test-alertmanager", "monitoring", configMap, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return channel;
	}

	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	public Boolean deleteChannel(int id, String channel) {
		List<RuleData> ruleList = new ArrayList<RuleData>();
		FileWriter writer = null;
		List receiverList = null;
		Map<String, Object> routeMap = new HashMap<String, Object>();
		List routesList = null;

		try {
			ApiClient client = Config.defaultClient();
			Configuration.setDefaultApiClient(client);

			CoreV1Api api = new CoreV1Api();
			V1ConfigMap configMap;

			configMap = api.readNamespacedConfigMap("test-alertmanager", "monitoring", null, null, null);

			File file = new File("channel.yaml");

			writer = new FileWriter(file, false);
			writer.write(configMap.getData().get("config.yml"));
			writer.flush();

			YamlReader reader = new YamlReader(new FileReader("channel.yaml"));
			Object object = reader.read();

			Map<String, Map<String, Object>> mapGlobal = (Map)object;
			routeMap = mapGlobal.get("route");

			routesList = (List)routeMap.get("routes");
			routesList.remove(id+2);

			Map<String, Object> newRouteMap = new HashMap<String, Object>();
			newRouteMap.put("routes", routesList);

			receiverList = (List)mapGlobal.get("receivers");
			receiverList.remove(id+2);

			Map<String, Object> channelMap = new LinkedHashMap<String, Object>();
			channelMap.put("global", mapGlobal.get("global"));
			channelMap.put("templates", mapGlobal.get("templates"));
			channelMap.put("route", newRouteMap);
			channelMap.put("receivers", receiverList);

			YamlConfig config = new YamlConfig();
			YamlWriter ywriter = new YamlWriter(new FileWriter("channel.yaml"), config);
			ywriter.write(channelMap);
			ywriter.close();

			String yamlString = FileUtils.readFileToString(new File("channel.yaml"), "utf8");

			Map<String, String> data = new HashMap<String, String>();
			data.put("config.yml", yamlString);

			configMap.setData(data);
			V1ConfigMap replacedConfigmap = api.replaceNamespacedConfigMap("test-alertmanager", "monitoring", configMap, null);

		} catch (Exception e) {
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

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public List<RuleData> getRuleList()  {
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

	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	public RuleData createRule(RuleData createRuleVo) {
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
			e.printStackTrace();
		}
		return createRuleVo;
	}

	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	public Boolean deleteRule(int ruleId) {
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

}
