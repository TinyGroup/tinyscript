package org.tinygroup.tinyscript.config;

/**
 * 函数结果定义项
 * @author yancheng11334
 *
 */
public interface ResultConfig {

	/**
	 * 是否无返回值
	 * @return
	 */
	boolean isVoid();
	
	/**
	 * 获取参数类型
	 * @return
	 */
	String getResultType();
	
	/**
	 * 是否数组或集合类型
	 * @return
	 */
	boolean isArray();
}
