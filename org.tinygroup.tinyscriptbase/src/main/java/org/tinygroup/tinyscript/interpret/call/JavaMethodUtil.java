package org.tinygroup.tinyscript.interpret.call;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.MethodUtils;
import org.tinygroup.tinyscript.ScriptContext;

/**
 * Method操作辅助类
 * @author yancheng11334
 *
 */
public class JavaMethodUtil {

	//方法参数匹配条件
	private static List<MethodParameterRule> rules = new ArrayList<MethodParameterRule>();
	
	//方法缓存：一级key：class类型，二级key方法名
	private static Map<Class<?>,Map<String,List<Method>>> methodCache = new HashMap<Class<?>,Map<String,List<Method>>>();
	
	//简单类型映射
	private static Map<Class<?>,Class<?>> simpleClassTypes = new HashMap<Class<?>,Class<?>>();
	
	private static Comparator<Method> methodComparator = new MethodComparator();
	
	static{
		simpleClassTypes.put(boolean.class,Boolean.class);
		simpleClassTypes.put(byte.class,Byte.class);
		simpleClassTypes.put(char.class,Character.class);
		simpleClassTypes.put(short.class,Short.class);
		simpleClassTypes.put(int.class,Integer.class);
		simpleClassTypes.put(long.class,Long.class);
		simpleClassTypes.put(float.class,Float.class);
		simpleClassTypes.put(double.class,Double.class);
	}
	
	static{
		addMethodParameterRule(new NullValueRule());
		addMethodParameterRule(new InstanceRule());
		addMethodParameterRule(new SimpleTypeRule());
		addMethodParameterRule(new LambdaRule());
	}
	
	/**
	 * 增加java方法匹配规则
	 * @param rule
	 */
	public static void addMethodParameterRule(MethodParameterRule rule){
//		 if(!rules.contains(rule)){
//			 rules.add(rule);
//		 }
		for(MethodParameterRule methodParameterRule:rules){
		   if(methodParameterRule.equals(rule) || methodParameterRule.getClass().isInstance(rule)){
			  return ;
		   }
		}
		rules.add(rule);
	}
	
	/**
	 * 删除java方法匹配规则
	 * @param rule
	 */
	public static void removeMethodParameterRule(MethodParameterRule rule){
		rules.remove(rule);
	}
	
	/**
	 * 添加方法缓存
	 * @param clazz
	 */
	public static  void addClassMethod(Class<?> clazz){
		Map<String,List<Method>> classMethodMap = new HashMap<String,List<Method>>();
		Method[] methods = clazz.getMethods();
		if(methods!=null){
		  for(Method method:methods){	
			  List<Method> list = classMethodMap.get(method.getName());
			  if(list==null){
				 list = new ArrayList<Method>();
				 classMethodMap.put(method.getName(), list);
			  }
			  list.add(method);
		  }
		  //需要对方法列表进行排序
		  for(List<Method> list:classMethodMap.values()){
			  Collections.sort(list, methodComparator);
		  }
		}
		methodCache.put(clazz, classMethodMap);
	}
	
	/**
	 * 删除方法缓存
	 * @param clazz
	 */
	public static  void removeClassMethod(Class<?> clazz){
		methodCache.remove(clazz);
	}
	
	/**
	 * 获取指定名称的方法列表
	 * @param clazz
	 * @param methodName
	 * @return
	 */
	public static  List<Method> getClassMethod(Class<?> clazz,String methodName){
		Map<String,List<Method>> classMethodMap = methodCache.get(clazz);
		if(classMethodMap==null){
		   addClassMethod(clazz);
		   classMethodMap = methodCache.get(clazz);
		}
		return classMethodMap.get(methodName);
	}
	
	/**
	 * 判断参数值是否为类型的实例
	 * @param type
	 * @param value
	 * @return
	 */
	public static boolean isInstance(Class<?> type,Object value){
		if(simpleClassTypes.containsKey(type)){
		   if(value==null){
			  return false;
		   }
		   return simpleClassTypes.get(type).isInstance(value);
		}else{
		   if(value==null){
			  return true;
		   }
		   return type.isInstance(value);
		}
	}
	
	/**
	 * 调用克隆方法
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static Object clone(Object object) throws Exception{
		return MethodUtils.invokeMethod(object, "clone", null);
	}
	
	/**
	 * 安全调用克隆方法
	 * @param object
	 * @return
	 */
	public static Object safeClone(Object object) {
		try {
			return clone(object);
		} catch (Exception e) {
			return object;
		}
	}
	
	
	 /**
	  * 判断是否匹配当前java方法
	  * @param method
	  * @param parameters
	  * @return
	  */
	 static boolean checkMethod(Method method,Object[] parameters){
		 //空参数列表
		 if(parameters==null || parameters.length==0){
			return true; 
		 }
		 for(int i=0;i<parameters.length;i++){
			 Class<?> parameterType = method.getParameterTypes()[i];
			 MethodParameterRule p = findMethodParameterRule(parameterType,parameters[i]);
			 if(p==null){
				return false; //没有匹配规则失败
			 }
		 }
		 return true;
	 }
	 
	 static MethodParameterRule findMethodParameterRule(Class<?> parameterType,Object parameter){
		 for(MethodParameterRule rule:rules){
			 if(rule.isMatch(parameterType, parameter)){
				return rule;
			 }
		 }
		 return null;
	 }
	 
	 /**
	  * 对方法参数是否进行包装
	  * @param method
	  * @param parameters
	  * @return
	  */
     static Object[] wrapper(ScriptContext context,Method method,Object[] parameters){
		//空参数列表
		 if(parameters==null || parameters.length==0){
			return parameters;
		 }
		 Object[] newParameters = new Object[parameters.length];
		 for(int i=0;i<parameters.length;i++){
			 Class<?> parameterType = method.getParameterTypes()[i];
			 MethodParameterRule p = findMethodParameterRule(parameterType,parameters[i]);
			 if(p!=null){
				 newParameters[i] = p.convert(context,parameterType, parameters[i]);
			 }else{
				 newParameters[i] = parameters[i];
			 }
		 }
		 return newParameters;
	 }
     
     /**
      * 转换方法参数
      * @param context
      * @param parameterType
      * @param parameter
      * @return
      */
     static Object convert(ScriptContext context,Class<?> parameterType,Object parameter){
    	 MethodParameterRule p = findMethodParameterRule(parameterType,parameter);
    	 if(p!=null){
    		return p.convert(context,parameterType, parameter);
    	 }else{
    		return parameter;
    	 }
     }
     
     
	static class MethodComparator implements Comparator<Method>{
       
		public int compare(Method m1, Method m2) {
			int n1 = countMethod(m1);
			int n2 = countMethod(m2);
			if(n1>n2){
			   return 1;
			}else if(n1<n2){
			   return -1;
			}
			return 0;
		}
		
		private int countMethod(Method m){
			final int prime = 31;
			int result = 1;
			Class<?>[] types = m.getParameterTypes();
			if(types!=null){
			   for(Class<?> type:types){
				  if(type.equals(Object.class)){  
					  result = result*prime+2;
				  }else{
					  result = result*prime+1;
				  }
			   }
			}
			return result;
		}
    	 
    }
}
