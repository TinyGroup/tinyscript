package org.tinygroup.tinyscript.expression.convert;

import org.tinygroup.tinyscript.expression.Converter;

public class StringFloat implements Converter<String,Float> {

	public Float convert(String object) {
		return Float.parseFloat(object);
	}

	public Class<?> getSourceType() {
		return String.class;
	}

	public Class<?> getDestType() {
		return Float.class;
	}

}
