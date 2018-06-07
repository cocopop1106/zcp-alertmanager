package com.skcc.cloudz.zcp.alert.dao;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.skcc.cloudz.zcp.alert.vo.RuleDto;
import com.skcc.cloudz.zcp.alert.vo.RuleVo;

@Repository("ruleDao")
public interface RuleDao {
	
	/**
	 * getRuleListDao
	 * @return
	 */
	List<RuleDto> getRuleListDao();
	
	/**
	 * createRule
	 * @param rule
	 * @return
	 */
	RuleDto createRule(RuleDto rule);
	
}
