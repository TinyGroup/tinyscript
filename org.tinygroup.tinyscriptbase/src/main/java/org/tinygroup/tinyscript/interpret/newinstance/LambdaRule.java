package org.tinygroup.tinyscript.interpret.newinstance;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.interpret.ClassInstanceUtil;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.anonymous.SingleMethodProcessor;

public class LambdaRule implements ConstructorParameterRule{

	public boolean isMatch(Class<?> parameterType, Object parameter) {
		return parameter!=null && parameter instanceof LambdaFunction && ClassInstanceUtil.findSingleMethodProcessor(parameterType)!=null;
	}

	public Object convert(ScriptContext context,Class<?> parameterType, Object parameter) {
		LambdaFunction lambdaFunction = (LambdaFunction) parameter;
		SingleMethodProcessor processor = ClassInstanceUtil.findSingleMethodProcessor(parameterType);
		return processor.build(lambdaFunction, context);
	}

}
