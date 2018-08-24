package com.skcc.cloudz.zcp.alertmanager.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;


@Component
public class AlertManager {
	private static Logger logger = Logger.getLogger(AlertManager.class);
	private String str, receiveMsg;
	
	@Value("${props.alertManager.baseUrl}")
    private String baseUrl;

	@SuppressWarnings("static-access")
	public JSONObject getAlertList() throws Exception {
		JSONObject jsonObj = new JSONObject();

		try {
			String addr = UriComponentsBuilder.fromUriString(baseUrl).path("/api/v1/alerts").build().toString();
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
				logger.info(conn.getResponseCode());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}

}
