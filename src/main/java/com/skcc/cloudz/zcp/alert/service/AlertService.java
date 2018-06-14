package com.skcc.cloudz.zcp.alert.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skcc.cloudz.zcp.alert.vo.AlertCountVo;
import com.skcc.cloudz.zcp.alert.vo.AlertVo;
import com.skcc.cloudz.zcp.alert.vo.ApiServerVo;
import com.skcc.cloudz.zcp.alert.vo.NodeDownVo;
import com.skcc.cloudz.zcp.alert.vo.NodeNotReadyVo;
import com.skcc.cloudz.zcp.manager.AlertManager;
import com.skcc.cloudz.zcp.manager.PrometheusManager;

@Service
public class AlertService {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AlertService.class);

	@Autowired
	private AlertManager alertManager;

	@Autowired
	private PrometheusManager prometheusManager;


	public AlertCountVo getAlertCount() {
		JSONObject resultObj = prometheusManager.getAlertCount();
		AlertCountVo alertCountVo = new AlertCountVo();

		if(resultObj != null) {
			JSONObject dataObj = (JSONObject) resultObj.get("data");
			JSONArray resultArr = (JSONArray) dataObj.get("result");

			for(int i=0 ; i<resultArr.size() ; i++) {
				JSONObject tempObj = (JSONObject) resultArr.get(i);
				JSONArray valueArr = (JSONArray) tempObj.get("value");

				alertCountVo.setCount(valueArr.get(1).toString());
			}
		}
		return alertCountVo;
	}

	public ApiServerVo getApiServer() {
		JSONObject resultObj = prometheusManager.getApiServer();
		ApiServerVo apiServerVo = new ApiServerVo();

		if(resultObj != null) {
			JSONObject dataObj = (JSONObject) resultObj.get("data");
			JSONArray resultArr = (JSONArray) dataObj.get("result");

			for(int i=0 ; i<resultArr.size() ; i++) {
				JSONObject tempObj = (JSONObject) resultArr.get(i);
				JSONArray valueArr = (JSONArray) tempObj.get("value");

				apiServerVo.setStatus(valueArr.get(1).toString());
				if("1".equals(valueArr.get(1).toString())) {
					apiServerVo.setStatus("normal");
				} else {
					apiServerVo.setStatus("down");
				}
			}
		}
		return apiServerVo;
	}

	public NodeNotReadyVo getNodeNotReady() {
		NodeNotReadyVo nodeNotReadyVo = new NodeNotReadyVo();

		JSONObject resultObj = prometheusManager.getNodeNotReadyCnt();
		JSONObject resultTotObj = prometheusManager.getNodeNotReadyTotCnt();

		if(resultObj != null) {
			JSONObject dataObj = (JSONObject) resultObj.get("data");
			JSONArray resultArr = (JSONArray) dataObj.get("result");

			if(resultArr.size() == 0) {
				nodeNotReadyVo.setCount("0");
			} else {

				for(int i=0 ; i<resultArr.size() ; i++) {
					JSONObject tempObj = (JSONObject) resultArr.get(i);
					JSONArray valueArr = (JSONArray) tempObj.get("value");

					nodeNotReadyVo.setCount(valueArr.get(1).toString());
				}	
			}
		}

		if(resultTotObj != null) {
			JSONObject dataTotObj = (JSONObject) resultTotObj.get("data");
			JSONArray resultTotArr = (JSONArray) dataTotObj.get("result");
			if(resultTotArr.size() == 0) {
				nodeNotReadyVo.setTotalCount("0");
			} else {

				for(int i=0 ; i<resultTotArr.size() ; i++) {
					JSONObject tempTotObj = (JSONObject) resultTotArr.get(i);
					JSONArray valueTotArr = (JSONArray) tempTotObj.get("value");

					nodeNotReadyVo.setTotalCount(valueTotArr.get(1).toString());
				}	
			}
		}
		return nodeNotReadyVo;
	}

	public NodeDownVo getNodeDown() {
		NodeDownVo nodeDownVo = new NodeDownVo();

		JSONObject resultObj = prometheusManager.getNodeDownCnt();
		JSONObject resultTotObj = prometheusManager.getNodeDownTotCnt();

		if(resultObj != null) {
			JSONObject dataObj = (JSONObject) resultObj.get("data");
			JSONArray resultArr = (JSONArray) dataObj.get("result");

			if(resultArr.size() == 0) {
				nodeDownVo.setCount("0");
			} else {

				for(int i=0 ; i<resultArr.size() ; i++) {
					JSONObject tempObj = (JSONObject) resultArr.get(i);
					JSONArray valueArr = (JSONArray) tempObj.get("value");

					nodeDownVo.setCount(valueArr.get(1).toString());
				}	
			}
		}

		if(resultTotObj != null) {
			JSONObject dataTotObj = (JSONObject) resultTotObj.get("data");
			JSONArray resultTotArr = (JSONArray) dataTotObj.get("result");
			if(resultTotArr.size() == 0) {
				nodeDownVo.setTotalCount("0");
			} else {

				for(int i=0 ; i<resultTotArr.size() ; i++) {
					JSONObject tempTotObj = (JSONObject) resultTotArr.get(i);
					JSONArray valueTotArr = (JSONArray) tempTotObj.get("value");

					nodeDownVo.setTotalCount(valueTotArr.get(1).toString());
				}	
			}
		}

		return nodeDownVo;
	}

	public List<AlertVo> getAlertList() {
		List<AlertVo> resultList = new ArrayList<AlertVo>();
		JSONObject resultObj = alertManager.getAlertList();

		if(resultObj != null) {
			JSONArray resultArr = (JSONArray) resultObj.get("data");

			for(int i=0 ; i<resultArr.size() ; i++) {
				JSONObject alertObj = (JSONObject) resultArr.get(i);
				AlertVo alertVo = new AlertVo();

				if(alertObj.get("startsAt") != null) {
					alertVo.setTime(alertObj.get("startsAt").toString());	
				}

				JSONObject labelsObj = (JSONObject) alertObj.get("labels");
				if(labelsObj.get("severity") != null) {
					alertVo.setSeverity(labelsObj.get("severity").toString());	
				}

				if(labelsObj.get("alertname") != null) {
					alertVo.setType(labelsObj.get("alertname").toString());	
				}

				if(labelsObj.get("channel") != null) {
					alertVo.setReceiver(labelsObj.get("channel").toString());	
				}

				JSONObject annotationsObj = (JSONObject) alertObj.get("annotations");
				if(annotationsObj.get("description") != null) {
					alertVo.setDescription(annotationsObj.get("description").toString());	
				}

				resultList.add(alertVo);
			}
		}
		return resultList;
	}

}
