package com.skcc.cloudz.zcp.alertmanager.api.channel.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.skcc.cloudz.zcp.alertmanager.common.exception.ZcpErrorCode;
import com.skcc.cloudz.zcp.alertmanager.common.exception.ZcpException;
import com.skcc.cloudz.zcp.alertmanager.common.vo.ChannelData;
import com.skcc.cloudz.zcp.alertmanager.common.vo.ChannelDtlVo;
import com.skcc.cloudz.zcp.alertmanager.common.vo.ChannelListVo;
import com.skcc.cloudz.zcp.alertmanager.common.vo.ChannelVo;
import com.skcc.cloudz.zcp.alertmanager.common.vo.RepeatVo;

import com.skcc.cloudz.zcp.alertmanager.manager.KubeCoreManager;

@Service
public class ChannelService {
	private static Logger logger = Logger.getLogger(ChannelService.class);

	@Autowired
	private KubeCoreManager kubeCoreManager;

	@Value("${props.alertManager.baseUrl}")
	private String baseUrl;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<ChannelListVo> getChannelList() throws ZcpException {
		List listChannels = new ArrayList();
		try {
			listChannels = kubeCoreManager.getChannelList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZcpException(ZcpErrorCode.GET_CHANNEL_LIST_ERROR, e);
		}

		List<ChannelListVo> channelViewList = new ArrayList<ChannelListVo>();

		Map<String, Object> maplistReceivers;
		int count = 0;
		int id = 0;

		Iterator iterChannel = listChannels.iterator();

		while (iterChannel.hasNext()) {
			maplistReceivers = (Map) iterChannel.next();
			count = 0;
			ChannelListVo channel = new ChannelListVo();

			if (!"default".equals(maplistReceivers.get("name")) && !"sk-cps-ops".equals(maplistReceivers.get("name"))
					&& !"zcp-webhook".equals(maplistReceivers.get("name"))) {
				channel.setChannel(maplistReceivers.get("name").toString());
				if (maplistReceivers.get("email_configs") != null)
					count++;
				if (maplistReceivers.get("hipchat_configs") != null)
					count++;
				if (maplistReceivers.get("slack_configs") != null)
					count++;
				if (maplistReceivers.get("webhook_configs") != null)
					count++;
				channel.setId(id + "");
				channel.setNotifications(count + "");
				channelViewList.add(channel);
			}
			id++;
		}

		return channelViewList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public ChannelDtlVo findById(int channelId) throws ZcpException {
		List listChannels = new ArrayList();
		try {
			listChannels = kubeCoreManager.getChannelList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZcpException(ZcpErrorCode.GET_CHANNEL_DETAIL_ERROR, e);
		}

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

			channel.setChannel(maplistReceivers.get("name").toString());
			if (maplistReceivers.get("email_configs") != null) {
				count++;
				configList = (List) maplistReceivers.get("email_configs");
				Iterator iterConfig = configList.iterator();

				while (iterConfig.hasNext()) {
					maplistConfig = (Map) iterConfig.next();

					if (maplistConfig.get("to") != null)
						channel.setEmail_to(maplistConfig.get("to").toString());
					if (maplistConfig.get("from") != null)
						channel.setEmail_from(maplistConfig.get("from").toString());
					if (maplistConfig.get("smarthost") != null)
						channel.setEmail_smarthost(maplistConfig.get("smarthost").toString());
					if (maplistConfig.get("auth_username") != null)
						channel.setEmail_auth_username(maplistConfig.get("auth_username").toString());
					if (maplistConfig.get("auth_password") != null)
						channel.setEmail_auth_password(maplistConfig.get("auth_password").toString());
					if (maplistConfig.get("require_tls") != null)
						channel.setEmail_require_tls(maplistConfig.get("require_tls").toString());
					if (maplistConfig.get("send_resolved") != null)
						channel.setEmail_send_resolved(maplistConfig.get("send_resolved").toString());
				}
			}

			if (maplistReceivers.get("slack_configs") != null) {
				count++;
				configList = (List) maplistReceivers.get("slack_configs");
				Iterator iterConfig = configList.iterator();

				while (iterConfig.hasNext()) {
					maplistConfig = (Map) iterConfig.next();

					if (maplistConfig.get("api_url") != null)
						channel.setSlack_api_url(maplistConfig.get("api_url").toString());
					if (maplistConfig.get("send_resolved") != null)
						channel.setSlack_send_resolved(maplistConfig.get("send_resolved").toString());
				}
			}

			if (maplistReceivers.get("hipchat_configs") != null) {
				count++;
				configList = (List) maplistReceivers.get("hipchat_configs");
				Iterator iterConfig = configList.iterator();

				while (iterConfig.hasNext()) {
					maplistConfig = (Map) iterConfig.next();

					if (maplistConfig.get("api_url") != null)
						channel.setHipchat_api_url(maplistConfig.get("api_url").toString());
					if (maplistConfig.get("room_id") != null)
						channel.setHipchat_room_id(maplistConfig.get("room_id").toString());
					if (maplistConfig.get("auth_token") != null)
						channel.setHipchat_auth_token(maplistConfig.get("auth_token").toString());
					if (maplistConfig.get("notify") != null)
						channel.setHipchat_notify(maplistConfig.get("notify").toString());
					if (maplistConfig.get("send_resolved") != null)
						channel.setHipchat_send_resolved(maplistConfig.get("send_resolved").toString());
				}
			}

			if (maplistReceivers.get("webhook_configs") != null) {
				count++;
				configList = (List) maplistReceivers.get("webhook_configs");
				Iterator iterConfig = configList.iterator();

				while (iterConfig.hasNext()) {
					maplistConfig = (Map) iterConfig.next();

					if (maplistConfig.get("url") != null)
						channel.setWebhook_url(maplistConfig.get("url").toString());
					if (maplistConfig.get("send_resolved") != null)
						channel.setWebhook_send_resolved(maplistConfig.get("send_resolved").toString());
				}
			}

			channel.setNotifications(count + "");
			channelViewList.add(channel);

			id++;
		}

		channelDtlVo = channelViewList.get(channelId);

		return channelDtlVo;
	}

	public ChannelVo createChannel(ChannelVo channelVo) throws ZcpException {
		ChannelData channelData = new ChannelData();
		channelData.setChannel(channelVo.getChannel());

		ChannelData channelResult = new ChannelData();
		try {
			channelResult = kubeCoreManager.createChannel(channelData);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZcpException(ZcpErrorCode.CREATE_CHANNEL_ERROR, e);
		}

		if (channelResult != null) {
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					StringBuffer response = new StringBuffer();

					try {
						String url = UriComponentsBuilder.fromUriString(baseUrl).path("/-/reload").build().toString();
						URL obj = new URL(url);
						URLConnection conn = obj.openConnection();

						conn.setDoOutput(true);
						OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

						wr.flush();

						BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						String inputLine;

						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			timer.schedule(task, 120000);
		}

		return channelVo;
	}

	public Boolean deleteChannel(int id, String channel) throws ZcpException {
		Boolean result = null;
		try {
			result = kubeCoreManager.deleteChannel(id, channel);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZcpException(ZcpErrorCode.DELETE_CHANNEL_ERROR, e);
		}

		if (result != null) {
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					StringBuffer response = new StringBuffer();

					try {
						String url = UriComponentsBuilder.fromUriString(baseUrl).path("/-/reload").build().toString();
						URL obj = new URL(url);
						URLConnection conn = obj.openConnection();

						conn.setDoOutput(true);
						OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

						wr.flush();

						BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						String inputLine;

						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			timer.schedule(task, 120000);
		}

		return result;
	}

	public ChannelDtlVo updateChannel(int id, ChannelDtlVo channelDtlVo) throws ZcpException {
		ChannelDtlVo result = new ChannelDtlVo();

		try {
			result = kubeCoreManager.updateChannel(id, channelDtlVo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZcpException(ZcpErrorCode.UPDATE_CHANNEL_ERROR, e);
		}

		if (result != null) {
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					StringBuffer response = new StringBuffer();

					try {
						String url = UriComponentsBuilder.fromUriString(baseUrl).path("/-/reload").build().toString();
						URL obj = new URL(url);
						URLConnection conn = obj.openConnection();

						conn.setDoOutput(true);
						OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

						wr.flush();

						BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						String inputLine;

						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			timer.schedule(task, 120000);
		}

		return channelDtlVo;
	}

	public RepeatVo getRepeatInterval() throws ZcpException {
		Map<String, Object> routeMap = new HashMap<String, Object>();

		try {
			routeMap = kubeCoreManager.getRepeatInterval();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZcpException(ZcpErrorCode.GET_REPEAT_INTERVAL_ERROR, e);
		}

		RepeatVo repeatVo = new RepeatVo();
		repeatVo.setRepeat_interval(routeMap.get("repeat_interval").toString());

		return repeatVo;
	}

	public RepeatVo updateRepeatInterval(RepeatVo repeatVo) throws ZcpException {
		RepeatVo result = new RepeatVo();

		try {
			result = kubeCoreManager.updateRepeatInterval(repeatVo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZcpException(ZcpErrorCode.UPDATE_REPEAT_INTERVAL_ERROR, e);
		}

		if (result != null) {
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {

					try {
						String url = UriComponentsBuilder.fromUriString(baseUrl).path("/-/reload").build().toString();
						URL obj = new URL(url);
						HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

						conn.setDoOutput(true);
						OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

						wr.flush();
						logger.debug(conn.getResponseCode());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			timer.schedule(task, 120000);
		}

		return result;
	}

	public ChannelVo updateChannelName(int id, ChannelVo channelVo) throws ZcpException {
		ChannelVo result = new ChannelVo();

		try {
			result = kubeCoreManager.updateChannelName(id, channelVo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZcpException(ZcpErrorCode.UPDATE_CHANNEL_NAME_ERROR, e);
		}

		if (result != null) {
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {

					try {
						String url = UriComponentsBuilder.fromUriString(baseUrl).path("/-/reload").build().toString();
						URL obj = new URL(url);
						HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

						conn.setDoOutput(true);
						OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

						wr.flush();
						logger.debug(conn.getResponseCode());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			timer.schedule(task, 120000);
		}

		return result;
	}

	public Boolean deleteNotification(int id, String channel, ChannelDtlVo channelDtlVo) throws ZcpException {
		Boolean result = null;

		try {
			result = kubeCoreManager.deleteNotification(id, channel, channelDtlVo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZcpException(ZcpErrorCode.DELETE_NOTIFICATION_ERROR, e);
		}

		if (result != null) {
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					StringBuffer response = new StringBuffer();

					try {
						String url = UriComponentsBuilder.fromUriString(baseUrl).path("/-/reload").build().toString();
						URL obj = new URL(url);
						URLConnection conn = obj.openConnection();

						conn.setDoOutput(true);
						OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

						wr.flush();

						BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						String inputLine;

						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			timer.schedule(task, 120000);
		}

		return result;
	}

}
