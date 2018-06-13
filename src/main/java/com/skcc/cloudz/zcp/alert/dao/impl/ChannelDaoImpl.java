package com.skcc.cloudz.zcp.alert.dao.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skcc.cloudz.zcp.alert.dao.ChannelDao;
import com.skcc.cloudz.zcp.alert.dao.RuleDao;
import com.skcc.cloudz.zcp.alert.vo.ChannelData;
import com.skcc.cloudz.zcp.alert.vo.RuleData;
import com.skcc.cloudz.zcp.common.util.Message;
import com.skcc.cloudz.zcp.common.yamlbeans.YamlReader;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1ConfigMap;
import io.kubernetes.client.util.Config;

@Repository("channelDao")
public class ChannelDaoImpl implements ChannelDao {
	
	private static Logger logger = Logger.getLogger(ChannelDaoImpl.class);
	
	@Autowired
    Message message;

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.dao.ChannelDao#getChannelListDao()
	 */
	@Override
	public List<ChannelData> getChannelList() {
		// TODO Auto-generated method stub
		
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
			// TODO Auto-generated catch block
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

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.dao.ChannelDao#createChannel(com.skcc.cloudz.zcp.alert.vo.ChannelData)
	 */
	@Override
	public ChannelData createChannel(ChannelData channel) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
