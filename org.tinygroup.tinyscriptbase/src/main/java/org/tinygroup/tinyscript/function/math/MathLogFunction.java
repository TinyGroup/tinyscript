package org.tinygroup.tinyscript.function.math;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

public class MathLogFunction extends AbstractMathCollectionFunction {

	@Override
	public String getNames() {
		return "log";
	}

	@Override
	protected int getParameterCount() {
		return 2;
	}

	@Override
	protected Object computeItem(Object... parameters) throws ScriptException {
		double x = ExpressionUtil.convertDouble(parameters[0]);
		double y = ExpressionUtil.convertDouble(parameters[1]);
		return Math.log(x) / Math.log(y);
	}

}
