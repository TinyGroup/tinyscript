package org.tinygroup.tinyscript.expression.convert;

import org.tinygroup.tinyscript.expression.Converter;

public class StringDouble implements Converter<String, Double> {

	public Double convert(String object) {
		if (object.indexOf("%") > 0) {
			object = object.replaceAll("%", "");
			return Double.parseDouble(object)/100;
		}
		return Double.parseDouble(object);
	}

	public Class<?> getSourceType() {
		return String.class;
	}

	public Class<?> getDestType() {
		return Double.class;
	}

}
