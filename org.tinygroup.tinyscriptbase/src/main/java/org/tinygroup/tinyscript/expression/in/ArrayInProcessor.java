package org.tinygroup.tinyscript.expression.in;

import java.lang.reflect.Array;

import org.tinygroup.tinyscript.expression.InExpressionProcessor;

public class ArrayInProcessor implements InExpressionProcessor {

	@Override
	public boolean isMatch(Object collection) throws Exception {
		return collection.getClass().isArray();
	}

	@Override
	public boolean checkIn(Object collection, Object item) throws Exception {
		boolean tag = false;
		int length = Array.getLength(collection);
		for (int i = 0; i < length; i++) {
			if (item.equals(Array.get(collection, i))) {
				tag = true;
				break;
			}
		}
		return tag;
	}

}
