package org.tinygroup.tinyscript.assignvalue;

import org.tinygroup.tinyscript.ScriptContext;

/**
 * 赋值操作处理器
 * @author yancheng11334
 *
 */
public interface AssignValueProcessor {

	/**
	 * 判断是否处理
	 * @param name
	 * @param context
	 * @return
	 */
	boolean isMatch(String name,ScriptContext context);
	
	/**
	 * 执行赋值处理逻辑
	 * @param name
	 * @param value
	 * @param context
	 * @throws Exception
	 */
	void process(String name,Object value,ScriptContext context) throws Exception;
}
