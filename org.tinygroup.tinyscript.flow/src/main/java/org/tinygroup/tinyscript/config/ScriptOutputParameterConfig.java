package org.tinygroup.tinyscript.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 输出参数配置项
 * @author yancheng11334
 *
 */
@XStreamAlias("output")
public class ScriptOutputParameterConfig {
	
	@XStreamAsAttribute
	private String type;
	
	private String description;
	
	@XStreamAsAttribute
	private String nullable;

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

	public String getNullable() {
		return nullable;
	}

	public void setNullable(String nullable) {
		this.nullable = nullable;
	}

	public String toString() {
		return "ScriptOutputParameterConfig [type=" + type + ", description="
				+ description + ", nullable=" + nullable + "]";
	}
	
	
}
