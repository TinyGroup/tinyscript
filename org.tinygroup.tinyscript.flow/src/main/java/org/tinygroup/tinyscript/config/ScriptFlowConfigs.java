package org.tinygroup.tinyscript.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("flows")
public class ScriptFlowConfigs {

	@XStreamImplicit
	private List<ScriptFlowConfig> flowList;

	public List<ScriptFlowConfig> getFlowList() {
		return flowList;
	}

	public void setFlowList(List<ScriptFlowConfig> flowList) {
		this.flowList = flowList;
	}
	
}
