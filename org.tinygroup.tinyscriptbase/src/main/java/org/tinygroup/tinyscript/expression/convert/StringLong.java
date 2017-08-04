package org.tinygroup.tinyscript.expression.convert;

import org.tinygroup.tinyscript.expression.Converter;

public class StringLong implements Converter<String,Long> {

	public Long convert(String object) {
		return Long.parseLong(object);
	}

	public Class<?> getSourceType() {
		return String.class;
	}

	public Class<?> getDestType() {
		return Long.class;
	}

}
