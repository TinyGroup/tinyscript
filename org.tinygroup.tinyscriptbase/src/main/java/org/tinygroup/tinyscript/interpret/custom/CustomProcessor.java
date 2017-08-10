package org.tinygroup.tinyscript.interpret.custom;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;

/**
 * 用户自定义规则执行器
 * @author yancheng11334
 *
 */
public interface CustomProcessor {

	/**
	 * 判断处理器是否匹配当前对象
	 * @param sqlObj
	 * @return
	 */
	boolean isMatch(Object sqlObj);
	
	/**
	 * 执行规则并返回结果
	 * @param customObj
	 * @param customRule
	 * @param context
	 * @return
	 * @throws ScriptException
	 */
	Object executeRule(Object customObj,String customRule,ScriptContext context) throws ScriptException;
}
