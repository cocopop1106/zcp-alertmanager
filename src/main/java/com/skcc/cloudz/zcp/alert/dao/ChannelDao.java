package com.skcc.cloudz.zcp.alert.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.skcc.cloudz.zcp.alert.vo.ChannelData;

@Repository("channelDao")
public interface ChannelDao {

	List<ChannelData> getChannelList();
	
	ChannelData createChannel(ChannelData channel);
}
