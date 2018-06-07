package com.skcc.cloudz.zcp.example;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1ConfigMap;
import io.kubernetes.client.util.Config;

public class ReplaceNamespacedConfigMap {
	private static Logger logger = Logger.getLogger(ReplaceNamespacedConfigMap.class);

	public static void main(String[] args) throws IOException, ApiException {
		ApiClient client = Config.defaultClient();
		Configuration.setDefaultApiClient(client);

		CoreV1Api api = new CoreV1Api();
		V1ConfigMap body = new V1ConfigMap();
		V1ConfigMap configMap = api.replaceNamespacedConfigMap("test-rules", "monitoring", body, null);
		
		logger.info(configMap.toString());
	}
}
