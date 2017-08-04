package org.tinygroup.tinyscript.expression.convert;

import org.tinygroup.tinyscript.expression.Converter;

public class LongInteger implements Converter<Long,Integer> {
    public Integer convert(Long object) {
        return object.intValue();
    }

    public Class<?> getSourceType() {
        return Long.class;
    }

    public Class<?> getDestType() {
        return Integer.class;
    }

}
