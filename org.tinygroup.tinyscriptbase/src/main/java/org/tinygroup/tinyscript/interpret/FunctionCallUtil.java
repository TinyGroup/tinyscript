package org.tinygroup.tinyscript.interpret;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.call.*;
import org.tinygroup.tinyscript.interpret.exception.NotMatchException;

/**
 * 方法调用工具类
 * @author yancheng11334
 *
 */
public class FunctionCallUtil {

	private static List<FunctionCallProcessor> processors = new ArrayList<FunctionCallProcessor>();
	
	static{
		addFunctionCallProcessor(new ScriptFunctionProcessor());
		addFunctionCallProcessor(new ScriptClassInstanceProcessor());
		addFunctionCallProcessor(new JavaMethodProcessor());
		addFunctionCallProcessor(new JavaStaticMethodProcessor());
		addFunctionCallProcessor(new LambdaFunctionProcessor());
		addFunctionCallProcessor(new CollectionModelProcessor());
	}
	
	public static void addFunctionCallProcessor(FunctionCallProcessor processor){
//		if(!processors.contains(processor)){
//			processors.add(processor);
//		}
		for(FunctionCallProcessor functionCallProcessor:processors){
			if(functionCallProcessor.equals(processor) || functionCallProcessor.getClass().isInstance(processor)){
			   return ;
			}
		}
		processors.add(processor);
	}
	
	public static void removeFunctionCallProcessor(FunctionCallProcessor processor){
		processors.remove(processor);
	}
	
	/**
	 * 调度方法总入口
	 * @param segment
	 * @param context
	 * @param object
	 * @param methodName
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public static Object operate(ScriptSegment segment,
			ScriptContext context, Object object, String methodName,
			Object... parameters) throws Exception{
		for(FunctionCallProcessor processor:processors){
		    try{
		    	return processor.invoke(segment, context, object, methodName, parameters);
		    }catch(NotMatchException e){
		    	//表示当前处理器不匹配
		    	continue;
		    }catch(Exception e){
		    	throw e;
		    }
		}
		if(object!=null){
			throw new ScriptException(String.format("没有找到合适的FunctionCallProcessor进行处理,对象类型[%s],方法名[%s]",object.getClass().getName(),methodName));
		}else{
			throw new ScriptException(String.format("没有找到合适的FunctionCallProcessor进行处理,对象为null,方法名[%s]",methodName));
		}
	}
}
