package org.tinygroup.tinyscript.interpret.call;

import java.lang.reflect.Method;

import javassist.Modifier;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ExpressionParameter;
import org.tinygroup.tinyscript.interpret.FunctionCallProcessor;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.exception.NotMatchException;

/**
 * java方法调用的抽象处理器
 * @author yancheng11334
 *
 */
public abstract class AbstractMethodProcessor implements FunctionCallProcessor{

	public boolean  enableExpressionParameter(){
		return false;
	}
	
	public Object invoke(ScriptSegment segment, ScriptContext context,
			Object object, String methodName, Object... parameters)
			throws Exception {
		if(enableExpressionParameter()){
			//执行表达式参数
			return invokeMethod(segment,context,object,methodName,parameters);
		}else{
			//不支持表达式参数，需要转换成原生参数
			Object[] newParameters = null;
			try{
				newParameters = convertParameters(parameters);
			}catch(Exception e){
				//参数转换失败,不代表表达式错误,需要合适的处理器和函数进行解释
				throw new NotMatchException();
			}
			return invokeMethod(segment,context,object,methodName,newParameters);
		}
	}
	
	/**
	 * 转换表达式参数为值参数
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	protected Object[] convertParameters(Object[] parameters) throws Exception{
		if( parameters==null || parameters.length==0){
			return parameters;
		}
		Object[] newParameters = new Object[parameters.length];
		for(int i=0;i<parameters.length;i++){
			if(parameters[i]!=null && parameters[i] instanceof ExpressionParameter){
				newParameters[i] = ((ExpressionParameter)parameters[i]).eval();
			}else{
				newParameters[i] = parameters[i];
			}
		}
		return newParameters;
	}

	protected abstract Object invokeMethod(ScriptSegment segment, ScriptContext context,
			Object object, String methodName, Object... parameters)
			throws Exception;
	
	/**
	 * 参数是否包含lambda
	 * @param parameters
	 * @return
	 */
	protected boolean containsLambda(Object... parameters){
		if(parameters!=null && parameters.length>0){
		   for(Object p:parameters){
			  if(p!=null && p instanceof LambdaFunction){
				 return true;
			  }
		   }
		}
		return false;
	}
	
	/**
	 * 参数是否包含null值
	 * @param parameters
	 * @return
	 */
	protected boolean containsNull(Object... parameters){
		if(parameters!=null && parameters.length>0){
			   for(Object p:parameters){
				  if(p==null){
					 return true;
				  }
			   }
			}
			return false;
	}
	
	/**
	 * 判断方法名是否匹配
	 * @param method
	 * @param methodName
	 * @return
	 */
	protected boolean checkName(Method method,String methodName){
		if(method!=null){
		   return method.getName().equals(methodName);
		}
		return false;
	}
	
	/**
	 * 判断方法是否静态
	 * @param method
	 * @return
	 */
	protected boolean checkStatic(Method method){
		return Modifier.isStatic(method.getModifiers());
	}
	
	/**
	 * 判断参数个数是否一致
	 * @param method
	 * @param parameters
	 * @return
	 */
	protected boolean checkParameter(Method method,Object... parameters){
		int m = method.getParameterTypes() == null ? 0 : method.getParameterTypes().length;
		int n = parameters == null ? 0 : parameters.length;
		return m==n;
	}
	
}
