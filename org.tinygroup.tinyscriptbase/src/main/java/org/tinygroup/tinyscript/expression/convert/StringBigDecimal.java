package org.tinygroup.tinyscript.expression.convert;

import java.math.BigDecimal;

import org.tinygroup.tinyscript.expression.Converter;

public class StringBigDecimal implements Converter<String,BigDecimal> {

	public BigDecimal convert(String object) {
		return new BigDecimal(object);
	}

	public Class<?> getSourceType() {
		return String.class;
	}

	public Class<?> getDestType() {
		return BigDecimal.class;
	}

}
