package org.tinygroup.tinyscript.objectitem;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

public class ObjectItemUtil {

	private static List<ObjectItemProcessor> processors = new ArrayList<ObjectItemProcessor>();
	
	static{
		addObjectItemProcessor(new ArrayItemProcessor());
		addObjectItemProcessor(new ListItemProcessor());
	}
	
	public static void addObjectItemProcessor(ObjectItemProcessor processor){
//		if(!processors.contains(processor)){
//			processors.add(processor);
//		}
		for(ObjectItemProcessor objectItemProcessor:processors){
			if(objectItemProcessor.equals(processor) || objectItemProcessor.getClass().isInstance(processor)){
			   return ;	
			}
		}
		processors.add(processor);
	}
	
	public static void addObjectItemProcessor(ObjectItemProcessor processor,int index){
//		if(!processors.contains(processor)){
//			processors.add(index,processor);
//		}
		for(ObjectItemProcessor objectItemProcessor:processors){
			if(objectItemProcessor.equals(processor) || objectItemProcessor.getClass().isInstance(processor)){
			   return ;	
			}
		}
		processors.add(index,processor);
	}
	
    public static void removeObjectItemProcessor(ObjectItemProcessor processor){
    	processors.remove(processor);
	}
	
    /**
     * 取值操作
     * @param context
     * @param obj
     * @param items
     * @return
     * @throws Exception
     */
	public static Object operate(ScriptContext context,Object obj,Object... items) throws Exception{
		for(ObjectItemProcessor processor:processors){
			if(processor.isMatch(obj, items)){
			   return processor.process(context,obj, items);
			}
		}
		throw new ScriptException(ResourceBundleUtil.getDefaultMessage("unmatch.info1", ObjectItemProcessor.class.getName()));
	}
	
	/**
	 * 赋值操作
	 * @param context
	 * @param value
	 * @param obj
	 * @param items
	 * @throws Exception
	 */
	public static void assignValue(ScriptContext context,Object value,Object obj,Object... items) throws Exception{
		boolean tag = false;
		for(ObjectItemProcessor processor:processors){
			if(processor.isMatch(obj, items)){
			   processor.assignValue(context,value,obj, items);
			   tag = true;
			   break;
			}
		}
		if(!tag){
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("unmatch.info1", ObjectItemProcessor.class.getName()));
		}
	}
}
