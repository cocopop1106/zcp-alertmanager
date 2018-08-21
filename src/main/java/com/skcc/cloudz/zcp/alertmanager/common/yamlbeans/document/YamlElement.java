package com.skcc.cloudz.zcp.alertmanager.common.yamlbeans.document;

import java.io.IOException;

import com.skcc.cloudz.zcp.alertmanager.common.yamlbeans.YamlConfig.WriteConfig;
import com.skcc.cloudz.zcp.alertmanager.common.yamlbeans.emitter.Emitter;
import com.skcc.cloudz.zcp.alertmanager.common.yamlbeans.emitter.EmitterException;

public abstract class YamlElement {

	String tag;
	String anchor;
	
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}
	
	public String getTag() {
		return tag;
	}
	
	public String getAnchor() {
		return anchor;
	}

	public abstract void emitEvent(Emitter emitter, WriteConfig config) throws EmitterException, IOException;
}
