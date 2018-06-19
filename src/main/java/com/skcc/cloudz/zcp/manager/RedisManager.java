package com.skcc.cloudz.zcp.manager;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;


@Component
public class RedisManager {
	private static Logger logger = Logger.getLogger(RedisManager.class);
	
	@Value("${props.redis.host}")
    private String host;
	
	@Value("${props.redis.port}")
    private int port;
	
	
	public JSONObject getAlertHistoryList() {
		JSONObject jsonObj = new JSONObject();
		
		Jedis jedis = new Jedis(host, port);
		jedis.get("1529152227_PodFrequentlyRestarting_zcp-webhook");
//		String s0 = (String) JReJSON.get(jedis, "1529152227_PodFrequentlyRestarting_zcp-webhook");
		
		return null;
	}
	

}
