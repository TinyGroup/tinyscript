package org.tinygroup.tinyscript.expression.convert;

import org.tinygroup.tinyscript.expression.Converter;

public class StringInteger implements Converter<String,Integer> {

	public Integer convert(String object) {
		return Integer.parseInt(object);
	}

	public Class<?> getSourceType() {
		return String.class;
	}

	public Class<?> getDestType() {
		return Integer.class;
	}

}
