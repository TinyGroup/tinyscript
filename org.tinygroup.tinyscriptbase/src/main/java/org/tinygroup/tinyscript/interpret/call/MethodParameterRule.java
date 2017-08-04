package org.tinygroup.tinyscript.interpret.call;

import org.tinygroup.tinyscript.ScriptContext;

/**
 * 方法参数规则
 * @author yancheng11334
 *
 */
public interface MethodParameterRule {
    
	/**
	 * 是否匹配
	 * @param parameterType
	 * @param parameter
	 * @return
	 */
	boolean isMatch(Class<?> parameterType,Object parameter);
	
	/**
	 * 转换
	 * @param context
	 * @param parameterType
	 * @param parameter
	 * @return
	 */
	Object  convert(ScriptContext context,Class<?> parameterType,Object parameter);
}
