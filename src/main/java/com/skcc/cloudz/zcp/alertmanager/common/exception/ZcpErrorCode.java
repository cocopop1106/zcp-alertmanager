package com.skcc.cloudz.zcp.alertmanager.common.exception;

public enum ZcpErrorCode {
	
	//alert
	GET_ALERT_COUNT_ERROR							(21001, "error : Get alert count"),
	GET_API_SERVER_ERROR							(21002, "error : Get api server"),
	GET_NODE_NOT_READY_ERROR							(21003, "error : Get node not ready"),
	GET_NODE_NOT_READY_TOT_CNT_ERROR							(21004, "error : Get node not ready total count"),
	GET_NODE_DOWN_CNT_ERROR							(21005, "error : Get node down count"),
	GET_NODE_DOWN_TOT_CNT_ERROR							(21006, "error : Get node down total count"),
	GET_ALERT_LIST_ERROR							(21007, "error : Get alert list"),
	GET_ALERT_HISTORY_LIST_ERROR							(21008, "error : Get alert history list"),
	
	//channel
	GET_CHANNEL_LIST_ERROR							(22001, "error : Get channel list"),
	GET_CHANNEL_DETAIL_ERROR							(22002, "error : Get channel detail"),
	CREATE_CHANNEL_ERROR							(22003, "error : Create channel"),
	DELETE_CHANNEL_ERROR							(22004, "error : Delete channel"),
	UPDATE_CHANNEL_ERROR							(22005, "error : Update channel"),
	GET_REPEAT_INTERVAL_ERROR							(22006, "error : Get repeat interval"),
	UPDATE_REPEAT_INTERVAL_ERROR							(22007, "error : Update repeat interval"),
	UPDATE_CHANNEL_NAME_ERROR							(22008, "error : Update channel name"),
	DELETE_NOTIFICATION_ERROR							(22009, "error : Delete Notification"),
	
	//rule
	GET_RULE_LIST_ERROR							(23001, "error : Get rule list"),
	GET_NOTIFICATION_ERROR							(23002, "error : Get notification"),
	GET_RULE_DETAIL_ERROR							(23003, "error : Get rule detail"),
	CREATE_RULE_ERROR							(23004, "error : Create rule"),
	UPDATE_RULE_ERROR							(23005, "error : Update rule"),
	DELETE_RULE_ERROR							(23006, "error : Delete rule"),
	
	//common
	UNKNOWN_ERROR						            (11001, "Unknown error"),
	KUBERNETES_UNKNOWN_ERROR			            (11002, "Unknown kobernetes error"),
	KEYCLOAK_UNKNOWN_ERROR				            (11003, "Unknown keycloak error"),
	
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
