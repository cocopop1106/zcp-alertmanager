package com.skcc.cloudz.zcp.alert.dao.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Repository;

import com.skcc.cloudz.zcp.alert.dao.AlertDao;
import com.skcc.cloudz.zcp.alert.vo.AlertCountData;
import com.skcc.cloudz.zcp.alert.vo.AlertData;
import com.skcc.cloudz.zcp.alert.vo.ApiServerData;
import com.skcc.cloudz.zcp.common.util.JsonUtil;


@Repository("alertDao")
public class AlertDaoImpl implements AlertDao {
	private static Logger logger = Logger.getLogger(AlertDaoImpl.class);
	private String str, receiveMsg;
	
	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.dao.AlertDao#getAlertCount()
	 */
	@Override
	public JSONObject getAlertCount() {
		// TODO Auto-generated method stub
		
		JSONObject jsonObj = new JSONObject();
		
		try {
			String addr = "http://prometheus.zcp-dev.jp-tok.containers.mybluemix.net/api/v1/query?query=alertmanager_alerts{state=%22active%22}";
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
                
                JSONParser jsonParser = new JSONParser();
                jsonObj = (JSONObject) jsonParser.parse(receiveMsg);
                
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
        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObj;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.dao.AlertDao#getApiServer()
	 */
	@Override
	public JSONObject getApiServer() {
		// TODO Auto-generated method stub
		
		JSONObject jsonObj = new JSONObject();
		
		try {
			String addr = "http://prometheus.zcp-dev.jp-tok.containers.mybluemix.net/api/v1/query?query=up{job=%22kubernetes-apiservers%22}";
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
                
                JSONParser jsonParser = new JSONParser();
                jsonObj = (JSONObject) jsonParser.parse(receiveMsg);
                
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
        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return jsonObj;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.dao.AlertDao#getNodeNotReady()
	 */
	@Override
	public JSONObject getNodeNotReady() {
		// TODO Auto-generated method stub
		
		JSONObject jsonObj = new JSONObject();
		
		try {
			String addr = "http://prometheus.zcp-dev.jp-tok.containers.mybluemix.net/api/v1/query?query=count(kube_node_status_condition{condition=%22Ready%22,status=%22true%22}==0)";
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
                
                JSONParser jsonParser = new JSONParser();
                jsonObj = (JSONObject) jsonParser.parse(receiveMsg);
                
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
        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObj;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.dao.AlertDao#getNodeDown()
	 */
	@Override
	public JSONObject getNodeDown() {
		// TODO Auto-generated method stub
		
		JSONObject jsonObj = new JSONObject();
		
		try {
			String addr = "http://prometheus.zcp-dev.jp-tok.containers.mybluemix.net/api/v1/query?query=count(up{component=%22node-exporter%22,job=%22kubernetes-monitoring-endpoints%22}==0)";
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
                
                JSONParser jsonParser = new JSONParser();
                jsonObj = (JSONObject) jsonParser.parse(receiveMsg);
                
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
        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObj;
	}
	
	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.dao.AlertDao#getAlertListDao()
	 */
	@Override
	public JSONObject getAlertList() {
		// TODO Auto-generated method stub
		
		JSONObject jsonObj = new JSONObject();
		
		try {
			String addr = "http://alertmanager.zcp-dev.jp-tok.containers.mybluemix.net/api/v1/alerts";
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
                
                JSONParser jsonParser = new JSONParser();
                jsonObj = (JSONObject) jsonParser.parse(receiveMsg);
                
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
        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObj;
	}
	
}
