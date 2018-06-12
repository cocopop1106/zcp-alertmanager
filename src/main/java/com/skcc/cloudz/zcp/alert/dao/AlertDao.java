package com.skcc.cloudz.zcp.alert.dao;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;

import com.skcc.cloudz.zcp.alert.vo.AlertCountData;
import com.skcc.cloudz.zcp.alert.vo.AlertData;

@Repository("alertDao")
public interface AlertDao {
	
	/**
	 * 
	 * @return
	 */
	JSONObject getAlertCount();
	
	/**
	 * 
	 * @return
	 */
	JSONObject getApiServer();
	
	/**
	 * 
	 * @return
	 */
	JSONObject getNodeNotReadyCnt();
	
	/**
	 * 
	 * @return
	 */
	JSONObject getNodeNotReadyTotCnt();
	
	/**
	 * 
	 * @return
	 */
	JSONObject getNodeDownCnt();
	
	/**
	 * 
	 * @return
	 */
	JSONObject getNodeDownTotCnt();
	
	/**
	 * 
	 * @return
	 */
	JSONObject getAlertList();
}
