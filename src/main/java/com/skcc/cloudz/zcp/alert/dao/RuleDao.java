package com.skcc.cloudz.zcp.alert.dao;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.skcc.cloudz.zcp.alert.vo.RuleData;
import com.skcc.cloudz.zcp.alert.vo.RuleVo;

@Repository("ruleDao")
public interface RuleDao {
	
	/**
	 * 
	 * @return
	 */
	List<RuleData> getRuleListDao();
	
	/**
	 * 
	 * @param rule
	 * @return
	 */
	RuleData createRule(RuleData rule);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	Boolean deleteRule(int id);
	
}
