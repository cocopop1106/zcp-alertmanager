package com.skcc.cloudz.zcp.alert.service;

import java.util.List;

import com.skcc.cloudz.zcp.alert.vo.ChannelDtlVo;
import com.skcc.cloudz.zcp.alert.vo.ChannelListVo;
import com.skcc.cloudz.zcp.alert.vo.ChannelVo;

public interface ChannelService {
	
	/**
	 * 
	 * @return
	 */
	List<ChannelListVo> getChannelList();
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	ChannelDtlVo findById(int id);
	
	/**
	 * 
	 * @param channelDtlVo
	 * @return
	 */
	ChannelVo createChannel(ChannelVo channelVo);

}
