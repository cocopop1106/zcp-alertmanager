package com.skcc.cloudz.zcp.alert.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skcc.cloudz.zcp.alert.dao.impl.AlertDaoImpl;
import com.skcc.cloudz.zcp.alert.service.AlertService;
import com.skcc.cloudz.zcp.alert.vo.AlertCountData;
import com.skcc.cloudz.zcp.alert.vo.AlertCountVo;
import com.skcc.cloudz.zcp.alert.vo.AlertData;
import com.skcc.cloudz.zcp.alert.vo.AlertVo;

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
		
		AlertCountData result = alertDao.getAlertCount();
		result.getValue();
		
		AlertCountVo alertCountVo = new AlertCountVo();
		
		return alertCountVo;
	}

	
}
