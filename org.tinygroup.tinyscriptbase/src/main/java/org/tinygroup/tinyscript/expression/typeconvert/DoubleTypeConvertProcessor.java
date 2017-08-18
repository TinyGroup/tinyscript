package org.tinygroup.tinyscript.expression.typeconvert;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * 转换Double类型
 * @author yancheng11334
 *
 */
public class DoubleTypeConvertProcessor extends SingleTypeConvertProcessor{

	public String getName() {
		return "double";
	}

	protected Object convertSingle(Object parameter) throws ScriptException {
		return ExpressionUtil.convertDouble(parameter);
	}

}
