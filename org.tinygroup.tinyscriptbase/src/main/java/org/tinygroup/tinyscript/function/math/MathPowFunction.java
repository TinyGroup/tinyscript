package org.tinygroup.tinyscript.function.math;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

public class MathPowFunction extends AbstractMathCollectionFunction{

	public String getNames() {
		return "pow";
	}

	protected int getParameterCount() {
		return 2;
	}

	protected Object computeItem(Object... parameters) throws ScriptException {
		Double a = ExpressionUtil.convertDouble(parameters[0]);
		Double b = ExpressionUtil.convertDouble(parameters[1]);
		return Math.pow(a, b);
	}

}
