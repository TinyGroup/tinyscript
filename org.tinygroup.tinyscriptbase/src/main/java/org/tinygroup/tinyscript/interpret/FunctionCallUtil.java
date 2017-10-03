package org.tinygroup.tinyscript.interpret;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
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
			throw new NoSuchMethodException(ResourceBundleUtil.getDefaultMessage("call.notfound.error", object.getClass().getName(),methodName));
		}else{
			throw new NoSuchMethodException(ResourceBundleUtil.getDefaultMessage("call.null.error", methodName));
		}
	}
}
