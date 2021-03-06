package com.skcc.cloudz.zcp.alertmanager.api.channel.controller;

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

import com.skcc.cloudz.zcp.alertmanager.common.vo.ChannelDtlVo;
import com.skcc.cloudz.zcp.alertmanager.common.vo.ChannelListVo;
import com.skcc.cloudz.zcp.alertmanager.common.vo.ChannelVo;
import com.skcc.cloudz.zcp.alertmanager.common.vo.RepeatVo;

import com.skcc.cloudz.zcp.alertmanager.api.channel.service.ChannelService;

@RestController
public class ChannelController {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ChannelController.class);

	@Autowired
	private ChannelService channelService;

	@RequestMapping(value = "channel", method = RequestMethod.GET)
	public ResponseEntity<List<ChannelListVo>> getChannelList() throws Exception {
		List<ChannelListVo> channelList = channelService.getChannelList();
		if (channelList.isEmpty()) {
			return new ResponseEntity<List<ChannelListVo>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<ChannelListVo>>(channelList, HttpStatus.OK);
	}

	@RequestMapping(value = "channel/{id}", method = RequestMethod.GET)
	public ResponseEntity<ChannelDtlVo> getChannelDtl(@PathVariable("id") final int id) throws Exception {
		ChannelDtlVo channelDtl = channelService.findById(id);
		if (channelDtl == null) {
			return new ResponseEntity<ChannelDtlVo>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ChannelDtlVo>(channelDtl, HttpStatus.OK);
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "channel", method = RequestMethod.POST)
	public ResponseEntity<Void> createChannel(@RequestBody final ChannelVo channelVo,
			final UriComponentsBuilder ucBuilder) throws Exception {
		ChannelVo createChannel = channelService.createChannel(channelVo);

		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "channel/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ChannelDtlVo> updateChannel(@PathVariable("id") final int id,
			@RequestBody final ChannelDtlVo channelDtlVo) throws Exception {
		ChannelDtlVo updateChannel = channelService.updateChannel(id, channelDtlVo);

		if (updateChannel == null) {
			return new ResponseEntity<ChannelDtlVo>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ChannelDtlVo>(HttpStatus.OK);
	}

	@RequestMapping(value = "channel/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteChannel(@PathVariable("id") final int id) throws Exception {
		ChannelDtlVo channelResult = channelService.findById(id);
		if (channelResult == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} else {
			channelService.deleteChannel(id, channelResult.getChannel());
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
	}

	@RequestMapping(value = "repeat", method = RequestMethod.GET)
	public ResponseEntity<RepeatVo> getRepeatInterval() throws Exception {
		RepeatVo repeatVo = channelService.getRepeatInterval();
		if (repeatVo == null) {
			return new ResponseEntity<RepeatVo>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RepeatVo>(repeatVo, HttpStatus.OK);
	}

	@RequestMapping(value = "repeat", method = RequestMethod.PUT)
	public ResponseEntity<RepeatVo> updateRepeatInterval(@RequestBody final RepeatVo repeatVo) throws Exception {
		RepeatVo updateRepeatInterval = channelService.updateRepeatInterval(repeatVo);

		if (updateRepeatInterval == null) {
			return new ResponseEntity<RepeatVo>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RepeatVo>(HttpStatus.OK);
	}

	@RequestMapping(value = "channelName/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ChannelVo> updateChannelName(@PathVariable("id") final int id,
			@RequestBody final ChannelVo channelVo) throws Exception {
		ChannelVo updateChannelName = channelService.updateChannelName(id, channelVo);

		if (updateChannelName == null) {
			return new ResponseEntity<ChannelVo>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ChannelVo>(HttpStatus.OK);
	}

	@RequestMapping(value = "notification/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteNotification(@PathVariable("id") final int id,
			@RequestBody final ChannelDtlVo channelDtlVo) throws Exception {

		ChannelDtlVo channelResult = channelService.findById(id);
		if (channelResult == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} else {
			channelService.deleteNotification(id, channelResult.getChannel(), channelDtlVo);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
	}

}
