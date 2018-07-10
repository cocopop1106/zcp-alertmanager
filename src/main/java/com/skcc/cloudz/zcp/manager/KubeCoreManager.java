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
import com.skcc.cloudz.zcp.common.vo.ChannelVo;
import com.skcc.cloudz.zcp.common.vo.RepeatVo;
import com.skcc.cloudz.zcp.common.vo.RuleData;
import com.skcc.cloudz.zcp.common.util.Message;
import com.skcc.cloudz.zcp.common.yamlbeans.YamlConfig;
import com.skcc.cloudz.zcp.common.yamlbeans.YamlReader;
import com.skcc.cloudz.zcp.common.yamlbeans.YamlWriter;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1ConfigMap;
import io.kubernetes.client.models.V1NamespaceList;
import io.kubernetes.client.models.V1PodList;
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

			Map<String, Object> maplistGroups;
			Iterator iteratorData = receiverList.iterator();

			int count = 0;
			int removeId = 0;

			Map<String, Object> email_configs = new HashMap<String, Object>();
			Map<String, Object> slack_configs = new HashMap<String, Object>();
			Map<String, Object> hipchat_configs = new HashMap<String, Object>();
			Map<String, Object> webhook_configs = new HashMap<String, Object>();

			int mapCount = 0;

			while (iteratorData.hasNext()) {
				maplistGroups = (Map) iteratorData.next();

				if (mapCount == id) {
					if (maplistGroups.get("email_configs") != null) {
						email_configs.put("email_configs", maplistGroups.get("email_configs"));
					}
					if (maplistGroups.get("slack_configs") != null) {
						slack_configs.put("slack_configs", maplistGroups.get("slack_configs"));
					}
					if (maplistGroups.get("hipchat_configs") != null) {
						hipchat_configs.put("hipchat_configs", maplistGroups.get("hipchat_configs"));
					}
					if (maplistGroups.get("webhook_configs") != null) {
						webhook_configs.put("webhook_configs", maplistGroups.get("webhook_configs"));
					}
				}
				mapCount++;
			}
			receiverList.remove(id);

			if (channelDtlVo.getEmail_to() != null && !"".equals(channelDtlVo.getEmail_to())) {
				Map<String, Object> emailMap = new LinkedHashMap<String, Object>();

				emailMap.put("to", channelDtlVo.getEmail_to());

				if (channelDtlVo.getEmail_from() != null && !"".equals(channelDtlVo.getEmail_from())) {
					emailMap.put("from", channelDtlVo.getEmail_from());
				} else {
					emailMap.put("from", "'Alertmanager <alertmanager@zcp.test.com>'");
				}

				if (channelDtlVo.getEmail_smarthost() != null && !"".equals(channelDtlVo.getEmail_smarthost())) {
					emailMap.put("smarthost", channelDtlVo.getEmail_smarthost());
				} else {
					emailMap.put("smarthost", "smtp.sendgrid.net:465");
				}

				if (channelDtlVo.getEmail_auth_username() != null
						&& !"".equals(channelDtlVo.getEmail_auth_username())) {
					emailMap.put("auth_username", channelDtlVo.getEmail_auth_username());
				} else {
					emailMap.put("auth_username", "zcp-sender-api-key");
				}

				if (channelDtlVo.getEmail_auth_password() != null
						&& !"".equals(channelDtlVo.getEmail_auth_password())) {
					emailMap.put("auth_password", channelDtlVo.getEmail_auth_password());
				} else {
					emailMap.put("auth_password",
							"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");
				}

				if (channelDtlVo.getEmail_require_tls() != null && !"".equals(channelDtlVo.getEmail_require_tls())) {
					emailMap.put("require_tls", channelDtlVo.getEmail_require_tls());
				} else {
					emailMap.put("require_tls", "false");
				}

				if (channelDtlVo.getEmail_send_resolved() != null
						&& !"".equals(channelDtlVo.getEmail_send_resolved())) {
					emailMap.put("send_resolved", channelDtlVo.getEmail_send_resolved());
				} else {
					emailMap.put("send_resolved", "true");
				}

				emailList.add(emailMap);

				newReceiver.put("name", channelDtlVo.getChannel());
				newReceiver.put("email_configs", emailList);
			} else {
				if (email_configs.get("email_configs") != null) {
					newReceiver.put("name", channelDtlVo.getChannel());
					newReceiver.put("email_configs", email_configs.get("email_configs"));
				}
			}

			if (channelDtlVo.getSlack_api_url() != null && !"".equals(channelDtlVo.getSlack_api_url())) {
				Map<String, Object> slackMap = new LinkedHashMap<String, Object>();

				slackMap.put("api_url", channelDtlVo.getSlack_api_url());

				if (channelDtlVo.getSlack_send_resolved() != null
						&& !"".equals(channelDtlVo.getSlack_send_resolved())) {
					slackMap.put("send_resolved", channelDtlVo.getSlack_send_resolved());
				} else {
					slackMap.put("send_resolved", "true");
				}

				slackList.add(slackMap);

				newReceiver.put("name", channelDtlVo.getChannel());
				newReceiver.put("slack_configs", slackList);
			} else {
				if (slack_configs.get("slack_configs") != null) {
					newReceiver.put("name", channelDtlVo.getChannel());
					newReceiver.put("slack_configs", slack_configs.get("slack_configs"));
				}
			}

			if (channelDtlVo.getHipchat_api_url() != null && !"".equals(channelDtlVo.getHipchat_api_url())) {
				Map<String, Object> hipchatMap = new LinkedHashMap<String, Object>();

				hipchatMap.put("api_url", channelDtlVo.getHipchat_api_url());

				if (channelDtlVo.getHipchat_room_id() != null && !"".equals(channelDtlVo.getHipchat_room_id())) {
					hipchatMap.put("room_id", channelDtlVo.getHipchat_room_id());
				} else {
					hipchatMap.put("room_id", "4603546");
				}

				if (channelDtlVo.getHipchat_auth_token() != null && !"".equals(channelDtlVo.getHipchat_auth_token())) {
					hipchatMap.put("auth_token", channelDtlVo.getHipchat_auth_token());
				} else {
					hipchatMap.put("auth_token", "HSlJFvopWxhkiqg6YsqTjOxap59JhnndCwgmwK8N");
				}

				if (channelDtlVo.getHipchat_notify() != null && !"".equals(channelDtlVo.getHipchat_notify())) {
					hipchatMap.put("notify", channelDtlVo.getHipchat_notify());
				} else {
					hipchatMap.put("notify", "true");
				}

				if (channelDtlVo.getHipchat_send_resolved() != null
						&& !"".equals(channelDtlVo.getHipchat_send_resolved())) {
					hipchatMap.put("send_resolved", channelDtlVo.getHipchat_send_resolved());
				} else {
					hipchatMap.put("send_resolved", "true");
				}

				hipchatList.add(hipchatMap);

				newReceiver.put("name", channelDtlVo.getChannel());
				newReceiver.put("hipchat_configs", hipchatList);
			} else {
				if (hipchat_configs.get("hipchat_configs") != null) {
					newReceiver.put("name", channelDtlVo.getChannel());
					newReceiver.put("hipchat_configs", hipchat_configs.get("hipchat_configs"));
				}
			}

			if (channelDtlVo.getWebhook_url() != null && !"".equals(channelDtlVo.getWebhook_url())) {
				Map<String, Object> webhookMap = new LinkedHashMap<String, Object>();

				webhookMap.put("url", channelDtlVo.getWebhook_url());

				if (channelDtlVo.getWebhook_send_resolved() != null
						&& !"".equals(channelDtlVo.getWebhook_send_resolved())) {
					webhookMap.put("send_resolved", channelDtlVo.getWebhook_send_resolved());
				} else {
					webhookMap.put("send_resolved", "true");
				}

				webhookList.add(webhookMap);

				newReceiver.put("name", channelDtlVo.getChannel());
				newReceiver.put("webhook_configs", webhookList);
			} else {
				if (webhook_configs.get("webhook_configs") != null) {
					newReceiver.put("name", channelDtlVo.getChannel());
					newReceiver.put("webhook_configs", webhook_configs.get("webhook_configs"));
				}
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

			Map<String, Object> maplistGroups;
			Iterator iteratorData = routesList.iterator();

			int count = 0;
			int removeId = 0;

			Map<String, Object> matchMap = new HashMap<String, Object>();

			while (iteratorData.hasNext()) {
				maplistGroups = (Map) iteratorData.next();
				matchMap = (Map<String, Object>) maplistGroups.get("match");

				if (matchMap != null) {
					if (channel.equals(matchMap.get("channel"))) {
						removeId = count;
					}
				}
				count++;
			}
			routesList.remove(removeId);

			Map<String, Object> newRouteMap = new LinkedHashMap<String, Object>();

			newRouteMap.put("group_by", routeMap.get("group_by"));
			newRouteMap.put("group_wait", routeMap.get("group_wait"));
			newRouteMap.put("group_interval", routeMap.get("group_interval"));
			newRouteMap.put("repeat_interval", routeMap.get("repeat_interval"));
			newRouteMap.put("receiver", routeMap.get("receiver"));
			newRouteMap.put("routes", routesList);

			receiverList = (List) mapGlobal.get("receivers");
			receiverList.remove(id);

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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> getRepeatInterval() {

		FileWriter writer = null;
		Map<String, Object> routeMap = new HashMap<String, Object>();

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

		} catch (Exception e) {
			e.printStackTrace();
		}

		return routeMap;

	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public RepeatVo updateRepeatInterval(RepeatVo repeatVo) {
		FileWriter writer = null;

		Map<String, Object> routeMap = new LinkedHashMap<String, Object>();
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
			HashMap<String, Object> newRoute = new LinkedHashMap<String, Object>();

			routeMap = channelMap.get("route");

			if (repeatVo != null) {
				routeMap.put("repeat_interval", repeatVo.getRepeat_interval());
			}

			newChannelMap.put("global", channelMap.get("global"));
			newChannelMap.put("templates", channelMap.get("templates"));
			newChannelMap.put("route", channelMap.get("route"));
			newChannelMap.put("receivers", channelMap.get("receivers"));

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

		return repeatVo;
	}

	public V1NamespaceList getNamespaceList() {
		V1NamespaceList namespaceList = null;

		try {
			ApiClient client = Config.defaultClient();
			Configuration.setDefaultApiClient(client);

			CoreV1Api api = new CoreV1Api();
			namespaceList = api.listNamespace(null, null, null, null, null, null, null, null, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return namespaceList;
	}

	public V1PodList getPodList(String namespace) {
		V1PodList podList = null;

		try {
			ApiClient client = Config.defaultClient();
			Configuration.setDefaultApiClient(client);

			CoreV1Api api = new CoreV1Api();
			podList = api.listNamespacedPod(namespace, null, null, null, null, null, null, null, null, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return podList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public ChannelVo updateChannelName(int id, ChannelVo channelVo) {
		FileWriter writer = null;
		List receiverList = null;
		Map<String, Object> newChannelMap = new LinkedHashMap<String, Object>();

		Map<String, Object> routeMap = new HashMap<String, Object>();
		Map<String, Object> newRouteMap = new LinkedHashMap<String, Object>();
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

			Map<String, Map<String, Object>> channelMap = (Map) object;
			HashMap<String, Object> newReceiver = new LinkedHashMap<String, Object>();

			routeMap = channelMap.get("route");
			routesList = (List) routeMap.get("routes");

			receiverList = (List) channelMap.get("receivers");

			Map<String, Object> maplistGroups;
			Iterator iteratorData = receiverList.iterator();

			int mapCount = 0;
			String beforeName = "";

			while (iteratorData.hasNext()) {
				maplistGroups = (Map) iteratorData.next();

				if (mapCount == id) {
					beforeName = maplistGroups.get("name").toString();

					newReceiver.put("name", channelVo.getChannel());

					if (maplistGroups.get("email_configs") != null) {
						newReceiver.put("email_configs", maplistGroups.get("email_configs"));
					}
					if (maplistGroups.get("slack_configs") != null) {
						newReceiver.put("slack_configs", maplistGroups.get("slack_configs"));
					}
					if (maplistGroups.get("hipchat_configs") != null) {
						newReceiver.put("hipchat_configs", maplistGroups.get("hipchat_configs"));
					}
					if (maplistGroups.get("webhook_configs") != null) {
						newReceiver.put("webhook_configs", maplistGroups.get("webhook_configs"));
					}
				}
				mapCount++;
			}
			receiverList.remove(id);
			receiverList.add(newReceiver);

			Map<String, Object> maplistRoute;
			Iterator iteratorRoute = routesList.iterator();

			int count = 0;
			int removeId = 0;

			Map<String, Object> matchMap = new HashMap<String, Object>();

			while (iteratorRoute.hasNext()) {
				maplistRoute = (Map) iteratorRoute.next();
				matchMap = (Map<String, Object>) maplistRoute.get("match");

				if (matchMap != null) {
					if (beforeName.equals(matchMap.get("channel"))) {
						removeId = count;
					}
				}
				count++;
			}
			routesList.remove(removeId);

			HashMap<String, Object> newMatchMap = new LinkedHashMap<String, Object>();
			newMatchMap.put("channel", channelVo.getChannel());
			newMatchMap.put("receiver", channelVo.getChannel());

			HashMap<String, Object> routesMap = new HashMap<String, Object>();
			routesMap.put("match", newMatchMap);

			routesList.add(routesMap);

			newRouteMap.put("group_by", routeMap.get("group_by"));
			newRouteMap.put("group_wait", routeMap.get("group_wait"));
			newRouteMap.put("group_interval", routeMap.get("group_interval"));
			newRouteMap.put("repeat_interval", routeMap.get("repeat_interval"));
			newRouteMap.put("receiver", routeMap.get("receiver"));
			newRouteMap.put("routes", routesList);

			newChannelMap.put("global", channelMap.get("global"));
			newChannelMap.put("templates", channelMap.get("templates"));
			newChannelMap.put("route", newRouteMap);
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

		return channelVo;
	}

	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	public Boolean deleteNotification(int id, String channel, ChannelDtlVo channelDtlVo) {
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

			receiverList = (List) mapGlobal.get("receivers");

			HashMap<String, Object> newReceiver = new LinkedHashMap<String, Object>();

			Map<String, Object> maplistGroups;
			Iterator iteratorData = receiverList.iterator();

			int mapCount = 0;

			while (iteratorData.hasNext()) {
				maplistGroups = (Map) iteratorData.next();

				if (mapCount == id) {
					newReceiver.put("name", channel);

					if (channelDtlVo.getEmail_to() == null) {
						if (maplistGroups.get("email_configs") != null) {
							newReceiver.put("email_configs", maplistGroups.get("email_configs"));
						}
					}

					if (channelDtlVo.getSlack_api_url() == null) {
						if (maplistGroups.get("slack_configs") != null) {
							newReceiver.put("slack_configs", maplistGroups.get("slack_configs"));
						}
					}

					if (channelDtlVo.getHipchat_api_url() == null) {
						if (maplistGroups.get("hipchat_configs") != null) {
							newReceiver.put("hipchat_configs", maplistGroups.get("hipchat_configs"));
						}
					}

					if (channelDtlVo.getWebhook_url() == null) {
						if (maplistGroups.get("webhook_configs") != null) {
							newReceiver.put("webhook_configs", maplistGroups.get("webhook_configs"));
						}
					}

				}
				mapCount++;
			}
			receiverList.remove(id);
			receiverList.add(newReceiver);

			Map<String, Object> channelMap = new LinkedHashMap<String, Object>();

			channelMap.put("global", mapGlobal.get("global"));
			channelMap.put("templates", mapGlobal.get("templates"));
			channelMap.put("route", mapGlobal.get("route"));
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

}
