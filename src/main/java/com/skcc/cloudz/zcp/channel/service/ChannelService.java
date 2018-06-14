package com.skcc.cloudz.zcp.channel.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skcc.cloudz.zcp.common.vo.ChannelData;
import com.skcc.cloudz.zcp.common.vo.ChannelDtlVo;
import com.skcc.cloudz.zcp.common.vo.ChannelListVo;
import com.skcc.cloudz.zcp.common.vo.ChannelVo;
import com.skcc.cloudz.zcp.manager.KubeCoreManager;

@Service
public class ChannelService {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ChannelService.class);

	@Autowired
	private KubeCoreManager kubeCoreManager;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<ChannelListVo> getChannelList() {
		List listChannels = kubeCoreManager.getChannelList();
		List<ChannelListVo> channelViewList = new ArrayList<ChannelListVo>();

		Map<String, Object> maplistReceivers;
		int count = 0;
		int id = 0;

		Iterator iterChannel = listChannels.iterator();

		while (iterChannel.hasNext()) {
			maplistReceivers = (Map) iterChannel.next();
			count = 0;
			ChannelListVo channel = new ChannelListVo();

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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ChannelDtlVo findById(int channelId) {
		List listChannels = kubeCoreManager.getChannelList();

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

	public ChannelVo createChannel(ChannelVo channelVo) {
		ChannelData channelData = new ChannelData();
		channelData.setChannel(channelVo.getChannel());

		ChannelData channelResult = kubeCoreManager.createChannel(channelData);

		if(channelResult != null) {
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					StringBuffer response = new StringBuffer();

					try {
						String url = "http://alertmanager.zcp-dev.jp-tok.containers.mybluemix.net/-/reload";
						URL obj = new URL(url);
						URLConnection conn = obj.openConnection();

						conn.setDoOutput(true);
						OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

						wr.flush();

						BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						String inputLine;

						while((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}

					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			};
			timer.schedule(task, 120000);
		}

		return channelVo;
	}
}
