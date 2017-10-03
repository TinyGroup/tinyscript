package org.tinygroup.tinyscript.interpret;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptClassInstance;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.impl.ScriptMethodContext;
import org.tinygroup.tinyscript.interpret.anonymous.*;
import org.tinygroup.tinyscript.interpret.directive.*;
import org.tinygroup.tinyscript.interpret.exception.NotMatchException;
import org.tinygroup.tinyscript.interpret.newinstance.*;

/**
 * 类实例工具类,支持构造参数带简单类型
 * @author yancheng11334
 *
 */
@SuppressWarnings("rawtypes")
public class ClassInstanceUtil {

	private static final List<SingleMethodProcessor> singleMethodProcessors = new ArrayList<SingleMethodProcessor>();
	
	private static final List<NewInstanceProcessor> newInstanceProcessors = new ArrayList<NewInstanceProcessor>();
	
	private static final List<InstanceOfProcessor> instanceOfProcessors = new ArrayList<InstanceOfProcessor>();
	
	static{
		addSingleMethodProxy(new RunnableProcessor());
		addSingleMethodProxy(new ComparatorProcessor());
		
		addNewInstanceProcessor(new JavaInstanceProcessor());
		addNewInstanceProcessor(new ScriptClassInstanceProcessor());
		
		addInstanceOfProcessor(new JavaInstanceOfProcessor());
	}
	
	public static void addSingleMethodProxy(SingleMethodProcessor singleMethodProxy){

		for(SingleMethodProcessor oldSingleMethodProcessor:singleMethodProcessors){
		    if(oldSingleMethodProcessor.equals(singleMethodProxy) || oldSingleMethodProcessor.getClass().isInstance(singleMethodProxy)){
		       return ;
		    }
		}
		singleMethodProcessors.add(singleMethodProxy);
	}
	
	public static void removeSingleMethodProxy(SingleMethodProcessor singleMethodProxy){
		singleMethodProcessors.remove(singleMethodProxy);
	}
	
	public static void addNewInstanceProcessor(NewInstanceProcessor processor){
		for(NewInstanceProcessor newInstanceProcessor:newInstanceProcessors){
		    if(newInstanceProcessor.equals(processor) || newInstanceProcessor.getClass().isInstance(processor)){
		       return ;
		    }
		}
		newInstanceProcessors.add(processor);
	}
	
	public static void removeNewInstanceProcessor(NewInstanceProcessor processor){
		newInstanceProcessors.remove(processor);
	}
	
	public static void addInstanceOfProcessor(InstanceOfProcessor processor){
		for(InstanceOfProcessor instanceOfProcessor:instanceOfProcessors){
		    if(instanceOfProcessor.equals(processor) || instanceOfProcessor.getClass().isInstance(processor)){
		       return;
		    }
		}
		instanceOfProcessors.add(processor);
	}
	
	public static void removeInstanceOfProcessor(InstanceOfProcessor processor){
		instanceOfProcessors.remove(processor);
	}
	
	public static SingleMethodProcessor findSingleMethodProcessor(Class<?> parameterType){
		for(SingleMethodProcessor singleMethodProcessor:singleMethodProcessors){
			if(parameterType.equals(singleMethodProcessor.getType())){
			   return singleMethodProcessor;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Object newInstance(ScriptSegment segment,
			ScriptContext context, String className,
			List<Object> paramList) throws Exception {
		ScriptClassInstance classInstance = null;
		if(context instanceof ScriptMethodContext){
			classInstance = ((ScriptMethodContext) context).getScriptClassInstance();
		}
		
		//初始化import列表
		List<String> importList = null;
		if (classInstance != null) {
			importList = classInstance.getScriptClass().getScriptSegment()
					.getImportList();
		}
		
		//执行具体的new实例逻辑
		for(NewInstanceProcessor processor:newInstanceProcessors){
			Object clazz = processor.findClass(segment, className, importList);
			try{
				if(clazz!=null){
				   return processor.newInstance(clazz, context, paramList);
				}
			}catch(NotMatchException e){
				//处理器不支持
				continue;
			}
			
		}
		throw new ScriptException(ResourceBundleUtil.getDefaultMessage("newinstance.notfound.error", className));
	}
	
	public static boolean isInstance(Object object,Object type) throws Exception {
		for(InstanceOfProcessor processor:instanceOfProcessors){
		    try{
		    	if(processor.isMatch(object, type)){
		    	   return processor.isInstance(object, type);
		    	}
		    }catch(NotMatchException e){
		    	//处理器不支持
				continue;
		    }
		}
		throw new ScriptException(ResourceBundleUtil.getDefaultMessage("unmatch.info1", InstanceOfProcessor.class.getName()));
	}
	
}
