package org.tinygroup.tinyscript.interpret;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;

/**
 * 方法调度扩展接口
 * @author yancheng11334
 *
 */
public interface FunctionCallProcessor {

	/**
	 * 是否支持表达式参数
	 * @return
	 */
	boolean  enableExpressionParameter();
	
	/**
	 * 执行方法
	 * @param segment
	 * @param context
	 * @param object
	 * @param methodName
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	Object invoke(ScriptSegment segment,
			ScriptContext context, Object object, String methodName,
			Object... parameters) throws Exception;
}
