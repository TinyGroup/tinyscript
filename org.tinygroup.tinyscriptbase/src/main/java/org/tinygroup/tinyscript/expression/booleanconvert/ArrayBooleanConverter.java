package org.tinygroup.tinyscript.expression.booleanconvert;

import org.tinygroup.commons.tools.ArrayUtil;
import org.tinygroup.tinyscript.expression.BooleanConverter;

public class ArrayBooleanConverter implements BooleanConverter{

	public boolean isMatch(Object obj) {
		return obj.getClass().isArray();
	}

	public Boolean convert(Object obj) {
		return ArrayUtil.arrayLength(obj) > 0;
	}

}
