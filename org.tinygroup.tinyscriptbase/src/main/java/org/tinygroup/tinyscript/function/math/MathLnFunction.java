package org.tinygroup.tinyscript.function.math;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

public class MathLnFunction extends AbstractMathCollectionFunction {

	@Override
	public String getNames() {
		return "ln";
	}

	@Override
	protected int getParameterCount() {
		return 1;
	}

	@Override
	protected Object computeItem(Object... parameters) throws ScriptException {
		double num = ExpressionUtil.convertDouble(parameters[0]);
		return Math.log(num);
	}

}
