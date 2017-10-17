package org.tinygroup.tinyscript.function.math;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * floor函数
 * @author yancheng11334
 *
 */
public class MathFloorFunction extends AbstractMathCollectionFunction{

	public String getNames() {
		return "floor";
	}

	protected int getParameterCount() {
		return 1;
	}

	protected Object computeItem(Object... parameters) throws ScriptException {
		Object value = parameters[0];
		if(value instanceof Double){
			return Math.floor((Double) value);
		}else if(value instanceof Float){
			Double d =  Math.floor((Float) value);
			return d.floatValue();
		}else if(value instanceof Integer){
			Double d =  Math.floor((Integer) value);
			return d.intValue();
		}else if(value instanceof Long){
			Double d =  Math.floor((Long) value);
			return d.longValue();
		}else {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.unsupport", getNames(),value.getClass().getName()));
		}
	}

}
