package com.skcc.cloudz.zcp.alert.service;

import java.io.IOException;
import java.util.List;

import com.skcc.cloudz.zcp.alert.vo.RuleDto;
import com.skcc.cloudz.zcp.alert.vo.RuleVo;

public interface RuleService {
	/**
	 * findById
	 * @param ruleId
	 * @return
	 */
	RuleVo findById(Long ruleId);
	
	/**
	 * getRuleListService
	 * @return
	 */
	List<RuleVo> getRuleListService();
	
	/**
	 * createRule
	 * @param ruleVo
	 * @return
	 */
	RuleVo createRule(RuleVo ruleVo);
	
	/**
	 * isRuleExist
	 * @param ruleVo
	 * @return
	 */
	Boolean isRuleExist(RuleVo ruleVo);
	
	/**
	 * updateRule
	 * @param id
	 * @param ruleVo
	 * @return
	 */
	RuleVo updateRule(Long id, RuleVo ruleVo);
	
	/**
	 * deleteRule
	 * @param id
	 * @return
	 */
	Boolean deleteRule(Long id);
}
