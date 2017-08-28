package org.tinygroup.tinyscript.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.LambdaFunction;

/**
 * 动态执行脚本
 * 
 * @author yancheng11334
 *
 */
public class EvalScriptFunction extends AbstractScriptFunction {

	public String getNames() {
		return "eval";
	}

	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(String.format("%s函数的参数为空!", getNames()));
			} else if (checkParameters(parameters, 1)) {
				if (parameters[0] instanceof String) {
					String script = (String) parameters[0];
					return getScriptEngine().execute(convertExpression(script), context);
				}

				LambdaFunction pruneFunction = (LambdaFunction) parameters[0];
				return pruneFunction.execute(context).getResult();
			} else {
				throw new ScriptException(String.format("%函数的参数格式不正确!", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(String.format("%s函数执行发生异常:", getNames()), e);
		}
	}

}
