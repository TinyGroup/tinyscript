package org.tinygroup.tinyscript.function.math;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

public class MathAsinFunction extends AbstractMathCollectionFunction{

	public String getNames() {
		return "asin";
	}

	protected int getParameterCount() {
		return 1;
	}

	protected Object computeItem(Object... parameters) throws ScriptException {
		return Math.asin(ExpressionUtil.convertDouble(parameters[0]));
	}

}
