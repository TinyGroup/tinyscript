package org.tinygroup.tinyscript.interpret.attribute;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.tinygroup.tinyscript.interpret.AttributeProcessor;
import org.tinygroup.tinyscript.interpret.exception.NotMatchException;

public class MethodAttributeProcessor implements AttributeProcessor{

	private Map<Class<?>, Map<String, Method>> methodCache = new HashMap<Class<?>, Map<String, Method>>();
	private PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
	
	public boolean isMatch(Object object, Object name) {
		return name instanceof String;
	}

	public Object getAttribute(Object object, Object name) throws Exception {
		String fieldName = name.toString();
		Map<String, Method> stringMethodMap = methodCache.get(object
				.getClass());
		Method method = null;
		if (stringMethodMap != null) {
			method = stringMethodMap.get(fieldName);
		}
		if (method == null) {
			PropertyDescriptor descriptor = propertyUtilsBean
					.getPropertyDescriptor(object, fieldName);
			if (descriptor != null && descriptor.getReadMethod() != null) {
				method = object.getClass().getMethod(
						descriptor.getReadMethod().getName());
				method.setAccessible(true);
				if (stringMethodMap == null) {
					stringMethodMap = new HashMap<String, Method>();
					methodCache.put(object.getClass(), stringMethodMap);
				}
				stringMethodMap.put(fieldName, method);
			}
		}
		if (method != null) {
			return method.invoke(object);
		}
		//执行不匹配操作
		throw new NotMatchException();
	}

}
