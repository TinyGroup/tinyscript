package org.tinygroup.tinyscript.interpret.attribute;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.tinygroup.tinyscript.interpret.AttributeProcessor;
import org.tinygroup.tinyscript.interpret.exception.NotMatchException;

public class FieldAttributeProcessor implements AttributeProcessor{

	private Map<Class<?>, Map<String, Field>> fieldCache = new HashMap<Class<?>, Map<String, Field>>();
	
	public boolean isMatch(Object object, Object name) {
		return name instanceof String;
	}

	public Object getAttribute(Object object, Object name) throws Exception {
		String fieldName = name.toString();
		Map<String, Field> stringFieldMap = fieldCache.get(fieldName);
		Field field = null;
		if (stringFieldMap != null) {
			field = stringFieldMap.get(fieldName);
		}
		if (field == null) {
			if(object instanceof Class){//静态类
				field = ((Class<?>)object).getField(fieldName);
			}else{
				field = object.getClass().getField(fieldName);
			}
			
			if (field != null) {
				if (Modifier.isPublic(field.getModifiers())) {
					field.setAccessible(true);
					if (stringFieldMap == null) {
						stringFieldMap = new HashMap<String, Field>();
						fieldCache.put(object.getClass(), stringFieldMap);
					}
					stringFieldMap.put(fieldName, field);
				} else {
					field = null;
				}
			}
		}
		if (field != null) {
			return field.get(object);
		}
		//执行不匹配操作
	    throw new NotMatchException();
	}

}
