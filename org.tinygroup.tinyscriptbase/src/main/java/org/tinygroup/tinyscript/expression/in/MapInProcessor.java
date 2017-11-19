package org.tinygroup.tinyscript.expression.in;

import java.util.Map;

import org.tinygroup.tinyscript.expression.InExpressionProcessor;

public class MapInProcessor implements InExpressionProcessor {

	@Override
	public boolean isMatch(Object collection) throws Exception {
		return collection instanceof Map;
	}

	@Override
	public boolean checkIn(Object collection, Object item) throws Exception {
		Map<?, ?> map = (Map<?, ?>) collection;
		return map.containsKey(item);
	}

}
