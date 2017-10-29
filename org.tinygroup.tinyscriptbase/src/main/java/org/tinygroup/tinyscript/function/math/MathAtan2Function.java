package org.tinygroup.tinyscript.function.math;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

public class MathAtan2Function extends AbstractMathCollectionFunction {
	public String getNames() {
		return "atan2";
	}

	protected int getParameterCount() {
		return 2;
	}

	protected Object computeItem(Object... parameters) throws ScriptException {
		return Math.atan2(ExpressionUtil.convertDouble(parameters[0]), ExpressionUtil.convertDouble(parameters[1]));
	}
}
