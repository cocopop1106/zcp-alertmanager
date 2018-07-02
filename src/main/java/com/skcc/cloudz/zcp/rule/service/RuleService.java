package com.skcc.cloudz.zcp.rule.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.skcc.cloudz.zcp.common.vo.RuleData;
import com.skcc.cloudz.zcp.common.vo.RuleVo;
import com.skcc.cloudz.zcp.manager.KubeCoreManager;

import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1NamespaceList;

@Service
public class RuleService {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(RuleService.class);

	@Autowired
	private KubeCoreManager kubeCoreManager;

	@Value("${props.prometheus.baseUrl}")
	private String baseUrl;

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public List<RuleVo> getRuleList() {
		List listRules = kubeCoreManager.getRuleList();

		List<RuleVo> ruleViewList = new ArrayList<RuleVo>();
		Map<String, Object> maplistRules;
		int count = 0;
		String condition = "";

		if (listRules != null) {
			Iterator iteratorRules = listRules.iterator();

			while (iteratorRules.hasNext()) {
				maplistRules = (Map) iteratorRules.next();

				RuleVo rule = new RuleVo();

				Map<String, Object> maplistLabels;
				maplistLabels = (Map<String, Object>) maplistRules.get("labels");

				Map<String, Object> maplistAnnotations;
				maplistAnnotations = (Map<String, Object>) maplistRules.get("annotations");

				rule.setId(count);
				rule.setType(maplistRules.get("alert").toString());
				rule.setSeverity(maplistLabels.get("severity").toString());

				if ("NodeDown".equals(rule.getType())) {
					condition = "=";

					rule.setCondition("");
					String[] gb = maplistRules.get("expr").toString().split("=");

					rule.setValue1("");
					rule.setValue2("");

				} else if ("ApiserverDown".equals(rule.getType())) {
					condition = "=";

					rule.setCondition("");
					String[] gb = maplistRules.get("expr").toString().split("=");

					rule.setValue1("");
					rule.setValue2("");

				} else if ("K8SNodeNotReady".equals(rule.getType())) {
					condition = "=";

					rule.setCondition("");
					String[] gb = maplistRules.get("expr").toString().split("=");

					rule.setValue1("");
					rule.setValue2("");

				} else if ("PodFrequentlyRestarting".equals(rule.getType())) {
					rule.setCondition("");
					rule.setValue1("");
					rule.setValue2("");

				} else {
					if (maplistRules.get("expr").toString().indexOf(">") >= 0) {
						condition = maplistRules.get("expr").toString().substring(
								maplistRules.get("expr").toString().indexOf(">"),
								maplistRules.get("expr").toString().indexOf(">") + 1);
					} else {
						condition = maplistRules.get("expr").toString().substring(
								maplistRules.get("expr").toString().indexOf("<"),
								maplistRules.get("expr").toString().indexOf("<") + 1);
					}

					rule.setCondition(condition);
					String[] gb = maplistRules.get("expr").toString().split(">|<");

					rule.setValue1(gb[0]);
					rule.setValue2(gb[1]);
				}

				if (maplistRules.get("for") != null)
					rule.setDuration(maplistRules.get("for").toString());
				rule.setChannel(maplistLabels.get("channel").toString());

				rule.setValue(rule.getCondition() + rule.getValue2());

				ruleViewList.add(count, rule);
				count++;
			}
		}

		return ruleViewList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public RuleVo findById(int ruleId) {
		List listRules = kubeCoreManager.getRuleList();
		List<RuleVo> ruleViewList = new ArrayList<RuleVo>();

		Map<String, Object> maplistRules;
		int count = 0;
		String condition = "";
		RuleVo ruleVo = new RuleVo();

		Iterator iteratorRules = listRules.iterator();

		while (iteratorRules.hasNext()) {
			maplistRules = (Map) iteratorRules.next();

			RuleVo rule = new RuleVo();

			Map<String, Object> maplistLabels;
			maplistLabels = (Map<String, Object>) maplistRules.get("labels");

			Map<String, Object> maplistAnnotations;
			maplistAnnotations = (Map<String, Object>) maplistRules.get("annotations");

			rule.setId(count);
			rule.setType(maplistRules.get("alert").toString());
			rule.setSeverity(maplistLabels.get("severity").toString());

			if ("NodeDown".equals(rule.getType())) {
				rule.setCondition("");
				rule.setValue1("");
				rule.setValue2("");

				rule.setValue(maplistRules.get("expr").toString());

			} else if ("ApiserverDown".equals(rule.getType())) {
				rule.setCondition("");
				rule.setValue1("");
				rule.setValue2("");

				rule.setValue(maplistRules.get("expr").toString());

			} else if ("K8SNodeNotReady".equals(rule.getType())) {
				rule.setCondition("");
				rule.setValue1("");
				rule.setValue2("");

				rule.setValue(maplistRules.get("expr").toString());

			} else if ("PodFrequentlyRestarting".equals(rule.getType())) {
				rule.setCondition("");
				rule.setValue1("");
				rule.setValue2("");

				String expr = maplistRules.get("expr").toString();
				int idx = expr.indexOf('\"');
				String exprSub = expr.substring(idx + 1);
				int lastIdx = exprSub.lastIndexOf(".");
				String pod = exprSub.substring(0, lastIdx);

				rule.setValue(pod);

			} else {
				if (maplistRules.get("expr").toString().indexOf(">") >= 0) {
					condition = maplistRules.get("expr").toString().substring(
							maplistRules.get("expr").toString().indexOf(">"),
							maplistRules.get("expr").toString().indexOf(">") + 1);
				} else {
					condition = maplistRules.get("expr").toString().substring(
							maplistRules.get("expr").toString().indexOf("<"),
							maplistRules.get("expr").toString().indexOf("<") + 1);
				}

				rule.setCondition(condition);
				String[] gb = maplistRules.get("expr").toString().split(">|<");

				rule.setValue1(gb[0]);
				rule.setValue2(gb[1]);
			}

			if (maplistRules.get("for") != null)
				rule.setDuration(maplistRules.get("for").toString());
			rule.setChannel(maplistLabels.get("channel").toString());

			ruleViewList.add(count, rule);
			count++;
		}

		ruleVo = ruleViewList.get(ruleId);

		return ruleVo;
	}

	public RuleVo createRule(RuleVo ruleVo) {
		RuleData ruleData = new RuleData();

		String ruleExpr = "";
		ruleExpr = ruleVo.getValue1() + " " + ruleVo.getCondition() + " " + ruleVo.getValue2();

		ruleData.setRuleAlert(ruleVo.getType());
		ruleData.setRuleExpr(ruleExpr);
		ruleData.setRuleFor(ruleVo.getDuration());
		ruleData.setRuleSeverity(ruleVo.getSeverity());
		ruleData.setRuleChannel(ruleVo.getChannel());

		RuleData ruleResult = kubeCoreManager.createRule(ruleData);

		if (ruleResult != null) {
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					StringBuffer response = new StringBuffer();

					try {
						String url = UriComponentsBuilder.fromUriString(baseUrl).path("/-/reload").build().toString();
						URL obj = new URL(url);
						URLConnection conn = obj.openConnection();

						conn.setDoOutput(true);
						OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

						wr.flush();

						BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						String inputLine;

						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			timer.schedule(task, 120000);
		}

		return ruleVo;
	}

	public Boolean isRuleExist(RuleVo ruleVo) {
		return null;
	}

	@SuppressWarnings("unused")
	public RuleVo updateRule(int ruleId, RuleVo ruleVo) {
		Boolean result = kubeCoreManager.deleteRule(ruleId);

		RuleData ruleData = new RuleData();

		String ruleExpr = "";
		ruleExpr = ruleVo.getValue1() + " " + ruleVo.getCondition() + " " + ruleVo.getValue2();

		ruleVo.getId();
		ruleData.setRuleAlert(ruleVo.getType());
		ruleData.setRuleExpr(ruleExpr);
		ruleData.setRuleFor(ruleVo.getDuration());
		ruleData.setRuleSeverity(ruleVo.getSeverity());
		ruleData.setRuleChannel(ruleVo.getChannel());

		RuleData ruleResult = kubeCoreManager.createRule(ruleData);

		if (ruleResult != null) {
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					StringBuffer response = new StringBuffer();

					try {
						String url = UriComponentsBuilder.fromUriString(baseUrl).path("/-/reload").build().toString();
						URL obj = new URL(url);
						URLConnection conn = obj.openConnection();

						conn.setDoOutput(true);
						OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

						wr.flush();

						BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						String inputLine;

						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			timer.schedule(task, 120000);
		}

		return ruleVo;
	}

	public Boolean deleteRule(int id) {
		Boolean result = kubeCoreManager.deleteRule(id);

		if (result != null) {
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					StringBuffer response = new StringBuffer();

					try {
						String url = UriComponentsBuilder.fromUriString(baseUrl).path("/-/reload").build().toString();
						URL obj = new URL(url);
						URLConnection conn = obj.openConnection();

						conn.setDoOutput(true);
						OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

						wr.flush();

						BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						String inputLine;

						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			timer.schedule(task, 120000);
		}

		return result;
	}

	public List<String> getNamespaceList() {
		List<String> returnList = new ArrayList<String>();
		V1NamespaceList namespaceList = kubeCoreManager.getNamespaceList();

		for (V1Namespace item : namespaceList.getItems()) {
			returnList.add(item.getMetadata().getName());
		}

		return returnList;
	}

}
