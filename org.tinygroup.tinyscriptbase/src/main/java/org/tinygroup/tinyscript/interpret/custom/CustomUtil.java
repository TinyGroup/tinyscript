package org.tinygroup.tinyscript.interpret.custom;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;

/**
 * 自定义规则操作语句
 * @author yancheng11334
 *
 */
public final class CustomUtil {

	private static List<CustomProcessor> customProcessors = new ArrayList<CustomProcessor>();
	
	private CustomUtil(){
		
	}
	
	public static void addCustomProcessor(CustomProcessor processor){
		for(CustomProcessor customProcessor:customProcessors){
		    if(customProcessor.equals(processor) || customProcessor.getClass().isInstance(processor)){
		       return ;
		    }
		}
		customProcessors.add(processor);
	}
	
	public static void removeCustomProcessor(CustomProcessor processor){
		customProcessors.remove(processor);
	}
	
	/**
	 * 执行自定义规则并返回结果
	 * @param customObj
	 * @param customRule
	 * @param context
	 * @return
	 * @throws ScriptException
	 */
	public static Object executeRule(Object customObj,String customRule,ScriptContext context) throws ScriptException{
		for(CustomProcessor sqlProcessor:customProcessors){
			if(sqlProcessor.isMatch(customObj)){
			   return sqlProcessor.executeRule(customObj, customRule, context);
			}
		}
		throw new ScriptException(String.format("没有找到匹配类型[%s]的自定义规则处理器", customObj.getClass().getName()));
	}
}
