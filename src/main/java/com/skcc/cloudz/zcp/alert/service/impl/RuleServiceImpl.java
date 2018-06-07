package com.skcc.cloudz.zcp.alert.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skcc.cloudz.zcp.alert.dao.impl.RuleDaoImpl;
import com.skcc.cloudz.zcp.alert.service.RuleService;
import com.skcc.cloudz.zcp.alert.vo.RuleDto;
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
		
		List<RuleDto> ruleList = ruleDao.getRuleListDao();
		List<RuleVo> ruleViewList = new ArrayList<RuleVo>();
		String condition = "";
		
		for(int i=0; i<ruleList.size(); i++) {
			RuleVo rule = new RuleVo();
			
			rule.setId(i);
			rule.setType(ruleList.get(i).getRuleDataAlert());
			rule.setSeverity(ruleList.get(i).getRuleDataSeverity());
			
			if(ruleList.get(i).getRuleDataExpr().indexOf(">") >= 0) {
				condition = ruleList.get(i).getRuleDataExpr().substring(ruleList.get(i).getRuleDataExpr().indexOf(">"), ruleList.get(i).getRuleDataExpr().indexOf(">")+1);
			} else {
				condition = ruleList.get(i).getRuleDataExpr().substring(ruleList.get(i).getRuleDataExpr().indexOf("<"), ruleList.get(i).getRuleDataExpr().indexOf("<")+1);
			}
			
			rule.setCondition(condition);
			
			String[] gb =  ruleList.get(i).getRuleDataExpr().split(">|<");

			rule.setValue1(gb[0]);
			rule.setValue2(gb[1]);
			rule.setDuration(ruleList.get(i).getRuleDataFor());
			rule.setChannel(ruleList.get(i).getRuleDataChannel());
			
			ruleViewList.add(rule);
		}
		
		return ruleViewList;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.service.RuleService#findById(java.lang.Long)
	 */
	@Override
	public RuleVo findById(Long ruleId) {
		// TODO Auto-generated method stub
		
		List<RuleDto> ruleList = ruleDao.getRuleListDao();
		List<RuleVo> ruleViewList = new ArrayList<RuleVo>();
		String condition = "";
		RuleVo rule = new RuleVo();
		
		for(int i=0; i<ruleList.size(); i++) {
			if(ruleId == i) {
				rule.setId(i);
				rule.setType(ruleList.get(i).getRuleDataAlert());
				rule.setSeverity(ruleList.get(i).getRuleDataSeverity());
				
				if(ruleList.get(i).getRuleDataExpr().indexOf(">") >= 0) {
					condition = ruleList.get(i).getRuleDataExpr().substring(ruleList.get(i).getRuleDataExpr().indexOf(">"), ruleList.get(i).getRuleDataExpr().indexOf(">")+1);
				} else {
					condition = ruleList.get(i).getRuleDataExpr().substring(ruleList.get(i).getRuleDataExpr().indexOf("<"), ruleList.get(i).getRuleDataExpr().indexOf("<")+1);
				}
				
				rule.setCondition(condition);
				
				String[] gb =  ruleList.get(i).getRuleDataExpr().split(">|<");

				rule.setValue1(gb[0]);
				rule.setValue2(gb[1]);
				rule.setDuration(ruleList.get(i).getRuleDataFor());
				rule.setChannel(ruleList.get(i).getRuleDataChannel());
			}
		}
		return rule;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.service.RuleService#createRule(com.skcc.cloudz.zcp.alert.vo.RuleVo)
	 */
	@Override
	public RuleVo createRule(RuleVo ruleVo) {
		// TODO Auto-generated method stub
		
		RuleDto ruleDto = new RuleDto();
		
		String ruleDataExpr = "";
		ruleDataExpr = ruleVo.getValue1() + " " + ruleVo.getCondition() + " " + ruleVo.getValue2();
			
		ruleDto.setRuleDataAlert(ruleVo.getType());
		ruleDto.setRuleDataExpr(ruleDataExpr);
		ruleDto.setRuleDataFor(ruleVo.getDuration());
		ruleDto.setRuleDataSeverity(ruleVo.getSeverity());
		ruleDto.setRuleDataChannel(ruleVo.getChannel());
		
		RuleDto ruleResult = ruleDao.createRule(ruleDto);
		
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
	public RuleVo updateRule(Long id, RuleVo ruleVo) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.service.RuleService#deleteRule(java.lang.Long)
	 */
	@Override
	public Boolean deleteRule(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
