package org.tinygroup.tinyscript.config;

import java.util.List;

/**
 * 抽象函数方法类实现
 * @author yancheng11334
 *
 */
public abstract class AbstractFunctionConfig implements FunctionConfig{

	private String name;
	
	private String type;

	public AbstractFunctionConfig(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getFunctionType() {
		return type;
	}

	@Override
	public List<ParameterConfig> getParameters() {
		// TODO 暂不实现
		return null;
	}

	@Override
	public ResultConfig getResult() {
		// TODO 暂不实现
		return null;
	}
	
	public int compareTo(FunctionConfig o) {
		//默认按函数名进行排序
		return getName().compareTo(o.getName());
	}
	
	public String toString() {
		return "FunctionConfig [name=" + getName()
				+ ", functionType=" + getFunctionType() + ", description="
				+ getDescription() + ", parameters=" + getParameters()
				+ ", result=" + getResult() + "]";
	}

}
