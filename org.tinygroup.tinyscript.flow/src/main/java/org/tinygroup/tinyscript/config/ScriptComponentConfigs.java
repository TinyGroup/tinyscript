package org.tinygroup.tinyscript.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("components")
public class ScriptComponentConfigs {

	@XStreamImplicit
	private List<ScriptComponentConfig>  componentList;

	public List<ScriptComponentConfig> getComponentList() {
		return componentList;
	}

	public void setComponentList(List<ScriptComponentConfig> componentList) {
		this.componentList = componentList;
	}
	
}
