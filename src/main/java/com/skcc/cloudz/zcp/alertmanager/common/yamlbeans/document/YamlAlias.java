package com.skcc.cloudz.zcp.alertmanager.common.yamlbeans.document;

import java.io.IOException;

import com.skcc.cloudz.zcp.alertmanager.common.yamlbeans.YamlConfig.WriteConfig;
import com.skcc.cloudz.zcp.alertmanager.common.yamlbeans.emitter.Emitter;
import com.skcc.cloudz.zcp.alertmanager.common.yamlbeans.emitter.EmitterException;
import com.skcc.cloudz.zcp.alertmanager.common.yamlbeans.parser.AliasEvent;

public class YamlAlias extends YamlElement {

	@Override
	public void emitEvent(Emitter emitter, WriteConfig config) throws EmitterException, IOException {
		emitter.emit(new AliasEvent(anchor));
	}
	
	@Override
	public String toString() {
		return "*" + anchor;
	}
}
