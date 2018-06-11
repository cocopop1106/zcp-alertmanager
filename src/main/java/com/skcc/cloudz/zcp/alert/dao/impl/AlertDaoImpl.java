package com.skcc.cloudz.zcp.alert.dao.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.skcc.cloudz.zcp.alert.dao.AlertDao;
import com.skcc.cloudz.zcp.alert.vo.AlertCountData;
import com.skcc.cloudz.zcp.alert.vo.AlertData;

@Repository("alertDao")
public class AlertDaoImpl implements AlertDao {
	private static Logger logger = Logger.getLogger(AlertDaoImpl.class);
	private String str, receiveMsg;
	

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.dao.AlertDao#getAlertCount()
	 */
	@Override
	public AlertCountData getAlertCount() {
		// TODO Auto-generated method stub
		
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
                logger.debug(receiveMsg);
                System.out.println(receiveMsg);
                
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
		
		return null;
	}

	/* (non-Javadoc)
	 * @see com.skcc.cloudz.zcp.alert.dao.AlertDao#getAlertListDao()
	 */
	@Override
	public List<AlertData> getAlertListDao() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
