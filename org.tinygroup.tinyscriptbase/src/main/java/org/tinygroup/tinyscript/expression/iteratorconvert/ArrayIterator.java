package org.tinygroup.tinyscript.expression.iteratorconvert;

import java.lang.reflect.Array;
import java.util.Iterator;

import org.tinygroup.commons.tools.ArrayUtil;

public class ArrayIterator implements Iterator {
    private final Object object;
    private int size = 0;
    private int index = 0;

    public ArrayIterator(Object object) {
        this.object = object;
        size = ArrayUtil.arrayLength(object);
    }

    public boolean hasNext() {
        return index < size;
    }

    public Object next() {
        return Array.get(object, index++);
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}
