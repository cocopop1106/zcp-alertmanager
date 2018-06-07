package com.skcc.cloudz.zcp.alert.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.skcc.cloudz.zcp.alert.dao.AlertDao;
import com.skcc.cloudz.zcp.alert.vo.AlertData;

@Repository("alertDao")
public class AlertDaoImpl implements AlertDao {

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.dao.AlertDao#getAlertListDao()
	 */
	@Override
	public List<AlertData> getAlertListDao() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
