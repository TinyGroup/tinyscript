package org.tinygroup.tinyscript.function;

/**
 * 动态名称函数(支持一组业务行为相同，但名称不同的函数)
 * @author yancheng11334
 *
 */
public abstract class DynamicNameScriptFunction extends AbstractScriptFunction{

	/**
	 * 没有固定名称,所以返回null
	 */
	public String getNames() {
		return null;
	}
	
	/**
	 * 动态函数是否包含某名称业务行为
	 * @param name
	 * @return
	 */
	public abstract boolean exsitFunctionName(String name);

}
