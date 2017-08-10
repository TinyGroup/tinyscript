package org.tinygroup.tinyscript.interpret;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.interpret.attribute.*;
import org.tinygroup.tinyscript.interpret.exception.NotMatchException;

/**
 * 属性工具类
 * @author yancheng11334
 *
 */
public final class AttributeUtil {

	private  static List<AttributeProcessor> attributeProcessors = new ArrayList<AttributeProcessor>();
	
	private AttributeUtil(){
		
	}
	
	static{
		addAttributeProcessor(new MapAttributeProcessor());
		addAttributeProcessor(new ArrayLengthProcessor());
		addAttributeProcessor(new ScriptCollectionModelAttributeProcessor());
		addAttributeProcessor(new ScriptClassInstanceAttributeProcessor());
		addAttributeProcessor(new MethodAttributeProcessor());
		addAttributeProcessor(new FieldAttributeProcessor());
	}
	
	public static void addAttributeProcessor(AttributeProcessor processor){
//		if(!attributeProcessors.contains(processor)){
//			attributeProcessors.add(processor);
//		}
		for(AttributeProcessor oldAttributeProcessor:attributeProcessors){
		    if(oldAttributeProcessor.equals(processor) || oldAttributeProcessor.getClass().isInstance(processor)){
		       return ;
		    }
		}
		attributeProcessors.add(processor);
	}
	
	public static void addAttributeProcessor(AttributeProcessor processor,int index){
//		if(!attributeProcessors.contains(processor)){
//			attributeProcessors.add(index,processor);
//		}
		for(AttributeProcessor oldAttributeProcessor:attributeProcessors){
		    if(oldAttributeProcessor.equals(processor) || oldAttributeProcessor.getClass().isInstance(processor)){
		       return ;
		    }
		}
		attributeProcessors.add(index,processor);
	}
	
	public static void removeAttributeProcessor(AttributeProcessor processor){
		attributeProcessors.remove(processor);
	}
	
	/**
	 * 获取对象属性
	 * @param object
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static Object getAttribute(Object object, Object name)
			throws Exception {
		if(object==null){
		   return null;	
		}
		for(AttributeProcessor processor:attributeProcessors){
		    try{
		    	if(processor.isMatch(object, name)){
				   return processor.getAttribute(object, name);    
			    }
		    }catch(NotMatchException e){
		    	//忽略不匹配异常
		    	continue;
		    }catch(Exception e){
		    	throw new ScriptException(String.format("[%s]中查找[%s]的属性值发生异常:", object.getClass().getName(),name),e);
		    }
		}
		throw new ScriptException(String.format("[%s]中不能找到[%s]的属性值.", object.getClass().getName(),name));
	}
}
