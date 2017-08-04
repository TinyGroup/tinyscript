package org.tinygroup.tinyscript.function.math;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

public class MathAtanFunction extends AbstractMathCollectionFunction{

	public String getNames() {
		return "atan";
	}

	protected int getParameterCount() {
		return 1;
	}

	protected Object computeItem(Object... parameters) throws ScriptException {
		return Math.atan(ExpressionUtil.convertDouble(parameters[0]));
	}

}
