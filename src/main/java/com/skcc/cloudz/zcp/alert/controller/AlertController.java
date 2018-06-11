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
	@RequestMapping(value = "alert", method = RequestMethod.GET)
	public ResponseEntity<List<AlertVo>> getAlertList() throws IOException {
		return new ResponseEntity<List<AlertVo>>(HttpStatus.OK);
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "history", method = RequestMethod.GET)
	public ResponseEntity<List<AlertHistoryVo>> getHistoryList() throws IOException {
		return new ResponseEntity<List<AlertHistoryVo>>(HttpStatus.OK);
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "active", method = RequestMethod.GET)
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
	@RequestMapping(value = "api", method = RequestMethod.GET)
	public ResponseEntity<List<AlertVo>> getApiServer() throws IOException {
		return new ResponseEntity<List<AlertVo>>(HttpStatus.OK);
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "ready", method = RequestMethod.GET)
	public ResponseEntity<List<AlertVo>> getNodeNotReady() throws IOException {
		return new ResponseEntity<List<AlertVo>>(HttpStatus.OK);
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "down", method = RequestMethod.GET)
	public ResponseEntity<List<AlertVo>> getNodeDown() throws IOException {
		return new ResponseEntity<List<AlertVo>>(HttpStatus.OK);
	}
	
}
