package com.skcc.cloudz.zcp.alert.service;

import java.util.List;

import com.skcc.cloudz.zcp.alert.vo.ChannelDtlVo;
import com.skcc.cloudz.zcp.alert.vo.ChannelVo;

public interface ChannelService {
	
	/**
	 * 
	 * @return
	 */
	List<ChannelVo> getChannelList();
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	ChannelDtlVo findById(int id);

}
