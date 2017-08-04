package org.tinygroup.tinyscript.interpret;

import org.tinygroup.tinyscript.ScriptContext;

/**
 * lambda函数
 * @author yancheng11334
 *
 */
public interface LambdaFunction {

	String  getFunctionName();
	
	String[] getParamterNames();
	
	ScriptResult execute(ScriptContext context, Object... parameters) throws Exception;
}
