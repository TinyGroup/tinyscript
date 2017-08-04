package org.tinygroup.tinyscript.expression.convert;

import org.tinygroup.tinyscript.expression.Converter;

public class DoubleInteger implements Converter<Double,Integer> {

	public Integer convert(Double object) {
		return object.intValue();
	}

	public Class<?> getSourceType() {
		return Double.class;
	}

	public Class<?> getDestType() {
		return Integer.class;
	}

}
