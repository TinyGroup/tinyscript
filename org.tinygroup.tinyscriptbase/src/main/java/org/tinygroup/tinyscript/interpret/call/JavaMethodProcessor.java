package org.tinygroup.tinyscript.interpret.call;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.exception.NotMatchException;


/**
 * java方法调用
 * @author yancheng11334
 *
 */
public class JavaMethodProcessor extends AbstractMethodProcessor{
   
	private static  final Object[] EMPTY_ARGS = new Object[0];
	
	protected Object invokeMethod(ScriptSegment segment,
			ScriptContext context, Object object, String methodName,
			Object... parameters) throws Exception {
		if(object!=null){
		   if(containsLambda(parameters)){
			  return invokeMethodWithWrapper(context,object, methodName, parameters);
		   }else{
			  return invokeJavaMethod(object, methodName, parameters);
		   }
		}
		//抛出不匹配信息
	    throw new NotMatchException();
	}
	
	private Object invokeJavaMethod(Object object, String methodName,
			Object[] parameters) throws Exception {
		try{
			List<Method> methods = JavaMethodUtil.getClassMethod(object.getClass(),methodName);
			if(methods==null){
			   throw new NoSuchMethodException(String.format("类[%s]不存在方法[%s]", object.getClass().getName(),methodName));
			}
			//遍历同名方法,匹配参数
			for(Method method:methods){
				Object[] newArgs = dealCommonParameters(method,parameters);
				if(newArgs!=null){
				   method.setAccessible(true);
				   return method.invoke(object, newArgs);
				}
			}
			throw new NoSuchMethodException(String.format("类[%s]不存在与参数列表一致的方法[%s]", object.getClass().getName(),methodName));
		}catch(InvocationTargetException e){
		    throw new ScriptException(String.format("类[%s]执行方法[%s]发生异常.", object.getClass().getName(),methodName),e.getTargetException()); 
		}catch(Exception e){
			//抛出不匹配信息
		    throw new NotMatchException(e); 
		}
	}
	
	private Object[] dealCommonParameters(Method method,Object[] parameters){
		Class<?>[] types = method.getParameterTypes();
		int m = types == null ? 0 : types.length;
		int n = parameters == null ? 0 : parameters.length;
		boolean hasVarArgs = method.isVarArgs();
		
		if(hasVarArgs){
		   //有变长数组
		   if(m==1 && n==0){
			  return EMPTY_ARGS;
		   }else if(m>1 && n>=m-1){
			  Object[] newArgs = new Object[m];
			  //检查定长参数的匹配情况
			  for(int i=0;i<m-1;i++){
				 if( !JavaMethodUtil.isInstance(types[i], parameters[i])){
					//某个参数类型不一致
					return null;
				 }else{ 
					//复制非变长参数
				    newArgs[i] = parameters[i]; 
				 }
			  }
			  
			  //变长参数一定是方法最后一个参数
			  if(n==m-1){
				 newArgs[m-1] = EMPTY_ARGS;
			  }else{
				 //将变长参数转换为一个变长数组
				 Object dynamicArray = Array.newInstance(types[m-1].getComponentType(), n-m+1);
				 for(int i=0;i<=n-m;i++){
				    Array.set(dynamicArray, i, parameters[m-1+i]);
				 }
				 newArgs[m-1] = dynamicArray;
			  }
			  return newArgs;
		   }
		}else{
		   //无变长数组
			if(m==0 && n==0){
			   return EMPTY_ARGS;
			}else if(m==n){
			   for(int i=0;i<n;i++){
				   if( !JavaMethodUtil.isInstance(types[i], parameters[i])){
					  //某个参数类型不一致
					  return null;
				   }
			   }
			   //类型一致的有参函数
			   return parameters;
			}
		}
		return null;
	}
	
	private Object invokeMethodWithWrapper(ScriptContext context,Object object, String methodName,
			Object... parameters) throws Exception {
		try{
			List<Method> methods = JavaMethodUtil.getClassMethod(object.getClass(),methodName);
			if(methods==null){
			   throw new NoSuchMethodException(String.format("类[%s]不存在方法[%s]", object.getClass().getName(),methodName));
			}
			//遍历同名方法,匹配参数
			for(Method method:methods){
				Object[] newArgs = dealLambdaParameters(context,method,parameters);
				if(newArgs!=null){
				   method.setAccessible(true);
				   return method.invoke(object, newArgs);
				}
			}
			throw new NoSuchMethodException(String.format("类[%s]不存在与参数列表一致的方法[%s]", object.getClass().getName(),methodName));
		}catch(InvocationTargetException e){
		    throw new ScriptException(String.format("类[%s]执行方法[%s]发生异常.", object.getClass().getName(),methodName),e.getTargetException()); 
		}catch(Exception e){
			//抛出不匹配信息
		    throw new NotMatchException(); 
		}
	}
	
	private Object[] dealLambdaParameters(ScriptContext context,Method method,Object[] parameters){
		Class<?>[] types = method.getParameterTypes();
		int m = types == null ? 0 : types.length;
		int n = parameters == null ? 0 : parameters.length;
        boolean hasVarArgs = method.isVarArgs();
		if(hasVarArgs){
		   //有变长数组
		   if(m==1 && n==0){
			  return EMPTY_ARGS;
		   }else if(m>1 && n>=m-1){
			  Object[] newArgs = new Object[m];
			  //检查定长参数的匹配情况
			  for(int i=0;i<m-1;i++){
				 if(parameters[i]!=null && !(parameters[i] instanceof LambdaFunction) && !JavaMethodUtil.isInstance(types[i], parameters[i])){
				   //某个参数类型不一致
				   return null;
				 }else{
				   //复制非变长参数
				   newArgs[i] = JavaMethodUtil.convert(context, types[i], parameters[i]);
				 }
			  }
			  
			  //变长参数一定是方法最后一个参数
			  if(n==m-1){
				 newArgs[m-1] = EMPTY_ARGS;
			  }else{
				 //将变长参数转换为一个变长数组
				 Object dynamicArray = Array.newInstance(types[m-1].getComponentType(), n-m+1);
				 for(int i=0;i<=n-m;i++){
				    Array.set(dynamicArray, i, parameters[m-1+i]);
				 }
				 newArgs[m-1] = dynamicArray;
			  }
			  return newArgs;
		   }
		}else{
		   //无变长数组
			if(m==0 && n==0){
			   return EMPTY_ARGS;
			}else if(m==n){
			   Object[] newArgs = new Object[n];
			   for(int i=0;i<n;i++){
				   if(parameters[i]!=null && !(parameters[i] instanceof LambdaFunction) && !JavaMethodUtil.isInstance(types[i], parameters[i])){
					  //某个参数类型不一致
					  return null;
				   }else{
					   newArgs[i] = JavaMethodUtil.convert(context, types[i], parameters[i]);
				   }
			   }
			   //类型一致的有参函数
			   return newArgs;
			}
		}
		return null;
	}
	
}
