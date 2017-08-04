package org.tinygroup.tinyscript.interpret.call;

import org.tinygroup.tinyscript.ScriptClassInstance;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;
import org.tinygroup.tinyscript.interpret.exception.NotMatchException;

/**
 * 脚本类方法调用
 * @author yancheng11334
 *
 */
public class ScriptClassInstanceProcessor extends AbstractMethodProcessor{

	protected Object invokeMethod(ScriptSegment segment, ScriptContext context,
			Object object, String methodName, Object... parameters)
			throws Exception {
		ScriptClassInstance instance = ScriptContextUtil.getScriptClassInstance(context);
		//实例存在并且包含同名方法
		if(instance!=null){
		   if(instance.getScriptClass().getScriptMethod(methodName)!=null){
			   return instance.execute(context, methodName, parameters);
		   }else if(isGetMethod(instance,methodName,parameters)){
			   //执行get方法
			   String fieldName = getFieldName(methodName);
			   return instance.getField(fieldName);
		   }else if(isSetMethod(instance,methodName,parameters)){
			   //执行set方法
			   String fieldName = getFieldName(methodName);
			   instance.setField(fieldName, parameters[0]);
			   return null;
		   }
		}
		//抛出不匹配信息
	    throw new NotMatchException();
	}

	private boolean isGetMethod(ScriptClassInstance instance,String methodName, Object... parameters){
		if(methodName.startsWith("get") && (parameters==null || parameters.length==0)){
		   String fieldName = getFieldName(methodName);
		   return instance.existField(fieldName);
		}
		return false;
	}
	
	private boolean isSetMethod(ScriptClassInstance instance,String methodName, Object... parameters){
		if(methodName.startsWith("set") && parameters!=null && parameters.length==1){
		   String fieldName = getFieldName(methodName);
		   return instance.existField(fieldName);
		}
		return false;
	}
	
	private String getFieldName(String methodName){
		if(methodName!=null && methodName.length()>=4){
		   String first = methodName.substring(3, 4).toLowerCase();	//首字母小写
		   return first+methodName.substring(4);
		}
		return null;
	}
}
