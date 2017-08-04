package org.tinygroup.tinyscript.expression.convert;

import org.tinygroup.tinyscript.expression.Converter;

public class FloatLong implements Converter<Float,Long> {

    public Long convert(Float object) {
        return object.longValue();
    }

    public Class<?> getSourceType() {
        return Float.class;
    }

    public Class<?> getDestType() {
        return Long.class;
    }

}
