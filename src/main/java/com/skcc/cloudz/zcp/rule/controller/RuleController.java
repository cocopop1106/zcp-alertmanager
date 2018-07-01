package com.skcc.cloudz.zcp.rule.controller;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.skcc.cloudz.zcp.rule.service.RuleService;
import com.skcc.cloudz.zcp.common.vo.RuleVo;

@RestController
public class RuleController {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(RuleController.class);

	@Autowired
	private RuleService ruleService;

	@RequestMapping(value = "rule", method = RequestMethod.GET)
	public ResponseEntity<List<RuleVo>> getRuleList() throws IOException {

		List<RuleVo> ruleList = ruleService.getRuleList();
		if (ruleList.isEmpty()) {
			return new ResponseEntity<List<RuleVo>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<RuleVo>>(ruleList, HttpStatus.OK);
	}

	@RequestMapping(value = "rule/{id}", method = RequestMethod.GET)
	public ResponseEntity<RuleVo> getRuleDtl(@PathVariable("id") final int id) {
		RuleVo ruleDtl = ruleService.findById(id);
		if (ruleDtl == null) {
			return new ResponseEntity<RuleVo>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RuleVo>(ruleDtl, HttpStatus.OK);
	}

	@RequestMapping(value = "rule", method = RequestMethod.POST)
	public ResponseEntity<Void> createRule(@RequestBody final RuleVo ruleVo, final UriComponentsBuilder ucBuilder) {
		RuleVo createRule = ruleService.createRule(ruleVo);

		HttpHeaders headers = new HttpHeaders();
//		headers.setLocation(ucBuilder.path("rule/{id}").buildAndExpand(createRule.getId()).toUri());
		headers.setLocation(ucBuilder.path("alert/rules").build().toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "rule/{id}", method = RequestMethod.PUT)
	public ResponseEntity<RuleVo> updateRule(@PathVariable("id") final int id, @RequestBody final RuleVo ruleVo) {
		RuleVo updateRule = ruleService.updateRule(id, ruleVo);

		if (updateRule == null) {
			return new ResponseEntity<RuleVo>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RuleVo>(updateRule, HttpStatus.OK);
	}

	@RequestMapping(value = "rule/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteRule(@PathVariable("id") final int id) {
		RuleVo ruleResult = ruleService.findById(id);

		if (ruleResult == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} else {
			ruleService.deleteRule(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "namespaceList", method = RequestMethod.GET)
	public ResponseEntity<List<String>> getNamespaceList() throws IOException {

		List<String> namespaceList = ruleService.getNamespaceList();
		if (namespaceList.isEmpty()) {
			return new ResponseEntity<List<String>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<String>>(namespaceList, HttpStatus.OK);
	}

}
