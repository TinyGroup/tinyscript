package org.tinygroup.tinyscript.function.math;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

public class MathCotFunction extends AbstractMathCollectionFunction {

	@Override
	public String getNames() {
		return "cot";
	}

	@Override
	protected int getParameterCount() {
		return 1;
	}

	@Override
	protected Object computeItem(Object... parameters) throws ScriptException {
		double num = ExpressionUtil.convertDouble(parameters[0]);
		return 1 / Math.tan(num);
	}

}
