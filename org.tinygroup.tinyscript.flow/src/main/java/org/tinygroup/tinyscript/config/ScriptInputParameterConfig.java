package org.tinygroup.tinyscript.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 输入参数配置项
 * @author yancheng11334
 *
 */
@XStreamAlias("input")
public class ScriptInputParameterConfig {

	@XStreamAsAttribute
	private String name;
	
	@XStreamAsAttribute
	private String type;
	
	private String description;
	
	@XStreamAlias("default-value")
	private String defaultValue;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String toString() {
		return "ScriptInputParameterConfig [name=" + name + ", type=" + type
				+ ", description=" + description + ", defaultValue="
				+ defaultValue + "]";
	}
	
	
}
