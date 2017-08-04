package org.tinygroup.tinyscript.expression.iteratorconvert;

import java.util.Iterator;

import org.tinygroup.tinyscript.expression.IteratorConverter;

public class ArrayIteratorConverter implements IteratorConverter{

	public boolean isMatch(Object object) {
		return object.getClass().isArray();
	}

	public Iterator convert(Object object) {
		return new ArrayIterator(object);
	}

}
