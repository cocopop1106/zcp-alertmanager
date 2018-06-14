package com.skcc.cloudz.zcp.alert.dao.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skcc.cloudz.zcp.alert.dao.ChannelDao;
import com.skcc.cloudz.zcp.alert.vo.ChannelData;
import com.skcc.cloudz.zcp.alert.vo.RuleData;
import com.skcc.cloudz.zcp.common.util.Message;
import com.skcc.cloudz.zcp.common.yamlbeans.YamlConfig;
import com.skcc.cloudz.zcp.common.yamlbeans.YamlReader;
import com.skcc.cloudz.zcp.common.yamlbeans.YamlWriter;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1ConfigMap;
import io.kubernetes.client.util.Config;

@Repository("channelDao")
public class ChannelDaoImpl implements ChannelDao {
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ChannelDaoImpl.class);
	
	@Autowired
    Message message;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<ChannelData> getChannelList() {
		
		FileWriter writer = null;
		List listChannel = null;
		
		try {
			ApiClient client = Config.defaultClient();
			Configuration.setDefaultApiClient(client);
	
			CoreV1Api api = new CoreV1Api();
			V1ConfigMap configMap;
			
			configMap = api.readNamespacedConfigMap("test-alertmanager", "monitoring", null, null, null);
			
			File file = new File("channel.yaml");
	        
	        writer = new FileWriter(file, false);
	        writer.write(configMap.getData().get("config.yml"));
	        writer.flush();
	        
	        YamlReader reader = new YamlReader(new FileReader("channel.yaml"));
            Object object = reader.read();
            
			Map<String, Map<String, Object>> mapGlobal = (Map)object;
			listChannel = (List)mapGlobal.get("receivers");
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
            try {
                if(writer != null) writer.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
	        
		return listChannel;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	@Override
	public ChannelData createChannel(ChannelData channel) {
		
		List<RuleData> ruleList = new ArrayList<RuleData>();
		FileWriter writer = null;
		List receiverList = null;
		Map<String, Object> routeMap = new HashMap<String, Object>();
		Map<String, Object> newRouteMap = new LinkedHashMap<String, Object>();
		List routesList = null;
		
		try {
			ApiClient client = Config.defaultClient();
			Configuration.setDefaultApiClient(client);
	
			CoreV1Api api = new CoreV1Api();
			V1ConfigMap configMap = new V1ConfigMap();
			
			configMap = api.readNamespacedConfigMap("test-alertmanager", "monitoring", null, null, null);
			System.out.println(configMap);
			File file = new File("channel.yaml");
	        
	        writer = new FileWriter(file, false);
	        writer.write(configMap.getData().get("config.yml"));
	        writer.flush();
	        
	        YamlReader reader = new YamlReader(new FileReader("channel.yaml"));
            Object object = reader.read();
            
			Map<String, Map<String, Object>> mapGlobal = (Map)object;
			
			routeMap = mapGlobal.get("route");
			routesList = (List)routeMap.get("routes");
			
			HashMap<String, Object> matchMap = new LinkedHashMap<String, Object>();
			matchMap.put("channel", channel.getChannel());
			matchMap.put("receiver", channel.getChannel());
			
			HashMap<String, Object> routesMap = new HashMap<String, Object>();
			routesMap.put("match", matchMap);
			
			routesList.add(routesMap);
			
			newRouteMap.put("group_by", routeMap.get("group_by"));
			newRouteMap.put("group_wait", routeMap.get("group_wait"));
			newRouteMap.put("group_interval", routeMap.get("group_interval"));
			newRouteMap.put("repeat_interval", routeMap.get("repeat_interval"));
			newRouteMap.put("receiver", routeMap.get("receiver"));
			newRouteMap.put("routes", routesList);
			
			receiverList = (List)mapGlobal.get("receivers");
			
			HashMap<String, Object> newReceiver = new LinkedHashMap<String, Object>();
			newReceiver.put("name", channel.getChannel());
			
			receiverList.add(newReceiver);
			
			Map<String, Object> channelMap = new LinkedHashMap<String, Object>();
			channelMap.put("global", mapGlobal.get("global"));
			channelMap.put("templates", mapGlobal.get("templates"));
			channelMap.put("route", newRouteMap);
			channelMap.put("receivers", receiverList);
			
			YamlConfig config = new YamlConfig();
			YamlWriter ywriter = new YamlWriter(new FileWriter("channel.yaml"), config);
			ywriter.write(channelMap);
			ywriter.close();
			
			String yamlString = FileUtils.readFileToString(new File("channel.yaml"), "utf8");
    		
    		Map<String, String> data = new HashMap<String, String>();
    		data.put("config.yml", yamlString);
    		
    		configMap.setData(data);
			V1ConfigMap replacedConfigmap = api.replaceNamespacedConfigMap("test-alertmanager", "monitoring", configMap, null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return channel;
	}

	
}
