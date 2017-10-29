package org.tinygroup.tinyscript.function.math;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

public class MathRadiansFunction extends AbstractMathCollectionFunction {

	@Override
	public String getNames() {
		return "radians";
	}

	@Override
	protected int getParameterCount() {
		return 1;
	}

	@Override
	protected Object computeItem(Object... parameters) throws ScriptException {
		double num = ExpressionUtil.convertDouble(parameters[0]);
		return Math.PI * num / 180;
	}

}
