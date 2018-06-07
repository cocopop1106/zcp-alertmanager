package com.skcc.cloudz.zcp.example;

import java.io.IOException;

import org.apache.log4j.Logger;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1ConfigMap;
import io.kubernetes.client.models.V1ConfigMapList;
import io.kubernetes.client.util.Config;

public class ListConfigMapForAllNamespaces {
	private static Logger logger = Logger.getLogger(ListConfigMapForAllNamespaces.class);
	
	public static void main(String[] args) throws IOException, ApiException {
	    ApiClient client = Config.defaultClient();
	    Configuration.setDefaultApiClient(client);

	    CoreV1Api api = new CoreV1Api();
	    V1ConfigMapList list =
	        api.listConfigMapForAllNamespaces(null, null, null, null, null, null, null, null, null);
	    for (V1ConfigMap item : list.getItems()) {
	      logger.info(item.getMetadata().getName());
	    }
	  }
}
