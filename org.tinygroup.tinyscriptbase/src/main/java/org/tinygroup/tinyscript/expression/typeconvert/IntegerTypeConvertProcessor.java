package org.tinygroup.tinyscript.expression.typeconvert;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * 转换Integer类型
 * @author yancheng11334
 *
 */
public class IntegerTypeConvertProcessor extends SingleTypeConvertProcessor{

	public String getName() {
		return "int";
	}

	protected Object convertSingle(Object parameter) throws ScriptException {
		return ExpressionUtil.convertInteger(parameter);
	}

}
