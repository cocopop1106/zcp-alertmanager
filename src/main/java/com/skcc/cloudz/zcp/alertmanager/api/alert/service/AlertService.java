package com.skcc.cloudz.zcp.alertmanager.api.alert.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skcc.cloudz.zcp.alertmanager.common.exception.ZcpException;
import com.skcc.cloudz.zcp.alertmanager.common.util.TimestampUtil;
import com.skcc.cloudz.zcp.alertmanager.common.vo.AlertCountVo;
import com.skcc.cloudz.zcp.alertmanager.common.vo.AlertHistoryVo;
import com.skcc.cloudz.zcp.alertmanager.common.vo.AlertVo;
import com.skcc.cloudz.zcp.alertmanager.common.vo.ApiServerVo;
import com.skcc.cloudz.zcp.alertmanager.common.vo.NodeDownVo;
import com.skcc.cloudz.zcp.alertmanager.common.vo.NodeNotReadyVo;

import com.skcc.cloudz.zcp.alertmanager.manager.AlertManager;
import com.skcc.cloudz.zcp.alertmanager.manager.MariaManager;
import com.skcc.cloudz.zcp.alertmanager.manager.PrometheusManager;

@Service
public class AlertService {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AlertService.class);

	@Autowired
	private AlertManager alertManager;

	@Autowired
	private PrometheusManager prometheusManager;

	@Autowired
	private MariaManager mariaManager;

	public AlertCountVo getAlertCount() throws ZcpException {
		JSONObject resultObj = new JSONObject();
		try {
			resultObj = prometheusManager.getAlertCount();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZcpException("10001", "getAlertCount exception: " + e.getMessage());
		}
		AlertCountVo alertCountVo = new AlertCountVo();

		if (resultObj != null) {
			JSONObject dataObj = (JSONObject) resultObj.get("data");
			JSONArray resultArr = (JSONArray) dataObj.get("result");

			for (int i = 0; i < resultArr.size(); i++) {
				JSONObject tempObj = (JSONObject) resultArr.get(i);
				JSONArray valueArr = (JSONArray) tempObj.get("value");

				alertCountVo.setCount(valueArr.get(1).toString());
			}
		}
		return alertCountVo;
	}

	public ApiServerVo getApiServer() throws ZcpException {
		JSONObject resultObj = new JSONObject();
		try {
			resultObj = prometheusManager.getApiServer();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZcpException("10002", "getApiServer exception: " + e.getMessage());
		}

		ApiServerVo apiServerVo = new ApiServerVo();
		if (resultObj != null) {
			JSONObject dataObj = (JSONObject) resultObj.get("data");
			JSONArray resultArr = (JSONArray) dataObj.get("result");

			for (int i = 0; i < resultArr.size(); i++) {
				JSONObject tempObj = (JSONObject) resultArr.get(i);
				JSONArray valueArr = (JSONArray) tempObj.get("value");

				apiServerVo.setStatus(valueArr.get(1).toString());
				if ("1".equals(valueArr.get(1).toString())) {
					apiServerVo.setStatus("OK");
				} else {
					apiServerVo.setStatus("Downed");
				}
			}
			if(resultArr.size() == 0) {
				apiServerVo.setStatus("OK");
			}
		}
		return apiServerVo;
	}

	public NodeNotReadyVo getNodeNotReady() throws ZcpException {
		NodeNotReadyVo nodeNotReadyVo = new NodeNotReadyVo();

		JSONObject resultObj = new JSONObject();
		try {
			resultObj = prometheusManager.getNodeNotReadyCnt();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZcpException("10003", "getNodeNotReady exception: " + e.getMessage());
		}

		JSONObject resultTotObj = new JSONObject();
		try {
			resultTotObj = prometheusManager.getNodeNotReadyTotCnt();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZcpException("10004", "getNodeNotReadyTotCnt exception: " + e.getMessage());
		}

		if (resultObj != null) {
			JSONObject dataObj = (JSONObject) resultObj.get("data");
			JSONArray resultArr = (JSONArray) dataObj.get("result");

			if (resultArr.size() == 0) {
				nodeNotReadyVo.setCount("0");
			} else {

				for (int i = 0; i < resultArr.size(); i++) {
					JSONObject tempObj = (JSONObject) resultArr.get(i);
					JSONArray valueArr = (JSONArray) tempObj.get("value");

					nodeNotReadyVo.setCount(valueArr.get(1).toString());
				}
			}
		}

		if (resultTotObj != null) {
			JSONObject dataTotObj = (JSONObject) resultTotObj.get("data");
			JSONArray resultTotArr = (JSONArray) dataTotObj.get("result");
			if (resultTotArr.size() == 0) {
				nodeNotReadyVo.setTotalCount("0");
			} else {

				for (int i = 0; i < resultTotArr.size(); i++) {
					JSONObject tempTotObj = (JSONObject) resultTotArr.get(i);
					JSONArray valueTotArr = (JSONArray) tempTotObj.get("value");

					nodeNotReadyVo.setTotalCount(valueTotArr.get(1).toString());
				}
			}
		}
		return nodeNotReadyVo;
	}

	public NodeDownVo getNodeDown() throws ZcpException {
		NodeDownVo nodeDownVo = new NodeDownVo();

		JSONObject resultObj = new JSONObject();
		try {
			resultObj = prometheusManager.getNodeDownCnt();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZcpException("10005", "getNodeDownCnt exception: " + e.getMessage());
		}

		JSONObject resultTotObj = new JSONObject();
		try {
			resultTotObj = prometheusManager.getNodeDownTotCnt();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZcpException("10006", "getNodeDownTotCnt exception: " + e.getMessage());
		}

		if (resultObj != null) {
			JSONObject dataObj = (JSONObject) resultObj.get("data");
			JSONArray resultArr = (JSONArray) dataObj.get("result");

			if (resultArr.size() == 0) {
				nodeDownVo.setCount("0");
			} else {

				for (int i = 0; i < resultArr.size(); i++) {
					JSONObject tempObj = (JSONObject) resultArr.get(i);
					JSONArray valueArr = (JSONArray) tempObj.get("value");

					nodeDownVo.setCount(valueArr.get(1).toString());
				}
			}
		}

		if (resultTotObj != null) {
			JSONObject dataTotObj = (JSONObject) resultTotObj.get("data");
			JSONArray resultTotArr = (JSONArray) dataTotObj.get("result");
			if (resultTotArr.size() == 0) {
				nodeDownVo.setTotalCount("0");
			} else {

				for (int i = 0; i < resultTotArr.size(); i++) {
					JSONObject tempTotObj = (JSONObject) resultTotArr.get(i);
					JSONArray valueTotArr = (JSONArray) tempTotObj.get("value");

					nodeDownVo.setTotalCount(valueTotArr.get(1).toString());
				}
			}
		}

		return nodeDownVo;
	}

	public List<AlertVo> getAlertList() throws ZcpException {
		List<AlertVo> resultList = new ArrayList<AlertVo>();

		JSONObject resultObj = new JSONObject();
		try {
			resultObj = alertManager.getAlertList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZcpException("10007", "getAlertList exception: " + e.getMessage());
		}

		if (resultObj != null) {
			JSONArray resultArr = (JSONArray) resultObj.get("data");

			for (int i = 0; i < resultArr.size(); i++) {
				JSONObject alertObj = (JSONObject) resultArr.get(i);
				AlertVo alertVo = new AlertVo();

				JSONObject labelsObj = (JSONObject) alertObj.get("labels");

				if (!"sk-cps-ops".equals(labelsObj.get("channel")) && !"default".equals(labelsObj.get("channel"))
						&& !"zcp-webhook".equals(labelsObj.get("channel"))) {

					if (alertObj.get("startsAt") != null) {
						Object startsAt = TimestampUtil.ISO8601ToDate(alertObj.get("startsAt"));
						alertVo.setTime(startsAt.toString());
					}

					if (labelsObj.get("severity") != null) {
						alertVo.setSeverity(labelsObj.get("severity").toString());
					}

					if (labelsObj.get("alertname") != null) {
						alertVo.setType(labelsObj.get("alertname").toString());
					}

					if (labelsObj.get("channel") != null) {
						alertVo.setReceiver(labelsObj.get("channel").toString());
					}

					JSONObject annotationsObj = (JSONObject) alertObj.get("annotations");
					if (annotationsObj.get("description") != null) {
						alertVo.setDescription(annotationsObj.get("description").toString());
					}

					resultList.add(alertVo);
				}
			}
		}
		return resultList;
	}

	public List<AlertHistoryVo> getAlertHistoryList(String time) throws ZcpException {
		List<AlertHistoryVo> resultList = new ArrayList<AlertHistoryVo>();

		JSONArray resultArr = new JSONArray();
		try {
			resultArr = mariaManager.getAlertHistoryList(time);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZcpException("10008", "getAlertHistoryList exception: " + e.getMessage());
		}

		if (resultArr != null) {
			for (int resultCnt = 0; resultCnt < resultArr.size(); resultCnt++) {

				JSONArray alertArr = (JSONArray) resultArr.get(resultCnt);
				for (int alertCnt = 0; alertCnt < alertArr.size(); alertCnt++) {
					JSONObject alertObj = (JSONObject) alertArr.get(alertCnt);
					AlertHistoryVo alertHistoryVo = new AlertHistoryVo();

					if (alertObj.get("status") != null) {
						alertHistoryVo.setStatus(alertObj.get("status").toString());
					}

					if (alertObj.get("startsAt") != null) {
						Object startsAt = TimestampUtil.ISO8601ToDate(alertObj.get("startsAt"));
						alertHistoryVo.setTime(startsAt.toString());
					}

					JSONObject labelsObj = (JSONObject) alertObj.get("labels");
					if (labelsObj.get("severity") != null) {
						alertHistoryVo.setSeverity(labelsObj.get("severity").toString());
					}

					if (labelsObj.get("alertname") != null) {
						alertHistoryVo.setType(labelsObj.get("alertname").toString());
					}

					if (labelsObj.get("channel") != null) {
						alertHistoryVo.setReceiver(labelsObj.get("channel").toString());
					}

					JSONObject annotationsObj = (JSONObject) alertObj.get("annotations");
					if (annotationsObj.get("description") != null) {
						alertHistoryVo.setDescription(annotationsObj.get("description").toString());
					}
					resultList.add(alertHistoryVo);
				}
			}
		}
		return resultList;
	}

}
