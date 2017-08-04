package org.tinygroup.tinyscript.expression.booleanconvert;

import java.util.Iterator;

import org.tinygroup.tinyscript.expression.BooleanConverter;

public class IteratorBooleanConverter implements BooleanConverter{

	public boolean isMatch(Object obj) {
		return obj instanceof Iterator;
	}

	public Boolean convert(Object obj) {
		return ((Iterator) obj).hasNext();
	}

}
