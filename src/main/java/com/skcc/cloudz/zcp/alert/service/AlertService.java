package com.skcc.cloudz.zcp.alert.service;

import java.util.List;

import com.skcc.cloudz.zcp.alert.vo.AlertCountVo;
import com.skcc.cloudz.zcp.alert.vo.AlertVo;
import com.skcc.cloudz.zcp.alert.vo.ApiServerVo;
import com.skcc.cloudz.zcp.alert.vo.NodeDownVo;
import com.skcc.cloudz.zcp.alert.vo.NodeNotReadyVo;

public interface AlertService {
	
	/**
	 * 
	 * @return
	 */
	AlertCountVo getAlertCount();
	
	/**
	 * 
	 * @return
	 */
	ApiServerVo getApiServer();
	
	/**
	 * 
	 * @return
	 */
	NodeNotReadyVo getNodeNotReady();
	
	/**
	 * 
	 * @return
	 */
	NodeDownVo getNodeDown();
	
	/**
	 * 
	 * @return
	 */
	List<AlertVo> getAlertList();

}
