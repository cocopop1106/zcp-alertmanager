package com.skcc.cloudz.zcp.alert.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skcc.cloudz.zcp.alert.dao.impl.AlertDaoImpl;
import com.skcc.cloudz.zcp.alert.service.AlertService;
import com.skcc.cloudz.zcp.alert.vo.AlertCountData;
import com.skcc.cloudz.zcp.alert.vo.AlertCountVo;
import com.skcc.cloudz.zcp.alert.vo.AlertVo;
import com.skcc.cloudz.zcp.alert.vo.ApiServerVo;
import com.skcc.cloudz.zcp.alert.vo.NodeDownVo;
import com.skcc.cloudz.zcp.alert.vo.NodeNotReadyVo;

@Service("alertService")
public class AlertServiceImpl implements AlertService {
	
	private static Logger logger = Logger.getLogger(AlertServiceImpl.class);
	
	@Autowired
	AlertDaoImpl alertDao;

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.service.AlertService#getAlertCount()
	 */
	@Override
	public AlertCountVo getAlertCount() {
		// TODO Auto-generated method stub
		
		JSONObject resultObj = alertDao.getAlertCount();
		AlertCountVo alertCountVo = new AlertCountVo();
		
		if(resultObj != null) {
//			alertCountVo.setAlertCount(result.getValue()[1]);	
		}
		
		return alertCountVo;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.service.AlertService#getApiServer()
	 */
	@Override
	public ApiServerVo getApiServer() {
		// TODO Auto-generated method stub
		
		JSONObject resultObj = alertDao.getApiServer();
		ApiServerVo apiServerVo = new ApiServerVo();
		
		if(resultObj != null) {
			JSONObject dataObj = (JSONObject) resultObj.get("data");
			JSONArray resultArr = (JSONArray) dataObj.get("result");

			for(int i=0 ; i<resultArr.size() ; i++) {
				JSONObject tempObj = (JSONObject) resultArr.get(i);
				JSONArray valueArr = (JSONArray) tempObj.get("value");
				
				apiServerVo.setResult(valueArr.get(1).toString());
			}
		}
		return apiServerVo;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.service.AlertService#getNodeNotReady()
	 */
	@Override
	public NodeNotReadyVo getNodeNotReady() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.service.AlertService#getNodeDown()
	 */
	@Override
	public NodeDownVo getNodeDown() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.service.AlertService#getAlertListDao()
	 */
	@Override
	public List<AlertVo> getAlertList() {
		// TODO Auto-generated method stub
		
		List<AlertVo> resultList = new ArrayList<AlertVo>();
		
		JSONObject resultObj = alertDao.getAlertList();
		System.out.println(resultObj);
		
		return resultList;
	}

	
}
