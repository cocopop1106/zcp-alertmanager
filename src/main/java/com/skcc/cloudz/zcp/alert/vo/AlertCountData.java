package com.skcc.cloudz.zcp.alert.vo;

public class AlertCountData {
	
	private String status;
	private String resultType;
	private String __name__;
	private String component;
	private String instance;
	private String job;
	private String kubernetes_name;
	private String kubernetes_namespace;
	private String state;
	private String[] value;
	
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the resultType
	 */
	public String getResultType() {
		return resultType;
	}
	/**
	 * @param resultType the resultType to set
	 */
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	/**
	 * @return the __name__
	 */
	public String get__name__() {
		return __name__;
	}
	/**
	 * @param __name__ the __name__ to set
	 */
	public void set__name__(String __name__) {
		this.__name__ = __name__;
	}
	/**
	 * @return the component
	 */
	public String getComponent() {
		return component;
	}
	/**
	 * @param component the component to set
	 */
	public void setComponent(String component) {
		this.component = component;
	}
	/**
	 * @return the instance
	 */
	public String getInstance() {
		return instance;
	}
	/**
	 * @param instance the instance to set
	 */
	public void setInstance(String instance) {
		this.instance = instance;
	}
	/**
	 * @return the job
	 */
	public String getJob() {
		return job;
	}
	/**
	 * @param job the job to set
	 */
	public void setJob(String job) {
		this.job = job;
	}
	/**
	 * @return the kubernetes_name
	 */
	public String getKubernetes_name() {
		return kubernetes_name;
	}
	/**
	 * @param kubernetes_name the kubernetes_name to set
	 */
	public void setKubernetes_name(String kubernetes_name) {
		this.kubernetes_name = kubernetes_name;
	}
	/**
	 * @return the kubernetes_namespace
	 */
	public String getKubernetes_namespace() {
		return kubernetes_namespace;
	}
	/**
	 * @param kubernetes_namespace the kubernetes_namespace to set
	 */
	public void setKubernetes_namespace(String kubernetes_namespace) {
		this.kubernetes_namespace = kubernetes_namespace;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the value
	 */
	public String[] getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String[] value) {
		this.value = value;
	}
	
	

}
