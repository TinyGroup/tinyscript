package org.tinygroup.tinyscript.expression.in;

import java.util.Collection;

import org.tinygroup.tinyscript.expression.InExpressionProcessor;

public class CollectionInProcessor implements InExpressionProcessor {

	@Override
	public boolean isMatch(Object collection) throws Exception {
		return collection instanceof Collection;
	}

	@Override
	public boolean checkIn(Object collection, Object item) throws Exception {
		Collection<?> c = (Collection<?>) collection;
		return c.contains(item);
	}

}
