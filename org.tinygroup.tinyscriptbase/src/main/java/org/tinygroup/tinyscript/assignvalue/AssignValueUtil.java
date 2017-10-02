package org.tinygroup.tinyscript.assignvalue;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 赋值扩展工具类
 * @author yancheng11334
 *
 */
public class AssignValueUtil {

	private static List<AssignValueProcessor> processors = new ArrayList<AssignValueProcessor>();
	
	static {
		addAssignValueProcessor(new DefaultAssignValueProcessor());
		addAssignValueProcessor(new MapAssignValueProcessor());
		addAssignValueProcessor(new ClassFieldAssignValueProcessor());
		addAssignValueProcessor(new ObjectItemAssignValueProcessor());
	}
	
	public  static void addAssignValueProcessor(AssignValueProcessor processor){
//		if(!processors.contains(processor)){
//			processors.add(0,processor); //优先级要比默认赋值处理器高
//		}
		for(AssignValueProcessor assignValueProcessor:processors){
		   if(assignValueProcessor.equals(processor) || assignValueProcessor.getClass().isInstance(processor)){
			  return ;	
		   }
		}
		processors.add(0,processor); //优先级要比默认赋值处理器高
	}
	
	public  static void removeAssignValueProcessor(AssignValueProcessor processor){
		processors.remove(processor);
	}
	
	/**
	 * 赋值扩展统一入口
	 * @param name
	 * @param value
	 * @param context
	 * @throws Exception
	 */
	public static void operate(String name,Object value,ScriptContext context) throws Exception{
		for(AssignValueProcessor processor:processors){
		    if(processor.isMatch(name, context)){
		       processor.process(name, value, context);
		       return ;
		    }
		}
		throw new ScriptException(ResourceBundleUtil.getDefaultMessage("unmatch.info1", AssignValueProcessor.class.getName()));
	}
	
}
