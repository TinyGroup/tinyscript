package org.tinygroup.tinyscript.expression.convert;

import org.tinygroup.tinyscript.expression.Converter;

public class FloatInteger implements Converter<Float,Integer> {

    public Integer convert(Float object) {
        return object.intValue();
    }

    public Class<?> getSourceType() {
        return Float.class;
    }

    public Class<?> getDestType() {
        return Integer.class;
    }

}
