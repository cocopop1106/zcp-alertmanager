package com.skcc.cloudz.zcp.alertmanager.common.exception;

public enum ZcpErrorCode {
	
	//common
	UNKNOWN_ERROR						            (10000, "Unknown error"),
	KUBERNETES_UNKNOWN_ERROR			            (10001, "Unknown kobernetes error"),
	KEYCLOAK_UNKNOWN_ERROR				            (10002, "Unknown keycloak error"),
		
	//alert
	GET_ALERT_COUNT_ERROR							(20001, "error : Get alert count"),
	GET_API_SERVER_ERROR							(20002, "error : Get api server"),
	GET_NODE_NOT_READY_ERROR							(20003, "error : Get node not ready"),
	GET_NODE_NOT_READY_TOT_CNT_ERROR							(20004, "error : Get node not ready total count"),
	GET_NODE_DOWN_CNT_ERROR							(20005, "error : Get node down count"),
	GET_NODE_DOWN_TOT_CNT_ERROR							(20006, "error : Get node down total count"),
	GET_ALERT_LIST_ERROR							(20007, "error : Get alert list"),
	GET_ALERT_HISTORY_LIST_ERROR							(20008, "error : Get alert history list"),
	
	//channel
	GET_CHANNEL_LIST_ERROR							(30001, "error : Get channel list"),
	GET_CHANNEL_DETAIL_ERROR							(30002, "error : Get channel detail"),
	CREATE_CHANNEL_ERROR							(30003, "error : Create channel"),
	DELETE_CHANNEL_ERROR							(30004, "error : Delete channel"),
	UPDATE_CHANNEL_ERROR							(30005, "error : Update channel"),
	GET_REPEAT_INTERVAL_ERROR							(30006, "error : Get repeat interval"),
	UPDATE_REPEAT_INTERVAL_ERROR							(30007, "error : Update repeat interval"),
	UPDATE_CHANNEL_NAME_ERROR							(30008, "error : Update channel name"),
	DELETE_NOTIFICATION_ERROR							(30009, "error : Delete Notification"),
	
	//rule
	GET_RULE_LIST_ERROR							(40001, "error : Get rule list"),
	GET_NOTIFICATION_ERROR							(40002, "error : Get notification"),
	GET_RULE_DETAIL_ERROR							(40003, "error : Get rule detail"),
	CREATE_RULE_ERROR							(40004, "error : Create rule"),
	UPDATE_RULE_ERROR							(40005, "error : Update rule"),
	DELETE_RULE_ERROR							(40005, "error : Delete rule"),
	
	;

	
	
	
	private int code;
	private String message;
	
	private ZcpErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}


}
