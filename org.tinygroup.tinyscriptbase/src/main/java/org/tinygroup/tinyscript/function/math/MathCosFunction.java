package org.tinygroup.tinyscript.function.math;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

public class MathCosFunction extends AbstractMathCollectionFunction{

	public String getNames() {
		return "cos";
	}

	protected int getParameterCount() {
		return 1;
	}

	protected Object computeItem(Object... parameters) throws ScriptException {
		return Math.cos(ExpressionUtil.convertDouble(parameters[0]));
	}

}
