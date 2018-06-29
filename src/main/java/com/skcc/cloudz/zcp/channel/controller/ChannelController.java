package com.skcc.cloudz.zcp.channel.controller;

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

import com.skcc.cloudz.zcp.common.vo.ChannelDtlVo;
import com.skcc.cloudz.zcp.common.vo.ChannelListVo;
import com.skcc.cloudz.zcp.common.vo.ChannelVo;
import com.skcc.cloudz.zcp.common.vo.RepeatVo;
import com.skcc.cloudz.zcp.channel.service.ChannelService;

@RestController
public class ChannelController {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ChannelController.class);

	@Autowired
	private ChannelService channelService;

	@RequestMapping(value = "channel", method = RequestMethod.GET)
	public ResponseEntity<List<ChannelListVo>> getChannelList() throws IOException {
		List<ChannelListVo> channelList = channelService.getChannelList();
		if (channelList.isEmpty()) {
			return new ResponseEntity<List<ChannelListVo>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<ChannelListVo>>(channelList, HttpStatus.OK);
	}

	@RequestMapping(value = "channel/{id}", method = RequestMethod.GET)
	public ResponseEntity<ChannelDtlVo> getChannelDtl(@PathVariable("id") final int id) {
		ChannelDtlVo channelDtl = channelService.findById(id);
		if (channelDtl == null) {
			return new ResponseEntity<ChannelDtlVo>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ChannelDtlVo>(channelDtl, HttpStatus.OK);
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "channel", method = RequestMethod.POST)
	public ResponseEntity<Void> createChannel(@RequestBody final ChannelVo channelVo,
			final UriComponentsBuilder ucBuilder) {
		ChannelVo createChannel = channelService.createChannel(channelVo);

		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "channel/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ChannelDtlVo> updateChannel(@PathVariable("id") final int id,
			@RequestBody final ChannelDtlVo channelDtlVo) {
		ChannelDtlVo updateChannel = channelService.updateChannel(id, channelDtlVo);

		if (updateChannel == null) {
			return new ResponseEntity<ChannelDtlVo>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ChannelDtlVo>(HttpStatus.OK);
	}

	@RequestMapping(value = "channel/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteChannel(@PathVariable("id") final int id) {
		ChannelDtlVo channelResult = channelService.findById(id);
		if (channelResult == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} else {
			channelService.deleteChannel(id, channelResult.getChannel());
			return new ResponseEntity<Void>(HttpStatus.OK);
		}

	}

	@RequestMapping(value = "repeat", method = RequestMethod.GET)
	public ResponseEntity<RepeatVo> getRepeatInterval() {
		RepeatVo repeatVo = channelService.getRepeatInterval();
		if (repeatVo == null) {
			return new ResponseEntity<RepeatVo>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RepeatVo>(repeatVo, HttpStatus.OK);
	}

	@RequestMapping(value = "repeat", method = RequestMethod.PUT)
	public ResponseEntity<RepeatVo> updateRepeatInterval(@RequestBody final RepeatVo repeatVo) {
		RepeatVo updateRepeatInterval = channelService.updateRepeatInterval(repeatVo);

		if (updateRepeatInterval == null) {
			return new ResponseEntity<RepeatVo>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RepeatVo>(HttpStatus.OK);
	}

}
