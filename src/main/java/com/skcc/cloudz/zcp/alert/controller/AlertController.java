package com.skcc.cloudz.zcp.alert.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.skcc.cloudz.zcp.alert.vo.AlertHistoryVo;
import com.skcc.cloudz.zcp.alert.vo.AlertVo;

import io.swagger.annotations.Api;

@RestController
public class AlertController {
	private static Logger logger = Logger.getLogger(AlertController.class);
	
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
	
}
