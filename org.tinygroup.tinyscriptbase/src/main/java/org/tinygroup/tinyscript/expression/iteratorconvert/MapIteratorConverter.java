package org.tinygroup.tinyscript.expression.iteratorconvert;

import java.util.Iterator;
import java.util.Map;

import org.tinygroup.tinyscript.expression.IteratorConverter;

public class MapIteratorConverter implements IteratorConverter{

	public boolean isMatch(Object object) {
		return object instanceof Map;
	}

	public Iterator convert(Object object) {
		return ((Map)object).entrySet().iterator();
	}

}
