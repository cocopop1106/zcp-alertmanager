package com.skcc.cloudz.zcp.alert.controller;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.skcc.cloudz.zcp.alert.service.AlertService;
import com.skcc.cloudz.zcp.common.vo.AlertCountVo;
import com.skcc.cloudz.zcp.common.vo.AlertHistoryVo;
import com.skcc.cloudz.zcp.common.vo.AlertVo;
import com.skcc.cloudz.zcp.common.vo.ApiServerVo;
import com.skcc.cloudz.zcp.common.vo.NodeDownVo;
import com.skcc.cloudz.zcp.common.vo.NodeNotReadyVo;

@RestController
public class AlertController {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AlertController.class);

	@Autowired
	private AlertService alertService;

	@RequestMapping(value = "activeCount", method = RequestMethod.GET)
	public ResponseEntity<AlertCountVo> getAlertCount() throws IOException {
		AlertCountVo result = alertService.getAlertCount();
		if (result == null) {
			return new ResponseEntity<AlertCountVo>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<AlertCountVo>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "apiServer", method = RequestMethod.GET)
	public ResponseEntity<ApiServerVo> getApiServer() throws IOException {
		ApiServerVo result = alertService.getApiServer();
		return new ResponseEntity<ApiServerVo>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "nodeNotReady", method = RequestMethod.GET)
	public ResponseEntity<NodeNotReadyVo> getNodeNotReady() throws IOException {
		NodeNotReadyVo result = alertService.getNodeNotReady();
		return new ResponseEntity<NodeNotReadyVo>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "nodeDown", method = RequestMethod.GET)
	public ResponseEntity<NodeDownVo> getNodeDown() throws IOException {
		NodeDownVo result = alertService.getNodeDown();
		return new ResponseEntity<NodeDownVo>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "alertList", method = RequestMethod.GET)
	public ResponseEntity<List<AlertVo>> getAlertList() throws IOException {
		List<AlertVo> result = alertService.getAlertList();
		if (result == null) {
			return new ResponseEntity<List<AlertVo>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<AlertVo>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "alertHistory/{time}", method = RequestMethod.GET)
	public ResponseEntity<List<AlertHistoryVo>> getHistoryList(@PathVariable("time") final String time)
			throws IOException {
		List<AlertHistoryVo> result = alertService.getAlertHistoryList(time);
		if (result == null) {
			return new ResponseEntity<List<AlertHistoryVo>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<AlertHistoryVo>>(result, HttpStatus.OK);
	}

}
