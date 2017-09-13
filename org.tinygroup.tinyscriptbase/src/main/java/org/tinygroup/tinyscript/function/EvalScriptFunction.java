package org.tinygroup.tinyscript.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

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
				throw new ScriptException(ResourceBundleUtil.getMessage("function.parameter.empty", getNames())); 
			} else if (checkParameters(parameters, 1)) {
				if (parameters[0] instanceof String) {
					String script = (String) parameters[0];
					return getScriptEngine().execute(convertExpression(script), context);
				}
				LambdaFunction pruneFunction = (LambdaFunction) parameters[0];
				return pruneFunction.execute(context).getResult();
			} else {
				throw new ScriptException(ResourceBundleUtil.getMessage("function.parameter.error", getNames())); 
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getMessage("function.run.error", getNames()),e); 
		}
	}

}
