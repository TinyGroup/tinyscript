package org.tinygroup.tinyscript.config;

/**
 * 函数参数配置项
 * @author yancheng11334
 *
 */
public interface ParameterConfig {

	/**
	 * 获取参数类型
	 * @return
	 */
	String getParameterType();
	
	/**
	 * 是否数组或集合类型
	 * @return
	 */
	boolean isArray();
	
	/**
	 * 是否允许空值存在
	 * @return
	 */
	boolean allowNull();
	
	/**
	 * 是否动态参数
	 * @return
	 */
	boolean isDynamic();
	
	/**
	 * 获取参数描述
	 * @return
	 */
	String getDescription();
}
