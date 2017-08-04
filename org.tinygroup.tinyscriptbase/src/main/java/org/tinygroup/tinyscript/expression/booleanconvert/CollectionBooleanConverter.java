package org.tinygroup.tinyscript.expression.booleanconvert;

import java.util.Collection;

import org.tinygroup.tinyscript.expression.BooleanConverter;

public class CollectionBooleanConverter implements BooleanConverter{

	public boolean isMatch(Object obj) {
		return obj instanceof Collection;
	}

	public Boolean convert(Object obj) {
		return ((Collection) obj).size() > 0;
	}

}
