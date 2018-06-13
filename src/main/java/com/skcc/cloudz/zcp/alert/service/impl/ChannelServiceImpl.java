package com.skcc.cloudz.zcp.alert.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skcc.cloudz.zcp.alert.dao.impl.ChannelDaoImpl;
import com.skcc.cloudz.zcp.alert.service.ChannelService;
import com.skcc.cloudz.zcp.alert.vo.ChannelVo;

@Service("channelService")
public class ChannelServiceImpl implements ChannelService {
	
private static Logger logger = Logger.getLogger(RuleServiceImpl.class);
	
	@Autowired
	ChannelDaoImpl channelDao;

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.service.ChannelService#getChannelListService()
	 */
	@Override
	public List<ChannelVo> getChannelListService() {
		// TODO Auto-generated method stub
		
		List listChannels = channelDao.getChannelList();
		List<ChannelVo> channelViewList = new ArrayList<ChannelVo>();
		
		return channelViewList;
	}
	
	

}
