package org.tinygroup.tinyscript.expression;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.typeconvert.*;

/**
 * 可扩展的类型转换工具类
 * @author yancheng11334
 *
 */
public final class TypeConvertUtil {
    
	private static Map<String,TypeConvertProcessor> processorMap = new HashMap<String,TypeConvertProcessor>();
	
	private TypeConvertUtil(){
		
	}
	
	static{
		addTypeConvertProcessor(new IntegerTypeConvertProcessor());
		addTypeConvertProcessor(new LongTypeConvertProcessor());
		addTypeConvertProcessor(new FloatTypeConvertProcessor());
		addTypeConvertProcessor(new DoubleTypeConvertProcessor());
		addTypeConvertProcessor(new DateTypeConvertProcessor());
	}
	
	/**
	 * 注册类型处理器
	 * @param processor
	 */
	public static void addTypeConvertProcessor(TypeConvertProcessor processor){
		processorMap.put(processor.getName(),processor);
	}
	
	/**
	 * 卸载类型处理器
	 * @param processor
	 */
	public static void removeTypeConvertProcessor(TypeConvertProcessor processor){
		processorMap.remove(processor.getName());
	}
	
	/**
	 * 存在指定类型
	 * @param type
	 * @return
	 */
	public static boolean exsitType(String type){
		return processorMap.containsKey(type);
	}
	/**
	 * 执行指定类型的转换逻辑
	 * @param type
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public static Object convert(String type,Object...parameters) throws Exception{
		TypeConvertProcessor processor = processorMap.get(type);
		if(processor==null){
		   throw new ScriptException(String.format("根据类型[%s]没有找到匹配的类型转换器", type));
		}
		return processor.convert(parameters);
	}
	
}
