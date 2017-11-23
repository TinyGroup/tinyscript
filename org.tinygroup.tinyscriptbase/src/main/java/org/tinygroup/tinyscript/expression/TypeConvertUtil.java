package org.tinygroup.tinyscript.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.typeconvert.*;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 可扩展的类型转换工具类
 * 
 * @author yancheng11334
 *
 */
public final class TypeConvertUtil {

	private static Map<String, TypeConvertProcessor> processorMap = new HashMap<String, TypeConvertProcessor>();

	private TypeConvertUtil() {

	}

	static {
		addTypeConvertProcessor(new IntegerTypeConvertProcessor());
		addTypeConvertProcessor(new LongTypeConvertProcessor());
		addTypeConvertProcessor(new FloatTypeConvertProcessor());
		addTypeConvertProcessor(new DoubleTypeConvertProcessor());
		addTypeConvertProcessor(new DateTypeConvertProcessor());
		addTypeConvertProcessor(new BooleanTypeConvertProcessor());
		addTypeConvertProcessor(new ShortTypeConvertProcessor());
		addTypeConvertProcessor(new CharTypeConvertProcessor());
	}

	/**
	 * 注册类型处理器
	 * 
	 * @param processor
	 */
	public static void addTypeConvertProcessor(TypeConvertProcessor processor) {
		processorMap.put(processor.getName(), processor);
	}

	/**
	 * 卸载类型处理器
	 * 
	 * @param processor
	 */
	public static void removeTypeConvertProcessor(TypeConvertProcessor processor) {
		processorMap.remove(processor.getName());
	}

	/**
	 * 存在指定类型
	 * 
	 * @param type
	 * @return
	 */
	public static boolean exsitType(String type) {
		return processorMap.containsKey(type);
	}

	/**
	 * 获得存在的类型
	 * 
	 * @return
	 */
	public static List<String> getTypes() {
		return new ArrayList<String>(processorMap.keySet());
	}

	/**
	 * 执行指定类型的转换逻辑
	 * 
	 * @param type
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public static Object convert(String type, Object... parameters) throws Exception {
		TypeConvertProcessor processor = processorMap.get(type);
		if (processor == null) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("type.convert.error", type));
		}
		return processor.convert(parameters);
	}

}
