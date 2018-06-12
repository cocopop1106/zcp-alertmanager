package com.skcc.cloudz.zcp.common.yamlbeans.document;

import java.io.IOException;

import com.skcc.cloudz.zcp.common.yamlbeans.YamlConfig.WriteConfig;
import com.skcc.cloudz.zcp.common.yamlbeans.emitter.Emitter;
import com.skcc.cloudz.zcp.common.yamlbeans.emitter.EmitterException;
import com.skcc.cloudz.zcp.common.yamlbeans.parser.ScalarEvent;

public class YamlScalar extends YamlElement {

	String value;
	
	public YamlScalar() {
	}
	
	public YamlScalar(Object value) {
		this.value = String.valueOf(value);
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(anchor!=null) {
			sb.append('&');
			sb.append(anchor);
			sb.append(' ');
		}
		sb.append(value);
		if(tag!=null) {
			sb.append(" !");
			sb.append(tag);
		}
		return sb.toString();
	}
	
	@Override
	public void emitEvent(Emitter emitter, WriteConfig config) throws EmitterException, IOException {
		char style = 0; // TODO determine style
		emitter.emit(new ScalarEvent(anchor, tag, new boolean[] {true, true}, value, style));
	}
}
