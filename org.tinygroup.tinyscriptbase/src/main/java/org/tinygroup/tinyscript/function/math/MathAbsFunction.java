package org.tinygroup.tinyscript.function.math;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

public class MathAbsFunction extends AbstractMathCollectionFunction{

	public String getNames() {
		return "abs";
	}

	protected int getParameterCount() {
		return 1;
	}

	protected Object computeItem(Object... parameters) throws ScriptException{
		Object value = parameters[0];
		if(value instanceof Double){
			return Math.abs((Double) value);
		}else if(value instanceof Float){
			return Math.abs((Float) value);
		}else if(value instanceof Integer){
			return Math.abs((Integer) value);
		}else if(value instanceof Long){
			return Math.abs((Long) value);
		}else {
			throw new ScriptException(ResourceBundleUtil.getMessage("function.parameter.unsupport", getNames(),value.getClass().getName()));
		}
	}

}
