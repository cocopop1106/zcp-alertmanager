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
		List configList = null;
		Map<String, Object> maplistConfig;
		
		while (iterChannel.hasNext()) {
			maplistReceivers = (Map) iterChannel.next();
			count = 0;
		    ChannelDtlVo channel = new ChannelDtlVo();
		    
		    if(!"sk-cps-team".equals(maplistReceivers.get("name")) && !"zcp-webhook".equals(maplistReceivers.get("name"))) {
		    	channel.setChannel(maplistReceivers.get("name").toString());
		    	if(maplistReceivers.get("email_configs") != null) {
		    		count++;
		    		configList = (List)maplistReceivers.get("email_configs");
		    		Iterator iterConfig = configList.iterator();
		    		
		    		while (iterConfig.hasNext()) {
		    			maplistConfig = (Map) iterConfig.next();
		    			
		    			if(maplistConfig.get("to") != null)
		    				channel.setEmail_to(maplistConfig.get("to").toString());
		    			if(maplistConfig.get("from") != null)
		    				channel.setEmail_from(maplistConfig.get("from").toString());
		    			if(maplistConfig.get("smarthost") != null)
		    				channel.setEmail_smarthost(maplistConfig.get("smarthost").toString());
		    			if(maplistConfig.get("auth_username") != null)
		    				channel.setEmail_auth_username(maplistConfig.get("auth_username").toString());
		    			if(maplistConfig.get("auth_password") != null)
		    				channel.setEmail_auth_password(maplistConfig.get("auth_password").toString());
		    			if(maplistConfig.get("require_tls") != null)
		    				channel.setEmail_require_tls(maplistConfig.get("require_tls").toString());
		    			if(maplistConfig.get("send_resolved") != null)
		    				channel.setEmail_send_resolved(maplistConfig.get("send_resolved").toString());
		    		}
		    	}
		    	
		    	if(maplistReceivers.get("slack_configs") != null)	 {
		    		count++;
		    		configList = (List)maplistReceivers.get("slack_configs");
		    		Iterator iterConfig = configList.iterator();
		    		
		    		while (iterConfig.hasNext()) {
		    			maplistConfig = (Map) iterConfig.next();
		    			
		    			if(maplistConfig.get("api_url") != null)
		    				channel.setSlack_api_url(maplistConfig.get("api_url").toString());
		    			if(maplistConfig.get("send_resolved") != null)
		    				channel.setSlack_send_resolved(maplistConfig.get("send_resolved").toString());
		    		}
		    	}
		    		
		    	if(maplistReceivers.get("hipchat_configs") != null)	{
		    		count++;
		    		configList = (List)maplistReceivers.get("hipchat_configs");
		    		Iterator iterConfig = configList.iterator();
		    		
		    		while (iterConfig.hasNext()) {
		    			maplistConfig = (Map) iterConfig.next();
		    			
		    			if(maplistConfig.get("api_url") != null)
		    				channel.setHipchat_api_url(maplistConfig.get("api_url").toString());
		    			if(maplistConfig.get("room_id") != null)
		    				channel.setHipchat_room_id(maplistConfig.get("room_id").toString());
		    			if(maplistConfig.get("auth_token") != null)
		    				channel.setHipchat_auth_token(maplistConfig.get("auth_token").toString());
		    			if(maplistConfig.get("notify") != null)
		    				channel.setHipchat_notify(maplistConfig.get("notify").toString());
		    			if(maplistConfig.get("send_resolved") != null)
		    				channel.setHipchat_send_resolved(maplistConfig.get("send_resolved").toString());
		    		}
		    	}
		    	
		    	if(maplistReceivers.get("webhook_configs") != null) {
		    		count++;
		    		configList = (List)maplistReceivers.get("webhook_configs");
		    		Iterator iterConfig = configList.iterator();
		    		
		    		while (iterConfig.hasNext()) {
		    			maplistConfig = (Map) iterConfig.next();
		    			
		    			if(maplistConfig.get("url") != null)
		    				channel.setWebhook_url(maplistConfig.get("url").toString());
		    			if(maplistConfig.get("send_resolved") != null)
		    				channel.setWebhook_send_resolved(maplistConfig.get("send_resolved").toString());
		    		}
		    	}
		    		
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
