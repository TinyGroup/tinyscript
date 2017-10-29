package org.tinygroup.tinyscript.function.math;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.function.DynamicNameScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;

/**
 * 实现math的聚合函数
 * 
 * @author yancheng11334
 *
 */
public class MathAggregateFunction extends DynamicNameScriptFunction {

	public boolean enableExpressionParameter() {
		return true;
	}

	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		String functionName = ScriptContextUtil.getDynamicFunctionName(context);
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(
						ResourceBundleUtil.getDefaultMessage("function.parameter.empty", functionName));
			} else if (checkParameters(parameters, 1)) {
				Object p = getValue(parameters[0]);
				List<Object> parameterList = new ArrayList<Object>();
				if (ExpressionUtil.isCollection(p)) {
					parameterList = ExpressionUtil.convertCollection(p);
				} else {
					parameterList.add(p);
				}
				return ExpressionUtil.compute(functionName, parameterList);
			} else if (parameters.length > 1 && ExpressionUtil.isCollection(getValue(parameters[0]))) {
				Object[] newParams = subArray(parameters, 1);
				return ExpressionUtil.compute(functionName, ExpressionUtil.convertCollection(getValue(parameters[0])),
						newParams);
			} else {
				List<Object> parameterList = new ArrayList<Object>();
				for (Object parameter : parameters) {
					Object v = getValue(parameter);
					if (v != null) {
						parameterList.add(v);
					}
				}
				return ExpressionUtil.compute(functionName, parameterList);
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", functionName),
					e);
		}
	}

	public boolean exsitFunctionName(String name) {
		return ExpressionUtil.getNumberCalculator(name) != null;
	}

	public List<String> getFunctionNames() {
		return ExpressionUtil.getCalculatorNames();
	}

}
