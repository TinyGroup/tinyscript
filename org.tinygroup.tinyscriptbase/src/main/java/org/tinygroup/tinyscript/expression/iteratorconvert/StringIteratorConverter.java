package org.tinygroup.tinyscript.expression.iteratorconvert;

import java.util.Iterator;

import org.tinygroup.tinyscript.expression.IteratorConverter;

public class StringIteratorConverter implements IteratorConverter{

	public boolean isMatch(Object object) {
		return object instanceof String;
	}

	public Iterator convert(Object object) {
		String s = (String) object;
		return new ArrayIterator(s.toCharArray());
	}

}
