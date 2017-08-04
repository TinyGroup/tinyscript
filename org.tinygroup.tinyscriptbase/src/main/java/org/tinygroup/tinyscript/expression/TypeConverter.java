package org.tinygroup.tinyscript.expression;

/**
 * 类型转换接口
 * @author yancheng11334
 *
 * @param <T>
 */
public interface TypeConverter<T> {

	boolean isMatch(Object object);
	
	T convert(Object object);
}
