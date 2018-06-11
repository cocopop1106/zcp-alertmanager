package com.skcc.cloudz.zcp.alert.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.skcc.cloudz.zcp.alert.vo.AlertCountData;
import com.skcc.cloudz.zcp.alert.vo.AlertData;

@Repository("alertDao")
public interface AlertDao {
	
	/**
	 * 
	 * @return
	 */
	AlertCountData getAlertCount();

	/**
	 * 
	 * @return
	 */
	List<AlertData> getAlertListDao();
}
