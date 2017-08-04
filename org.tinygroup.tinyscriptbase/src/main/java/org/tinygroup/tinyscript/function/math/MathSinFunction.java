package org.tinygroup.tinyscript.function.math;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

public class MathSinFunction extends AbstractMathCollectionFunction{

	public String getNames() {
		return "sin";
	}

	protected int getParameterCount() {
		return 1;
	}

	protected Object computeItem(Object... parameters) throws ScriptException {
		return Math.sin(ExpressionUtil.convertDouble(parameters[0]));
	}

}
