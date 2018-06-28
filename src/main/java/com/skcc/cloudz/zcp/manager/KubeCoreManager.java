package com.skcc.cloudz.zcp.manager;

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
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.skcc.cloudz.zcp.common.vo.ChannelData;
import com.skcc.cloudz.zcp.common.vo.ChannelDtlVo;
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

	@Value("${props.alertManager.configMap}")
	private String alertConfigMap;

	@Value("${props.alertManager.namespace}")
	private String alertNamespace;

	@Value("${props.prometheus.configMap}")
	private String promConfigMap;

	@Value("${props.prometheus.namespace}")
	private String promNamespace;

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

			configMap = api.readNamespacedConfigMap(alertConfigMap, alertNamespace, null, null, null);

			File file = new File("channel.yaml");

			writer = new FileWriter(file, false);
			writer.write(configMap.getData().get("config.yml"));
			writer.flush();

			YamlReader reader = new YamlReader(new FileReader("channel.yaml"));
			Object object = reader.read();

			Map<String, Map<String, Object>> mapGlobal = (Map) object;
			listChannel = (List) mapGlobal.get("receivers");

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return listChannel;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public ChannelData createChannel(ChannelData channel) {

		List<ChannelData> ruleList = new ArrayList<ChannelData>();
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

			configMap = api.readNamespacedConfigMap(alertConfigMap, alertNamespace, null, null, null);
			File file = new File("channel.yaml");

			writer = new FileWriter(file, false);
			writer.write(configMap.getData().get("config.yml"));
			writer.flush();

			YamlReader reader = new YamlReader(new FileReader("channel.yaml"));
			Object object = reader.read();

			Map<String, Map<String, Object>> mapGlobal = (Map) object;

			routeMap = mapGlobal.get("route");
			routesList = (List) routeMap.get("routes");

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

			receiverList = (List) mapGlobal.get("receivers");

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
			V1ConfigMap replacedConfigmap = api.replaceNamespacedConfigMap(alertConfigMap, alertNamespace, configMap,
					null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return channel;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public ChannelDtlVo updateChannel(int id, ChannelDtlVo channelDtlVo) {
		FileWriter writer = null;
		List receiverList = null;
		Map<String, Object> newChannelMap = new LinkedHashMap<String, Object>();

		try {
			ApiClient client = Config.defaultClient();
			Configuration.setDefaultApiClient(client);

			CoreV1Api api = new CoreV1Api();
			V1ConfigMap configMap;

			configMap = api.readNamespacedConfigMap(alertConfigMap, alertNamespace, null, null, null);

			File file = new File("channel.yaml");

			writer = new FileWriter(file, false);
			writer.write(configMap.getData().get("config.yml"));
			writer.flush();

			YamlReader reader = new YamlReader(new FileReader("channel.yaml"));
			Object object = reader.read();

			List emailList = new ArrayList();
			List slackList = new ArrayList();
			List hipchatList = new ArrayList();
			List webhookList = new ArrayList();
			Map<String, Map<String, Object>> channelMap = (Map) object;
			HashMap<String, Object> newReceiver = new LinkedHashMap<String, Object>();

			receiverList = (List) channelMap.get("receivers");
			receiverList.remove(id + 2);

			if (!"".equals(channelDtlVo.getEmail_to())) {
				Map<String, Object> emailMap = new LinkedHashMap<String, Object>();

				emailMap.put("to", channelDtlVo.getEmail_to());
				if (!"".equals(channelDtlVo.getEmail_from()))
					emailMap.put("from", channelDtlVo.getEmail_from());
				if (!"".equals(channelDtlVo.getEmail_smarthost()))
					emailMap.put("smarthost", channelDtlVo.getEmail_smarthost());
				if (!"".equals(channelDtlVo.getEmail_auth_username()))
					emailMap.put("auth_username", channelDtlVo.getEmail_auth_username());
				if (!"".equals(channelDtlVo.getEmail_auth_password()))
					emailMap.put("auth_password", channelDtlVo.getEmail_auth_password());
				if (!"".equals(channelDtlVo.getEmail_require_tls()))
					emailMap.put("require_tls", channelDtlVo.getEmail_require_tls());
				if (!"".equals(channelDtlVo.getEmail_send_resolved()))
					emailMap.put("send_resolved", channelDtlVo.getEmail_send_resolved());
				emailList.add(emailMap);

				newReceiver.put("name", channelDtlVo.getChannel());
				newReceiver.put("email_configs", emailList);
			}

			if (!"".equals(channelDtlVo.getSlack_api_url())) {
				Map<String, Object> slackMap = new LinkedHashMap<String, Object>();

				slackMap.put("api_url", channelDtlVo.getSlack_api_url());
				if (!"".equals(channelDtlVo.getSlack_send_resolved()))
					slackMap.put("send_resolved", channelDtlVo.getSlack_send_resolved());
				slackList.add(slackMap);

				newReceiver.put("name", channelDtlVo.getChannel());
				newReceiver.put("slack_configs", slackList);
			}

			if (!"".equals(channelDtlVo.getHipchat_api_url())) {
				Map<String, Object> hipchatMap = new LinkedHashMap<String, Object>();

				hipchatMap.put("api_url", channelDtlVo.getHipchat_api_url());
				if (!"".equals(channelDtlVo.getHipchat_room_id()))
					hipchatMap.put("room_id", channelDtlVo.getHipchat_room_id());
				if (!"".equals(channelDtlVo.getHipchat_auth_token()))
					hipchatMap.put("auth_token", channelDtlVo.getHipchat_auth_token());
				if (!"".equals(channelDtlVo.getHipchat_notify()))
					hipchatMap.put("notify", channelDtlVo.getHipchat_notify());
				if (!"".equals(channelDtlVo.getHipchat_send_resolved()))
					hipchatMap.put("send_resolved", channelDtlVo.getHipchat_send_resolved());

				hipchatList.add(hipchatMap);

				newReceiver.put("name", channelDtlVo.getChannel());
				newReceiver.put("hipchat_configs", hipchatList);
			}

			if (!"".equals(channelDtlVo.getWebhook_url())) {
				Map<String, Object> webhookMap = new LinkedHashMap<String, Object>();

				webhookMap.put("url", channelDtlVo.getWebhook_url());
				if (!"".equals(channelDtlVo.getWebhook_send_resolved()))
					webhookMap.put("send_resolved", channelDtlVo.getWebhook_send_resolved());

				webhookList.add(webhookMap);

				newReceiver.put("name", channelDtlVo.getChannel());
				newReceiver.put("webhook_configs", webhookList);
			}

			receiverList.add(newReceiver);

			newChannelMap.put("global", channelMap.get("global"));
			newChannelMap.put("templates", channelMap.get("templates"));
			newChannelMap.put("route", channelMap.get("route"));
			newChannelMap.put("receivers", receiverList);

			YamlConfig config = new YamlConfig();
			YamlWriter ywriter = new YamlWriter(new FileWriter("channel.yaml"), config);
			ywriter.write(newChannelMap);
			ywriter.close();

			String yamlString = FileUtils.readFileToString(new File("channel.yaml"), "utf8");

			Map<String, String> data = new HashMap<String, String>();
			data.put("config.yml", yamlString);

			configMap.setData(data);
			V1ConfigMap replacedConfigmap = api.replaceNamespacedConfigMap(alertConfigMap, alertNamespace, configMap,
					null);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return channelDtlVo;
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

			configMap = api.readNamespacedConfigMap(alertConfigMap, alertNamespace, null, null, null);

			File file = new File("channel.yaml");

			writer = new FileWriter(file, false);
			writer.write(configMap.getData().get("config.yml"));
			writer.flush();

			YamlReader reader = new YamlReader(new FileReader("channel.yaml"));
			Object object = reader.read();

			Map<String, Map<String, Object>> mapGlobal = (Map) object;
			routeMap = mapGlobal.get("route");

			routesList = (List) routeMap.get("routes");
			routesList.remove(id + 2);

			Map<String, Object> newRouteMap = new HashMap<String, Object>();
			newRouteMap.put("routes", routesList);

			receiverList = (List) mapGlobal.get("receivers");
			receiverList.remove(id + 2);

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
			V1ConfigMap replacedConfigmap = api.replaceNamespacedConfigMap(alertConfigMap, alertNamespace, configMap,
					null);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public List<RuleData> getRuleList() {
		FileWriter writer = null;
		List listRules = null;

		try {
			ApiClient client = Config.defaultClient();
			Configuration.setDefaultApiClient(client);

			CoreV1Api api = new CoreV1Api();
			V1ConfigMap configMap;

			configMap = api.readNamespacedConfigMap(promConfigMap, promNamespace, null, null, null);
			File file = new File("rule.yaml");

			writer = new FileWriter(file, false);

			Iterator<String> iter = configMap.getData().keySet().iterator();
			List keyList = new LinkedList();

			while (iter.hasNext()) {
				String keys = iter.next();
				keyList.add(keys);
			}
			writer.write(configMap.getData().get(keyList.get(0)));
			writer.flush();

			YamlReader reader = new YamlReader(new FileReader("rule.yaml"));
			Object object = reader.read();

			Map<String, Map<String, Object>> mapGroups = (Map) object;
			List listGroups = (List) mapGroups.get("groups");

			if (listGroups != null) {
				Map<String, Object> maplistGroups;
				Iterator iteratorData = listGroups.iterator();

				RuleData ruleData = new RuleData();
				maplistGroups = (Map) iteratorData.next();

				Map<String, Object> maplistRules;
				listRules = (List) maplistGroups.get("rules");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
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

			configMap = api.readNamespacedConfigMap(promConfigMap, promNamespace, null, null, null);

			Iterator<String> iter = configMap.getData().keySet().iterator();
			List keyList = new LinkedList();

			while (iter.hasNext()) {
				String keys = iter.next();
				keyList.add(keys);
			}

			File file = new File("rule.yaml");

			writer = new FileWriter(file, false);
			writer.write(configMap.getData().get(keyList.get(0)));
			writer.flush();

			HashMap<String, String> labels = new HashMap<String, String>();
			labels.put("severity", createRuleVo.getRuleSeverity());
			labels.put("channel", createRuleVo.getRuleChannel());

			HashMap<String, String> annotations = new HashMap<String, String>();

			createRuleVo.setRuleDescription(message.get(createRuleVo.getRuleAlert()));
			annotations.put("description", createRuleVo.getRuleDescription());

			HashMap<String, Object> newRules = new LinkedHashMap<String, Object>();

			newRules.put("alert", createRuleVo.getRuleAlert());
			newRules.put("expr", createRuleVo.getRuleExpr());
			newRules.put("for", createRuleVo.getRuleFor());
			newRules.put("labels", labels);
			newRules.put("annotations", annotations);

			YamlReader reader = new YamlReader(new FileReader("rule.yaml"));
			Object object = reader.read();

			Map<String, Map<String, Object>> mapGroups = (Map) object;
			List listGroups = (List) mapGroups.get("groups");

			Map<String, Object> groupMap = new HashMap<String, Object>();
			Map<String, Object> groups = new HashMap<String, Object>();
			Map<String, Object> maplistGroups = null;

			List groupList = new ArrayList();

			if (listGroups != null) {
				Iterator iteratorData = listGroups.iterator();

				while (iteratorData.hasNext()) {
					maplistGroups = (Map) iteratorData.next();
				}

				Map<String, Object> maplistRules;

				List listRules = (List) maplistGroups.get("rules");
				listRules.add(newRules);

				groups.put("name", keyList.get(0));
				groups.put("rules", listRules);

				groupList.add(groups);

				groupMap.put("groups", groupList);

			} else {
				List newListRules = new ArrayList();
				newListRules.add(newRules);

				groups.put("name", keyList.get(0));
				groups.put("rules", newListRules);

				groupList.add(groups);

				groupMap.put("groups", groupList);
			}

			YamlConfig config = new YamlConfig();
			YamlWriter ywriter = new YamlWriter(new FileWriter("rule.yaml"), config);
			ywriter.write(groupMap);
			ywriter.close();

			String yamlString = FileUtils.readFileToString(new File("rule.yaml"), "utf8");

			Map<String, String> data = new HashMap<String, String>();
			data.put(keyList.get(0).toString(), yamlString);

			configMap.setData(data);
			V1ConfigMap replacedConfigmap = api.replaceNamespacedConfigMap(promConfigMap, promNamespace, configMap,
					null);

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

			configMap = api.readNamespacedConfigMap(promConfigMap, promNamespace, null, null, null);

			File file = new File("rule.yaml");

			writer = new FileWriter(file, false);
			writer.write(configMap.getData().get("users-rules.rules"));
			writer.flush();

			YamlReader reader = new YamlReader(new FileReader("rule.yaml"));
			Object object = reader.read();

			Map<String, Map<String, Object>> mapGroups = (Map) object;
			List listGroups = (List) mapGroups.get("groups");

			Iterator iteratorData = listGroups.iterator();
			Map<String, Object> maplistGroups;

			RuleData ruleData = new RuleData();
			List listRules = new ArrayList();

			while (iteratorData.hasNext()) {
				maplistGroups = (Map) iteratorData.next();
				listRules = (List) maplistGroups.get("rules");
				for (int cnt = 0; cnt < listRules.size(); cnt++) {
					if (cnt == ruleId) {
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
			V1ConfigMap replacedConfigmap = api.replaceNamespacedConfigMap(promConfigMap, promNamespace, configMap,
					null);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

}
