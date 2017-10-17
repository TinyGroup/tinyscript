package org.tinygroup.tinyscript.function.math;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * ceil函数
 * @author yancheng11334
 *
 */
public class MathCeilFunction extends AbstractMathCollectionFunction{

	public String getNames() {
		return "ceil";
	}

	protected int getParameterCount() {
		return 1;
	}

	protected Object computeItem(Object... parameters) throws ScriptException {
		Object value = parameters[0];
		if(value instanceof Double){
			return Math.ceil((Double) value);
		}else if(value instanceof Float){
			Double d =  Math.ceil((Float) value);
			return d.floatValue();
		}else if(value instanceof Integer){
			Double d =  Math.ceil((Integer) value);
			return d.intValue();
		}else if(value instanceof Long){
			Double d =  Math.ceil((Long) value);
			return d.longValue();
		}else {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.unsupport", getNames(),value.getClass().getName()));
		}
	}

}
