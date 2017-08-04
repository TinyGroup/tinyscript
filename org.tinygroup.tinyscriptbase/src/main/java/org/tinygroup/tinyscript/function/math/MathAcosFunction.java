package org.tinygroup.tinyscript.function.math;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

public class MathAcosFunction extends AbstractMathCollectionFunction{

	public String getNames() {
		return "acos";
	}

	protected int getParameterCount() {
		return 1;
	}

	protected Object computeItem(Object... parameters) throws ScriptException {
		return Math.acos(ExpressionUtil.convertDouble(parameters[0]));
	}

}
