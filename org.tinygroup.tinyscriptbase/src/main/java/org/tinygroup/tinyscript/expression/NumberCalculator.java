package org.tinygroup.tinyscript.expression;

import java.util.List;

import org.tinygroup.tinyscript.ScriptException;

/**
 * 数值计算器
 * @author yancheng11334
 *
 */
public interface NumberCalculator {

	/**
	 * 处理器名
	 * @return
	 */
	String getName();
	
	/**
	 * 执行计算
	 * @param numbers
	 * @return
	 * @throws ScriptException
	 */
	Object compute(List<Object> numbers) throws ScriptException;
	
	/**
	 * 返回empty的处理值
	 * @return
	 */
	Object getEmptyValue();
	
}
