package com.skcc.cloudz.zcp.alert.vo;

public class ChannelDtlVo {
	
	private String id;
	private String channel;
	private String notifications;
	private String email;
	private String api_url;
	private String room_id;
	private String auth_token;
	private String send_resolved;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}
	/**
	 * @param channel the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}
	/**
	 * @return the notifications
	 */
	public String getNotifications() {
		return notifications;
	}
	/**
	 * @param notifications the notifications to set
	 */
	public void setNotifications(String notifications) {
		this.notifications = notifications;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the api_url
	 */
	public String getApi_url() {
		return api_url;
	}
	/**
	 * @param api_url the api_url to set
	 */
	public void setApi_url(String api_url) {
		this.api_url = api_url;
	}
	/**
	 * @return the room_id
	 */
	public String getRoom_id() {
		return room_id;
	}
	/**
	 * @param room_id the room_id to set
	 */
	public void setRoom_id(String room_id) {
		this.room_id = room_id;
	}
	/**
	 * @return the auth_token
	 */
	public String getAuth_token() {
		return auth_token;
	}
	/**
	 * @param auth_token the auth_token to set
	 */
	public void setAuth_token(String auth_token) {
		this.auth_token = auth_token;
	}
	/**
	 * @return the send_resolved
	 */
	public String getSend_resolved() {
		return send_resolved;
	}
	/**
	 * @param send_resolved the send_resolved to set
	 */
	public void setSend_resolved(String send_resolved) {
		this.send_resolved = send_resolved;
	}
	
	
}
