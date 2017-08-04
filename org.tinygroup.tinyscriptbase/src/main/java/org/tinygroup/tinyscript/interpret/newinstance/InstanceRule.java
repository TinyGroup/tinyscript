package org.tinygroup.tinyscript.interpret.newinstance;

import org.tinygroup.tinyscript.ScriptContext;

public class InstanceRule implements ConstructorParameterRule{

	public boolean isMatch(Class<?> parameterType, Object parameter) {
		return parameter!=null && parameterType.isInstance(parameter);
	}

	public Object convert(ScriptContext context, Class<?> parameterType,
			Object parameter) {
		return parameter;
	}

}
