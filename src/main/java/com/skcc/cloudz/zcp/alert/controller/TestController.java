package com.skcc.cloudz.zcp.alert.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.skcc.cloudz.zcp.common.yamlbeans.YamlReader;
import com.skcc.cloudz.zcp.common.yamlbeans.YamlWriter;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1ConfigMap;
import io.kubernetes.client.util.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Controller
public class TestController {
	private static Logger logger = Logger.getLogger(TestController.class);
	private String str, receiveMsg;
	
	@Resource(name="redisTemplate")
	private RedisTemplate<String, Object> redisTemplate;
	
	// String
	@Resource(name="redisTemplate") 
	private ValueOperations<String, String> valueOperations;
	
	// Set
	@Resource(name="redisTemplate") 
	private SetOperations<String, String> setOperations;
	
	// Sorted Set
	@Resource(name="redisTemplate") 
	private ZSetOperations<String, String> zSetOperations;

	// Hashes
	@Resource(name = "redisTemplate") 
	private HashOperations<String, String, String> hashOperations;
	
	// List
	@Resource(name="redisTemplate") 
	private ListOperations<String, String> listOperations;
	
	@RequestMapping(value="/alopex") 
	public String alopex(HttpServletRequest request) {
		
		return "alopexTest";
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping(value="/result")
	public String list(Model model, HttpServletRequest request) {
		try {
			String addr = request.getParameter("addr");
            URL url = new URL(addr);
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
 
            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                logger.debug(receiveMsg);
                
                model.addAttribute("alertList", receiveMsg);
 
                reader.close();
            } else {
            	logger.debug(conn.getResponseCode());
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return "hello";
	}
	
	@RequestMapping(value = "test", method = RequestMethod.GET)
	public void getTest() throws IOException, ApiException {
			
		ApiClient client = Config.defaultClient();
		Configuration.setDefaultApiClient(client);

		CoreV1Api api = new CoreV1Api();
		V1ConfigMap configMap = api.readNamespacedConfigMap("prometheus-user-rules", "monitoring", null, null, null);
		
		logger.info(configMap.getData());		
		
        File file = new File("rule.yaml");
        FileWriter writer = null;
        
        try {
            writer = new FileWriter(file, false);
            writer.write(configMap.getData().get("alertmanager.rules"));
            writer.flush();
            
            System.out.println("DONE");
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null) writer.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        
        try {
        	YamlReader reader = new YamlReader(new FileReader("rule.yaml"));
            Object object = reader.read();
            System.out.println(object);
            
            Map<String, Object> maplist;
            
			Map<String, Map<String, Object>> map = (Map)object;
			System.out.println(map.get("groups"));
            
			List listA = (List)map.get("groups");
			System.out.println(listA);
			
			Iterator iterator = listA.iterator();
			while (iterator.hasNext()) {
			    maplist = (Map) iterator.next();
			    System.out.println(maplist.get("name"));
			    System.out.println("maplist"+maplist);
			    maplist.put("name", "user1.rules");
			    System.out.println("maplist"+maplist);
			}
			System.out.println(listA);
			System.out.println(map.get("groups"));
			
			System.out.println(map);
			System.out.println(object);
			
			YamlWriter yamlWriter = new YamlWriter(new FileWriter("rule.yaml"));
			yamlWriter.write(object);
			yamlWriter.close();
			
			// Object를 Map으로 변경
			Map<String, String> obj = (Map)object;
			obj.put("alertmanager.rules", obj.toString());
			
			System.out.println(obj);
			configMap.setData(obj);
			System.out.println(configMap.getData());

			// replaceNamespacedConfigMap
			V1ConfigMap configMap2 = api.readNamespacedConfigMap("test-rules", "monitoring", null, null, null);
			V1ConfigMap replace = api.replaceNamespacedConfigMap("test-rules", "monitoring", configMap2, null);
			logger.info(replace.getData());
			
			
			
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
	}
	
	@RequestMapping(value = "setUser", method = RequestMethod.GET)
	public void setRedis() {
		
		String token = "1234";
		
		HashMap user = new HashMap();
		user.put("userId", "cocopop1106");
		user.put("userName", "오서우");
		
		System.out.println(user);
		
		redisTemplate.opsForHash().putAll(token, user);
		redisTemplate.opsForHash().put(token, "userPhone", "010-2392-0859");
	}
	
	@RequestMapping(value = "getUser", method = RequestMethod.GET)
	public Map getUserId(@RequestParam("token") String token) {
		
		String userName = (String) redisTemplate.opsForHash().get("alerts", "2018-05-31(10:21:14)_NodeCPUUsage_first_receiver");
		
		HashMap returnHm = new HashMap();
		returnHm.put("alerts", userName);
		
		System.out.println(returnHm);
		
		return returnHm;
	}
	
	@RequestMapping(value = "findall", method = RequestMethod.GET)
	public Map<String, String> getKey() {
		
//		Map<String, String> key = hashOperations.entries("2018-05-31(19:37:29)_NodeCPUUsage_first_receiver");
//		logger.debug(key.values());
//		logger.debug(key.get("status"));
		
//		hashOperations = redisTemplate.opsForHash();
//		hashOperations.get("2018-05-31(19:37:29)_NodeCPUUsage_first_receiver", "status");
//		logger.debug(hashOperations);
//		logger.debug(hashOperations.get("2018-05-31(19:37:29)_NodeCPUUsage_first_receiver", "status"));
//		hashOperations.keys("2018-05-31(19:37:29)_NodeCPUUsage_first_receiver");
//		logger.debug(hashOperations.keys("2018-05-31(19:37:29)_NodeCPUUsage_first_receiver"));
		
		hashOperations = redisTemplate.opsForHash();
		logger.debug("=============================");
		logger.debug(hashOperations);
		
		hashOperations.get("status", "2018-05-31(19:37:29)_NodeCPUUsage_first_receiver");
		logger.debug(hashOperations.get("status", "2018-05-31(19:37:29)_NodeCPUUsage_first_receiver"));
		Map<String, String> all = hashOperations.entries("2018-05-31(19:37:29)_NodeCPUUsage_first_receiver");
		logger.debug(all);
		for(String str : all.values()) {
			logger.debug(str.toString());
		}
		
		return null;
	}
	
	@RequestMapping(value = "jedis", method = RequestMethod.GET)
	public Map<String, String> jedis() {
		
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		JedisPool pool = new JedisPool(jedisPoolConfig, "169.56.100.58", 32000, 1000);

		Jedis jedis = pool.getResource();

		Map<String, String> fields = jedis.hgetAll("2018-05-31(19:37:29)_NodeCPUUsage_first_receiver");
		logger.debug(jedis.hget("2018-05-31(19:37:29)_NodeCPUUsage_first_receiver", "status"));
		logger.debug(fields);
		logger.debug(fields.get("status"));

		Set<String> keys = jedis.keys("*");

		for (String key : keys) {
//			String id = key.split(":")[2];
//			long ticketId = Long.parseLong(id);
//			List<Change> changes = getJournal(jedis, repository, ticketId);
//			if (ArrayUtils.isEmpty(changes)) {
//				log.warn("Empty journal for {}:{}", repository, ticketId);
//				continue;
//			}
//			TicketModel ticket = TicketModel.buildTicket(changes);
//			ticket.project = repository.projectPath;
//			ticket.repository = repository.name;
//			ticket.number = ticketId;
//
//			// add the ticket, conditionally, to the list
//			if (filter == null) {
//				list.add(ticket);
//			} else {
//				if (filter.accept(ticket)) {
//					list.add(ticket);
//				}
//			}
//			logger.debug(key);
		}
		
		return fields;
	}
	
}
