package org.tinygroup.tinyscript.interpret.anonymous;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;

/**
 * 单方法接口处理器
 * @author yancheng11334
 *
 * @param <T>
 */
public interface SingleMethodProcessor<T>{
	
	Class<?> getType();
	
	T build(LambdaFunction lambdaFunction,
			ScriptContext scriptContext);
}
