package org.tinygroup.tinyscript.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 脚本组件配置项
 * @author yancheng11334
 *
 */
@XStreamAlias("component")
public class ScriptComponentConfig {

	@XStreamAsAttribute
	private String id;
	
	@XStreamAsAttribute
	private String name;
	
	private String script;
	
	private String description;
	
	@XStreamImplicit
	private List<ScriptInputParameterConfig> inputList;
	
	@XStreamImplicit
	private List<ScriptOutputParameterConfig> outputList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public List<ScriptInputParameterConfig> getInputList() {
		return inputList;
	}

	public void setInputList(List<ScriptInputParameterConfig> inputList) {
		this.inputList = inputList;
	}

	public List<ScriptOutputParameterConfig> getOutputList() {
		return outputList;
	}

	public void setOutputList(List<ScriptOutputParameterConfig> outputList) {
		this.outputList = outputList;
	}
	
}
