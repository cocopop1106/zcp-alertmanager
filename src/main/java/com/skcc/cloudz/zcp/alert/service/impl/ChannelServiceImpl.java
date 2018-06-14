package com.skcc.cloudz.zcp.alert.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skcc.cloudz.zcp.alert.dao.impl.ChannelDaoImpl;
import com.skcc.cloudz.zcp.alert.service.ChannelService;
import com.skcc.cloudz.zcp.alert.vo.ChannelDtlVo;
import com.skcc.cloudz.zcp.alert.vo.ChannelVo;
import com.skcc.cloudz.zcp.alert.vo.RuleVo;

@Service("channelService")
public class ChannelServiceImpl implements ChannelService {
	
private static Logger logger = Logger.getLogger(RuleServiceImpl.class);
	
	@Autowired
	ChannelDaoImpl channelDao;

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.service.ChannelService#getChannelListService()
	 */
	@Override
	public List<ChannelVo> getChannelList() {
		// TODO Auto-generated method stub
		
		List listChannels = channelDao.getChannelList();
		List<ChannelVo> channelViewList = new ArrayList<ChannelVo>();
		
		Map<String, Object> maplistReceivers;
		int count = 0;
		int id = 0;
		
		Iterator iterChannel = listChannels.iterator();
		
		while (iterChannel.hasNext()) {
			maplistReceivers = (Map) iterChannel.next();
			count = 0;
		    ChannelVo channel = new ChannelVo();
		    
		    
		    if(!"sk-cps-team".equals(maplistReceivers.get("name")) && !"zcp-webhook".equals(maplistReceivers.get("name"))) {
		    	channel.setChannel(maplistReceivers.get("name").toString());
		    	if(maplistReceivers.get("email_configs") != null)	
		    		count++;
		    	if(maplistReceivers.get("hipchat_configs") != null)	
		    		count++;
		    	if(maplistReceivers.get("slack_configs") != null)	
		    		count++;
		    	if(maplistReceivers.get("webhook_configs") != null)	
		    		count++;
		    	channel.setId(id+"");
		    	channel.setNotifications(count+"");
		    	channelViewList.add(channel);
		    	
			    id++;
		    }
		}
		
		return channelViewList;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.service.ChannelService#getChannelDtl(int)
	 */
	@Override
	public ChannelDtlVo findById(int channelId) {
		// TODO Auto-generated method stub
		
		List listChannels = channelDao.getChannelList();
		
		List<ChannelDtlVo> channelViewList = new ArrayList<ChannelDtlVo>();
		Map<String, Object> maplistReceivers;
		int count = 0;
		int id = 0;
		ChannelDtlVo channelDtlVo = new ChannelDtlVo();
		
		Iterator iterChannel = listChannels.iterator();
		
		while (iterChannel.hasNext()) {
			maplistReceivers = (Map) iterChannel.next();
			count = 0;
		    ChannelDtlVo channel = new ChannelDtlVo();
		    
		    if(!"sk-cps-team".equals(maplistReceivers.get("name")) && !"zcp-webhook".equals(maplistReceivers.get("name"))) {
		    	channel.setChannel(maplistReceivers.get("name").toString());
		    	if(maplistReceivers.get("email_configs") != null) {
		    		count++;
		    		
		    	}
		    		
		    	if(maplistReceivers.get("hipchat_configs") != null)	
		    		count++;
		    	if(maplistReceivers.get("slack_configs") != null)	
		    		count++;
		    	if(maplistReceivers.get("webhook_configs") != null)	
		    		count++;
		    	channel.setId(id+"");
		    	channel.setNotifications(count+"");
		    	channelViewList.add(channel);
		    	
			    id++;
		    }
		}
		
		channelDtlVo = channelViewList.get(channelId);
		
		return channelDtlVo;
	}
	
	

}
