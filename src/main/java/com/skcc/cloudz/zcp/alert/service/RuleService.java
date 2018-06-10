package com.skcc.cloudz.zcp.alert.service;

import java.util.List;

import com.skcc.cloudz.zcp.alert.vo.RuleVo;

public interface RuleService {
	/**
	 * 
	 * @param ruleId
	 * @return
	 */
	RuleVo findById(int ruleId);
	
	/**
	 * 
	 * @return
	 */
	List<RuleVo> getRuleListService();
	
	/**
	 * 
	 * @param ruleVo
	 * @return
	 */
	RuleVo createRule(RuleVo ruleVo);
	
	/**
	 * 
	 * @param ruleVo
	 * @return
	 */
	Boolean isRuleExist(RuleVo ruleVo);
	
	/**
	 * 
	 * @param id
	 * @param ruleVo
	 * @return
	 */
	RuleVo updateRule(int id, RuleVo ruleVo);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	Boolean deleteRule(int id);
}
