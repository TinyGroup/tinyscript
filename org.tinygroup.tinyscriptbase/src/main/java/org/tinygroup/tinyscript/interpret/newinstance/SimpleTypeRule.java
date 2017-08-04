package org.tinygroup.tinyscript.interpret.newinstance;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;

public class SimpleTypeRule implements ConstructorParameterRule{

	private static final Map<Class<?>, String> simpleClassMap = new HashMap<Class<?>, String>();
	static {
		simpleClassMap.put(Boolean.class, "Boolean");
		simpleClassMap.put(boolean.class, "Boolean");
		simpleClassMap.put(Short.class, "Short");
		simpleClassMap.put(short.class, "Short");
		simpleClassMap.put(Byte.class, "Byte");
		simpleClassMap.put(byte.class, "Byte");
		simpleClassMap.put(Character.class, "Character");
		simpleClassMap.put(char.class, "Character");
		simpleClassMap.put(Integer.class, "Integer");
		simpleClassMap.put(int.class, "Integer");
		simpleClassMap.put(Long.class, "Long");
		simpleClassMap.put(long.class, "Long");
		simpleClassMap.put(Float.class, "Float");
		simpleClassMap.put(float.class, "Float");
		simpleClassMap.put(Double.class, "Double");
		simpleClassMap.put(double.class, "Double");
	}
	
	public boolean isMatch(Class<?> parameterType, Object parameter) {
		return parameter!=null && simpleClassMap.containsKey(parameterType) && simpleClassMap.get(parameterType).equals(simpleClassMap.get(parameter.getClass()));
	}

	public Object convert(ScriptContext context,Class<?> parameterType, Object parameter) {
		return parameter;
	}
}
