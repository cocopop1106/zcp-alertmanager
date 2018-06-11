package com.skcc.cloudz.zcp.alert.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skcc.cloudz.zcp.alert.dao.impl.RuleDaoImpl;
import com.skcc.cloudz.zcp.alert.service.RuleService;
import com.skcc.cloudz.zcp.alert.vo.RuleData;
import com.skcc.cloudz.zcp.alert.vo.RuleVo;

@Service("ruleService")
public class RuleServiceImpl implements RuleService {
	
	private static Logger logger = Logger.getLogger(RuleServiceImpl.class);
	
	@Autowired
	RuleDaoImpl ruleDao;

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.service.RuleService#getRuleListService()
	 */
	@Override
	public List<RuleVo> getRuleListService() {
		// TODO Auto-generated method stub
		
		List listRules = ruleDao.getRuleListDao();
		
		List<RuleVo> ruleViewList = new ArrayList<RuleVo>();
		Map<String, Object> maplistRules;
		int count = 0;
		String condition = "";
		
		Iterator iteratorRules = listRules.iterator();
		
		while (iteratorRules.hasNext()) {
		    maplistRules = (Map) iteratorRules.next();
		    
		    RuleVo rule = new RuleVo();
		    
		    Map<String, Object> maplistLabels;
		    maplistLabels = (Map<String, Object>) maplistRules.get("labels");
		    
		    Map<String, Object> maplistAnnotations;
		    maplistAnnotations = (Map<String, Object>) maplistRules.get("annotations");
		    
		    rule.setId(count);
		    rule.setType(maplistRules.get("alert").toString());
			System.out.println(rule.getType());
			rule.setSeverity(maplistLabels.get("severity").toString());
			
			if(maplistRules.get("expr").toString().indexOf(">") >= 0) {
				condition = maplistRules.get("expr").toString().substring(maplistRules.get("expr").toString().indexOf(">"), maplistRules.get("expr").toString().indexOf(">")+1);
			} else {
				condition = maplistRules.get("expr").toString().substring(maplistRules.get("expr").toString().indexOf("<"), maplistRules.get("expr").toString().indexOf("<")+1);
			}
			
			rule.setCondition(condition);
			String[] gb = maplistRules.get("expr").toString().split(">|<");

			rule.setValue1(gb[0]);
			rule.setValue2(gb[1]);
			rule.setDuration(maplistRules.get("for").toString());
			rule.setChannel(maplistLabels.get("channel").toString());
			
			ruleViewList.add(count, rule);
			count++;
		}
		
		return ruleViewList;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.service.RuleService#findById(java.lang.Long)
	 */
	@Override
	public RuleVo findById(int ruleId) {
		// TODO Auto-generated method stub
		
		List listRules = ruleDao.getRuleListDao();
		
		List<RuleVo> ruleViewList = new ArrayList<RuleVo>();
		Map<String, Object> maplistRules;
		int count = 0;
		String condition = "";
		RuleVo ruleVo = new RuleVo();
		
		Iterator iteratorRules = listRules.iterator();
		
		while (iteratorRules.hasNext()) {
			maplistRules = (Map) iteratorRules.next();
			
			RuleVo rule = new RuleVo();
		    
		    Map<String, Object> maplistLabels;
		    maplistLabels = (Map<String, Object>) maplistRules.get("labels");
		    
		    Map<String, Object> maplistAnnotations;
		    maplistAnnotations = (Map<String, Object>) maplistRules.get("annotations");
		    
		    rule.setId(count);
		    rule.setType(maplistRules.get("alert").toString());
		    rule.setSeverity(maplistLabels.get("severity").toString());
			
			if(maplistRules.get("expr").toString().indexOf(">") >= 0) {
				condition = maplistRules.get("expr").toString().substring(maplistRules.get("expr").toString().indexOf(">"), maplistRules.get("expr").toString().indexOf(">")+1);
			} else {
				condition = maplistRules.get("expr").toString().substring(maplistRules.get("expr").toString().indexOf("<"), maplistRules.get("expr").toString().indexOf("<")+1);
			}
			
			rule.setCondition(condition);
			String[] gb = maplistRules.get("expr").toString().split(">|<");

			rule.setValue1(gb[0]);
			rule.setValue2(gb[1]);
			rule.setDuration(maplistRules.get("for").toString());
			rule.setChannel(maplistLabels.get("channel").toString());
			
			ruleViewList.add(count, rule);
			count++;
		}
		
		ruleVo = ruleViewList.get(ruleId);
		
		return ruleVo;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.service.RuleService#createRule(com.skcc.cloudz.zcp.alert.vo.RuleVo)
	 */
	@Override
	public RuleVo createRule(RuleVo ruleVo) {
		// TODO Auto-generated method stub
		
		RuleData ruleData = new RuleData();
		
		String ruleExpr = "";
		ruleExpr = ruleVo.getValue1() + " " + ruleVo.getCondition() + " " + ruleVo.getValue2();
			
		ruleData.setRuleAlert(ruleVo.getType());
		ruleData.setRuleExpr(ruleExpr);
		ruleData.setRuleFor(ruleVo.getDuration());
		ruleData.setRuleSeverity(ruleVo.getSeverity());
		ruleData.setRuleChannel(ruleVo.getChannel());
		
		RuleData ruleResult = ruleDao.createRule(ruleData);
		
		return ruleVo;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.service.RuleService#isRuleExist(com.skcc.cloudz.zcp.alert.vo.RuleVo)
	 */
	@Override
	public Boolean isRuleExist(RuleVo ruleVo) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.service.RuleService#updateRule(java.lang.Long, com.skcc.cloudz.zcp.alert.vo.RuleVo)
	 */
	@Override
	public RuleVo updateRule(int ruleId, RuleVo ruleVo) {
		// TODO Auto-generated method stub
		System.out.println(ruleVo);
		
		Boolean result = ruleDao.deleteRule(ruleId);
		
		RuleData ruleData = new RuleData();
		
		String ruleExpr = "";
		ruleExpr = ruleVo.getValue1() + " " + ruleVo.getCondition() + " " + ruleVo.getValue2();
			
		ruleVo.getId();
		ruleData.setRuleAlert(ruleVo.getType());
		ruleData.setRuleExpr(ruleExpr);
		ruleData.setRuleFor(ruleVo.getDuration());
		ruleData.setRuleSeverity(ruleVo.getSeverity());
		ruleData.setRuleChannel(ruleVo.getChannel());
		
		RuleData ruleResult = ruleDao.createRule(ruleData);
		
		RuleVo resultVo = new RuleVo();
		
		resultVo.setId(ruleVo.getId());
		resultVo.setType(ruleResult.getRuleAlert());
		resultVo.setSeverity(ruleResult.getRuleSeverity());
		
		String condition = "";
		
		if(ruleResult.getRuleExpr().toString().indexOf(">") >= 0) {
			condition = ruleResult.getRuleExpr().toString().substring(ruleResult.getRuleExpr().toString().indexOf(">"), ruleResult.getRuleExpr().toString().indexOf(">")+1);
		} else {
			condition = ruleResult.getRuleExpr().toString().substring(ruleResult.getRuleExpr().toString().indexOf("<"), ruleResult.getRuleExpr().toString().indexOf("<")+1);
		}
		
		resultVo.setCondition(condition);
		String[] gb = ruleResult.getRuleExpr().toString().split(">|<");

		resultVo.setValue1(gb[0]);
		resultVo.setValue2(gb[1]);
		
		resultVo.setDuration(ruleResult.getRuleFor());
		resultVo.setChannel(ruleResult.getRuleChannel());
		
		System.out.println(resultVo);
		
		return resultVo;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.service.RuleService#deleteRule(java.lang.Long)
	 */
	@Override
	public Boolean deleteRule(int id) {
		// TODO Auto-generated method stub
		
		Boolean result = ruleDao.deleteRule(id);
		
		return result;
	}
	

}
