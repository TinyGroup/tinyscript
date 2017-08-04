package org.tinygroup.tinyscript.interpret.call;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;
import org.tinygroup.tinyscript.interpret.exception.NotMatchException;

/**
 * 调用非匿名lambda函数
 * @author yancheng11334
 *
 */
public class LambdaFunctionProcessor extends AbstractMethodProcessor{

	protected Object invokeMethod(ScriptSegment segment, ScriptContext context,
			Object object, String methodName, Object... parameters)
			throws Exception {
		
		LambdaFunction lambdaFunction = ScriptContextUtil.getLambdaFunction(context, methodName);
	    if(lambdaFunction!=null){
	       return lambdaFunction.execute(context, parameters);
	    }
		//抛出不匹配信息
	    throw new NotMatchException();
	}

}
