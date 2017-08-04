package org.tinygroup.tinyscript.expression.iteratorconvert;

import java.util.Collection;
import java.util.Iterator;

import org.tinygroup.tinyscript.expression.IteratorConverter;

public class CollectionIteratorConverter implements IteratorConverter{

	public boolean isMatch(Object object) {
		return object instanceof Collection;
	}

	public Iterator convert(Object object) {
		return ((Collection) object).iterator();
	}

}
