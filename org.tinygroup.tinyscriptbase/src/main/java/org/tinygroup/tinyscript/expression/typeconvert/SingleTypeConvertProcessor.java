package org.tinygroup.tinyscript.expression.typeconvert;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.TypeConvertProcessor;


public abstract  class SingleTypeConvertProcessor implements TypeConvertProcessor{

	public Object convert(Object... parameters) throws ScriptException {
		return convertSingle(parameters[0]);
	}
	
	/**
	 * 转换单个参数
	 * @param parameter
	 * @return
	 * @throws ScriptException
	 */
	protected abstract  Object convertSingle(Object parameter) throws ScriptException;

}
