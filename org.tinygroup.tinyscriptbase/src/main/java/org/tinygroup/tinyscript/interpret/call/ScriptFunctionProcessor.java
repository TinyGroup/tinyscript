package org.tinygroup.tinyscript.interpret.call;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptFunction;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.DynamicNameScriptFunction;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;
import org.tinygroup.tinyscript.interpret.exception.NotMatchException;

/**
 * 调用脚本函数
 * @author yancheng11334
 *
 */
public class ScriptFunctionProcessor extends AbstractMethodProcessor{

	public boolean  enableExpressionParameter(){
		return true;
	}
	
	protected Object invokeMethod(ScriptSegment segment, ScriptContext context,
			Object object, String methodName, Object... parameters)
			throws Exception {
		
		ScriptFunction function = segment.getScriptEngine()
				.findScriptFunction(object, methodName);
		if(function!=null){
		   try{
			   if(function instanceof DynamicNameScriptFunction){
				  //存储函数名
				   ScriptContextUtil.addDynamicFunctionName(context, methodName);
			   }
			   if(object==null){
				   //非绑定函数
				   if(function.enableExpressionParameter()){
					   return function.execute(segment, context, parameters);
				   }else{
					   return function.execute(segment, context, convertParameters(parameters));
				   }
			   }else{
				   //绑定对象的函数
				   Object[] newParameters = new Object[(parameters == null ? 1
							: parameters.length) + 1];
				   newParameters[0] = object;
				   if (parameters != null && parameters.length > 0) {
					   System.arraycopy(parameters, 0, newParameters, 1, parameters.length);
				   }
				   if(function.enableExpressionParameter()){
					   return function.execute(segment, context, newParameters); 
				   }else{
					   return function.execute(segment, context, convertParameters(newParameters)); 
				   }
			   }  
		   }catch(ScriptException e){
			   //抛出不匹配信息
			   throw new NotMatchException();
		   }finally{
               if(function instanceof DynamicNameScriptFunction){
				  //删除函数名
            	  ScriptContextUtil.removeDynamicFunctionName(context, methodName);
			   }
		   }
		   
		}
		//抛出不匹配信息
		throw new NotMatchException();
	}

}
