package com.skcc.cloudz.zcp.manager;

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

@Component
public class PrometheusManager {
	private static Logger logger = Logger.getLogger(PrometheusManager.class);
	private String str, receiveMsg;

	@Value("${props.prometheus.baseUrl}")
	private String baseUrl;

	@SuppressWarnings("static-access")
	public JSONObject getAlertCount() {
		JSONObject jsonObj = new JSONObject();

		try {
			String addr = baseUrl + "/api/v1/query?query=alertmanager_alerts{state=%22active%22}";
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return jsonObj;
	}

	@SuppressWarnings("static-access")
	public JSONObject getApiServer() {
		JSONObject jsonObj = new JSONObject();

		try {
			String addr = baseUrl + "/api/v1/query?query=up{job=%22kubernetes-apiservers%22}";
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return jsonObj;
	}

	@SuppressWarnings("static-access")
	public JSONObject getNodeNotReadyCnt() {
		JSONObject jsonObj = new JSONObject();

		try {
			String addr = baseUrl
					+ "/api/v1/query?query=count(kube_node_status_condition{condition=%22Ready%22,status=%22true%22}==0)";
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return jsonObj;
	}

	@SuppressWarnings("static-access")
	public JSONObject getNodeNotReadyTotCnt() {
		JSONObject jsonObj = new JSONObject();

		try {
			String addr = baseUrl
					+ "/api/v1/query?query=count(kube_node_status_condition{condition=%22Ready%22,status=%22true%22}==1)";
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return jsonObj;
	}

	@SuppressWarnings("static-access")
	public JSONObject getNodeDownCnt() {
		JSONObject jsonObj = new JSONObject();

		try {
			String addr = baseUrl
					+ "/api/v1/query?query=count(up{component=%22node-exporter%22,job=%22kubernetes-monitoring-endpoints%22}==0)";
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}

	@SuppressWarnings("static-access")
	public JSONObject getNodeDownTotCnt() {
		JSONObject jsonObj = new JSONObject();

		try {
			String addr = baseUrl
					+ "/api/v1/query?query=count(up{component=%22node-exporter%22,job=%22kubernetes-monitoring-endpoints%22}==1)";
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}

}

