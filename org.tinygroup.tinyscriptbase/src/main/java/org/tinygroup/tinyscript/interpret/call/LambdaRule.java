package org.tinygroup.tinyscript.interpret.call;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.interpret.ClassInstanceUtil;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.anonymous.SingleMethodProcessor;

/**
 * lambda转换规则
 * @author yancheng11334
 *
 */
public class LambdaRule implements MethodParameterRule{

	public boolean isMatch(Class<?> parameterType, Object parameter) {
		return parameter instanceof LambdaFunction && ClassInstanceUtil.findSingleMethodProcessor(parameterType)!=null;
	}

	public Object convert(ScriptContext context,Class<?> parameterType, Object parameter) {
		LambdaFunction lambdaFunction = (LambdaFunction) parameter;
		SingleMethodProcessor processor = ClassInstanceUtil.findSingleMethodProcessor(parameterType);
		return processor.build(lambdaFunction, context);
	}

}
