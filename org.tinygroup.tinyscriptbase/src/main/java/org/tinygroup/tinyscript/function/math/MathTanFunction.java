package org.tinygroup.tinyscript.function.math;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

public class MathTanFunction extends AbstractMathCollectionFunction{

	public String getNames() {
		return "tan";
	}

	protected int getParameterCount() {
		return 1;
	}

	protected Object computeItem(Object... parameters) throws ScriptException {
		return Math.tan(ExpressionUtil.convertDouble(parameters[0]));
	}

}
