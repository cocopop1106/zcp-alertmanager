package com.skcc.cloudz.zcp.alert.controller;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import com.skcc.cloudz.zcp.alert.service.impl.RuleServiceImpl;
import com.skcc.cloudz.zcp.alert.vo.RuleDto;
import com.skcc.cloudz.zcp.alert.vo.RuleVo;

import ch.qos.logback.core.net.SyslogOutputStream;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1ConfigMap;
import io.kubernetes.client.util.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@RestController
public class RuleController {
	private static Logger logger = Logger.getLogger(RuleController.class);
	
	@Autowired
	RuleServiceImpl ruleService;

	/**
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "rule", method = RequestMethod.GET)
	public ResponseEntity<List<RuleVo>> getRule() throws IOException {
		
		List<RuleVo> ruleList = ruleService.getRuleListService();
		if (ruleList.isEmpty()) {
			return new ResponseEntity<List<RuleVo>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<RuleVo>>(ruleList, HttpStatus.OK);
	}
	
	/**
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "rule/{id}", method = RequestMethod.GET)
	public ResponseEntity<RuleVo> getRuleDtl(@PathVariable("id") final Long id) {
		RuleVo ruleDtl = ruleService.findById(id);
		if (ruleDtl == null) {
			return new ResponseEntity<RuleVo>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RuleVo>(ruleDtl, HttpStatus.OK);
	}
	
	/**
	 * @param ruleVo
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(value = "rule", method = RequestMethod.POST)
	public ResponseEntity<Void> createRule(@RequestBody final RuleVo ruleVo, final UriComponentsBuilder ucBuilder) {
		RuleVo createRule = ruleService.createRule(ruleVo);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("rule/{id}").buildAndExpand(createRule.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	/**
	 * @param id
	 * @param ruleVo
	 * @return
	 */
	@RequestMapping(value = "rule/{id}", method = RequestMethod.PUT)
	public ResponseEntity<RuleVo> updateRule(@PathVariable("id") final Long id, @RequestBody final RuleVo ruleVo) {
//		RuleVo updateRule = ruleService.updateRule(id, ruleVo);
		
//		if (updatedRule == null) {
//			return new ResponseEntity<RuleVo>(HttpStatus.NOT_FOUND);
//		}
		
//		return new ResponseEntity<Customer>(updatedRule, HttpStatus.OK);
		return new ResponseEntity<RuleVo>(HttpStatus.OK);
	}
	
	/**
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "rule/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteRule(@PathVariable("id") final Long id) {
//		Boolean deleteResult = customerService.deleteCustomer(id);
		
//		if (deleteResult == null || !deleteResult) {
//			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
//		}
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
}
