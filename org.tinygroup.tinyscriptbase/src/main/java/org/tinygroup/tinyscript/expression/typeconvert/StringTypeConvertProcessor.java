package org.tinygroup.tinyscript.expression.typeconvert;

import java.util.Date;

import org.tinygroup.tinyscript.expression.TypeConvertProcessor;
import org.tinygroup.tinyscript.function.date.DateUtil;

public class StringTypeConvertProcessor implements TypeConvertProcessor {

	@Override
	public String getName() {
		return "string";
	}

	@Override
	public Object convert(Object... parameters) throws Exception {
		if (parameters != null) {
			if (parameters.length == 1) {
				return DateUtil.dateToString((Date) parameters[0], null);
			} else if (parameters.length == 2) {
				return DateUtil.dateToString((Date) parameters[0], (String) parameters[1]);
			}
		}
		return null;
	}

}
