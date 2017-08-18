package org.tinygroup.tinyscript.expression.typeconvert;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * 转换Float类型
 * @author yancheng11334
 *
 */
public class FloatTypeConvertProcessor extends SingleTypeConvertProcessor{

	public String getName() {
		return "float";
	}

	protected Object convertSingle(Object parameter) throws ScriptException {
		return ExpressionUtil.convertFloat(parameter);
	}

}
