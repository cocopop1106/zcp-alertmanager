package com.skcc.cloudz.zcp.common.util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.ApiResponse;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.Pair;
import io.kubernetes.client.ProgressRequestBody;
import io.kubernetes.client.ProgressResponseBody;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Endpoints;
import io.kubernetes.client.util.Config;

public class KubeClient<T> extends CoreV1Api{
	
	KubeApi api;
	private ApiClient apiClient;
	
	public KubeClient(ApiClient apiClient) {
		this.apiClient = apiClient;
	}
	
	public Object ctlData(KubeApi api) throws ApiException {
		return api.getData();
	}
	
		
	//@Override
	public ApiResponse<T> apiCall(
			//Class clazz
			String localVarPath
			, String _continue
			, String fieldSelector
			, Boolean includeUninitialized
			, String labelSelector
			, Integer limit
			, String pretty
			, String resourceVersion
			, Integer timeoutSeconds
			, Boolean watch
			, final ProgressResponseBody.ProgressListener progressListener
			, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
		
        Object localVarPostBody = null;

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (_continue != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("continue", _continue));
        if (fieldSelector != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("fieldSelector", fieldSelector));
        if (includeUninitialized != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("includeUninitialized", includeUninitialized));
        if (labelSelector != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("labelSelector", labelSelector));
        if (limit != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("limit", limit));
        if (pretty != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("pretty", pretty));
        if (resourceVersion != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("resourceVersion", resourceVersion));
        if (timeoutSeconds != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("timeoutSeconds", timeoutSeconds));
        if (watch != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("watch", watch));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json", "application/yaml", "application/vnd.kubernetes.protobuf", "application/json;stream=watch", "application/vnd.kubernetes.protobuf;stream=watch"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "*/*"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        

        String[] localVarAuthNames = new String[] { "BearerToken" };
        Call call = apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, null);
        
        Type localVarReturnType = new TypeToken<T>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }
	
	
	@SuppressWarnings("rawtypes")
    public ApiResponse<T> postApiCall(
    		String localVarPath
    		, Object body
    		, String pretty
    		, final ProgressResponseBody.ProgressListener progressListener
    		, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
		Object localVarPostBody = body;
		
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling ");
        }
        
        
     // create path and map variables
        
        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (pretty != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("pretty", pretty));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json", "application/yaml", "application/vnd.kubernetes.protobuf", "application/json;stream=watch", "application/vnd.kubernetes.protobuf;stream=watch"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "*/*"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        

        String[] localVarAuthNames = new String[] { "BearerToken" };
        Call call = apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, null);
        
        Type localVarReturnType = new TypeToken<T>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
        
    }

}
