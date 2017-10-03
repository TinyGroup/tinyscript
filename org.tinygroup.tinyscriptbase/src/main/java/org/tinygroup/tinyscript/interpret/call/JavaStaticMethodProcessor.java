package org.tinygroup.tinyscript.interpret.call;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.MethodUtils;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.exception.NotMatchException;


/**
 * java静态方法调用
 * @author yancheng11334
 *
 */
public class JavaStaticMethodProcessor extends AbstractMethodProcessor{

	protected Object invokeMethod(ScriptSegment segment,
			ScriptContext context, Object object, String methodName,
			Object... parameters) throws Exception {
		if(object instanceof Class){
		   Class<?> clazz = (Class<?>) object;
		   //存在lambda或者null值
		   if(containsLambda(parameters) || containsNull(parameters)){
			   return invokeMethodWithWrapper(context,clazz, methodName, parameters);
		   }else{
			   return invokeJavaMethod(clazz, methodName, parameters);
		   }
		}
		//抛出不匹配信息
	    throw new NotMatchException();
	}

	private Object invokeJavaMethod(Class<?> clazz, String methodName,
			Object... parameters) throws Exception {
		try{
			return MethodUtils.invokeStaticMethod(clazz, methodName, parameters);
		}catch(InvocationTargetException e){
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("java.run.error4",clazz.getName(),methodName),e.getTargetException()); 
		}catch(Exception e){
			//抛出不匹配信息
		    throw new NotMatchException(e); 
		}
	}
	
	private Object invokeMethodWithWrapper(ScriptContext context,Class<?> clazz, String methodName,
			Object... parameters) throws Exception {
		try{
			Method[] methods = clazz.getMethods();
			if(methods!=null){
				for(Method method:methods){
					//方法名称匹配
					//方法是静态方法
					//方法参数个数一致
				    if(checkName(method,methodName) && checkStatic(method) && checkParameter(method,parameters)){
				       if(JavaMethodUtil.checkMethod(method, parameters)){
				    	  Object[] newParameters = JavaMethodUtil.wrapper(context,method, parameters);
				    	  return method.invoke(null, newParameters);
				       }
				    }
				}
			}
		}catch(InvocationTargetException e){
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("java.run.error4",clazz.getName(),methodName),e.getTargetException()); 
		}catch(Exception e){
			//抛出不匹配信息
		    throw new NotMatchException(e); 
		}
		
		//抛出不匹配信息
	    throw new NotMatchException(); 
	}
}
