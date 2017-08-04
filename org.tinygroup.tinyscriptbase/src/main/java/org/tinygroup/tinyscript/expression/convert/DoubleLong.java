package org.tinygroup.tinyscript.expression.convert;

import org.tinygroup.tinyscript.expression.Converter;

public class DoubleLong implements Converter<Double,Long> {

	public Long convert(Double object) {
		return object.longValue();
	}

	public Class<?> getSourceType() {
		return Double.class;
	}

	public Class<?> getDestType() {
		return Long.class;
	}

}
