package org.tinygroup.tinyscript.expression;

/**
 * 可扩展的类型转换处理器
 * @author yancheng11334
 *
 */
public interface TypeConvertProcessor {

	/**
	 * 返回转换名称
	 * @return
	 */
	String  getName();
	
	/**
	 * 执行转换逻辑
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	Object convert(Object... parameters) throws Exception;
}
