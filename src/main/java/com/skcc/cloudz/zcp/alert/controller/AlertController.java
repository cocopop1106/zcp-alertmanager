package com.skcc.cloudz.zcp.alert.controller;

import java.io.IOException;
import java.util.ArrayList;
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

import com.skcc.cloudz.zcp.alert.service.impl.AlertServiceImpl;
import com.skcc.cloudz.zcp.alert.service.impl.RuleServiceImpl;
import com.skcc.cloudz.zcp.alert.vo.AlertCountVo;
import com.skcc.cloudz.zcp.alert.vo.AlertHistoryVo;
import com.skcc.cloudz.zcp.alert.vo.AlertVo;
import com.skcc.cloudz.zcp.alert.vo.ApiServerVo;
import com.skcc.cloudz.zcp.alert.vo.NodeDownVo;
import com.skcc.cloudz.zcp.alert.vo.NodeNotReadyVo;
import com.skcc.cloudz.zcp.alert.vo.RuleVo;

import io.swagger.annotations.Api;

@RestController
public class AlertController {
	private static Logger logger = Logger.getLogger(AlertController.class);
	
	@Autowired
	AlertServiceImpl alertService;
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "activeCount", method = RequestMethod.GET)
	public ResponseEntity<AlertCountVo> getAlertCount() throws IOException {
		AlertCountVo result = alertService.getAlertCount();
		if (result == null) {
			return new ResponseEntity<AlertCountVo>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<AlertCountVo>(result, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "apiServer", method = RequestMethod.GET)
	public ResponseEntity<ApiServerVo> getApiServer() throws IOException {
		ApiServerVo result = alertService.getApiServer();
		return new ResponseEntity<ApiServerVo>(result, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "nodeNotReady", method = RequestMethod.GET)
	public ResponseEntity<NodeNotReadyVo> getNodeNotReady() throws IOException {
		NodeNotReadyVo result = alertService.getNodeNotReady();
		return new ResponseEntity<NodeNotReadyVo>(result, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "nodeDown", method = RequestMethod.GET)
	public ResponseEntity<NodeDownVo> getNodeDown() throws IOException {
		NodeDownVo result = alertService.getNodeDown();
		return new ResponseEntity<NodeDownVo>(result, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "alertList", method = RequestMethod.GET)
	public ResponseEntity<List<AlertVo>> getAlertList() throws IOException {
		List<AlertVo> result = alertService.getAlertList();
		if (result == null) {
			return new ResponseEntity<List<AlertVo>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<AlertVo>>(result, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "alertHistory", method = RequestMethod.GET)
	public ResponseEntity<List<AlertHistoryVo>> getHistoryList() throws IOException {
		return new ResponseEntity<List<AlertHistoryVo>>(HttpStatus.OK);
	}
	
}
