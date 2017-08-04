package org.tinygroup.tinyscript.function.math;

import org.tinygroup.tinyscript.ScriptException;

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
			throw new ScriptException(String.format("%s函数不支持%s类型的参数",getNames() ,value.getClass().getName()));
		}
	}

}
