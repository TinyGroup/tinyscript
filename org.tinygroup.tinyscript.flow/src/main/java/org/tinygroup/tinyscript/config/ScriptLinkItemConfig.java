package org.tinygroup.tinyscript.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 脚本关联子项
 * @author yancheng11334
 *
 */
@XStreamAlias("item")
public class ScriptLinkItemConfig {

	/**
	 * 关联类型
	 */
	@XStreamAsAttribute
	private String type;
	
	/**
	 * 环境变量
	 */
	@XStreamAsAttribute
	@XStreamAlias("env-name")
	private String envName;
	
	/**
	 * 结果下标
	 */
	@XStreamAsAttribute
	private int order;
	
	/**
	 * 对应参数名
	 */
	@XStreamAsAttribute
	@XStreamAlias("parameter-name")
	private String parameterName;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	
}
