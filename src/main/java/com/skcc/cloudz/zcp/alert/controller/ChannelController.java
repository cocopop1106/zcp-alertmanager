package com.skcc.cloudz.zcp.alert.controller;

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

import com.skcc.cloudz.zcp.alert.service.impl.ChannelServiceImpl;
import com.skcc.cloudz.zcp.alert.service.impl.RuleServiceImpl;
import com.skcc.cloudz.zcp.alert.vo.ChannelVo;
import com.skcc.cloudz.zcp.alert.vo.RuleVo;

@RestController
public class ChannelController {
	private static Logger logger = Logger.getLogger(ChannelController.class);
	
	@Autowired
	ChannelServiceImpl channelService;
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "channel", method = RequestMethod.GET)
	public ResponseEntity<List<ChannelVo>> getChannelList() throws IOException {
		List<ChannelVo> channelList = channelService.getChannelListService();
		if (channelList.isEmpty()) {
			return new ResponseEntity<List<ChannelVo>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<ChannelVo>>(HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "channel/{id}", method = RequestMethod.GET)
	public ResponseEntity<ChannelVo> getChannelDtl(@PathVariable("id") final Long id) {
		return new ResponseEntity<ChannelVo>(HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param ruleVo
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(value = "channel", method = RequestMethod.POST)
	public ResponseEntity<Void> createChannel(@RequestBody final ChannelVo channelVo, final UriComponentsBuilder ucBuilder) {
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	/**
	 * 
	 * @param id
	 * @param ruleVo
	 * @return
	 */
	@RequestMapping(value = "channel/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ChannelVo> updateChannel(@PathVariable("id") final Long id, @RequestBody final ChannelVo channelVo) {
		return new ResponseEntity<ChannelVo>(HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "channel/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteChannel(@PathVariable("id") final Long id) {
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
