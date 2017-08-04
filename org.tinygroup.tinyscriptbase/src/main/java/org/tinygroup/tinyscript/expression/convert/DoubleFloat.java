package org.tinygroup.tinyscript.expression.convert;

import org.tinygroup.tinyscript.expression.Converter;

public class DoubleFloat implements Converter<Double,Float> {

	public Float convert(Double object) {
		return object.floatValue();
	}

	public Class<?> getSourceType() {
		return Double.class;
	}

	public Class<?> getDestType() {
		return Float.class;
	}

}
