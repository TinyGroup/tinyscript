package org.tinygroup.tinyscript.expression.typeconvert;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * 转换Long类型
 * @author yancheng11334
 *
 */
public class LongTypeConvertProcessor extends SingleTypeConvertProcessor{

	public String getName() {
		return "long";
	}

	protected Object convertSingle(Object parameter) throws ScriptException {
		return ExpressionUtil.convertLong(parameter);
	}

}
