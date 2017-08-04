package org.tinygroup.tinyscript.interpret.call;

import org.tinygroup.tinyscript.ScriptContext;

/**
 * 判断参数是否是类的实例规则
 * @author yancheng11334
 *
 */
public class InstanceRule implements MethodParameterRule{

	public boolean isMatch(Class<?> parameterType, Object parameter) {
		return parameterType.isInstance(parameter);
	}

	public Object convert(ScriptContext context,Class<?> parameterType, Object parameter) {
		return parameter;
	}

}
