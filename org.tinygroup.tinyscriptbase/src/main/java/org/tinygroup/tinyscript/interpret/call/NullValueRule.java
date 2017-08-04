package org.tinygroup.tinyscript.interpret.call;

import org.tinygroup.tinyscript.ScriptContext;

/**
 * 空值匹配规则
 * @author yancheng11334
 *
 */
public class NullValueRule implements MethodParameterRule{

	public boolean isMatch(Class<?> parameterType, Object parameter) {
		return parameter==null;
	}

	public Object convert(ScriptContext context,Class<?> parameterType, Object parameter) {
		return null;
	}

}
