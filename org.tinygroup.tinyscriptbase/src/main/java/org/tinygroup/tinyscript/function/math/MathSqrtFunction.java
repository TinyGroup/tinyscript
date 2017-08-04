package org.tinygroup.tinyscript.function.math;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

public class MathSqrtFunction extends AbstractMathCollectionFunction{

	public String getNames() {
		return "sqrt";
	}

	protected int getParameterCount() {
		return 1;
	}

	protected Object computeItem(Object... parameters) throws ScriptException {
		Double value = ExpressionUtil.convertDouble(parameters[0]);
		return Math.sqrt(value);
	}

}
