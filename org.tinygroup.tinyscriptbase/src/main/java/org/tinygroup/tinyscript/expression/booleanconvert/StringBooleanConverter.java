package org.tinygroup.tinyscript.expression.booleanconvert;

import org.tinygroup.tinyscript.expression.BooleanConverter;

public class StringBooleanConverter implements BooleanConverter{

	public boolean isMatch(Object obj) {
		return obj.getClass() == String.class;
	}

	public Boolean convert(Object obj) {
		return ((String) obj).length() > 0;
	}

}
