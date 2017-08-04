package org.tinygroup.tinyscript.interpret.newinstance;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;

/**
 * java构造器辅助类
 * @author yancheng11334
 *
 */
public class JavaConstructorUtil {
     
	private static List<ConstructorParameterRule> rules = new ArrayList<ConstructorParameterRule>();
	
	static{
		addConstructorParameterRule(new InstanceRule());
		addConstructorParameterRule(new SimpleTypeRule());
		addConstructorParameterRule(new LambdaRule());
	}
	
	/**
	 * 添加构造器规则
	 * @param rule
	 */
	public static void addConstructorParameterRule(ConstructorParameterRule rule){
//		if(!rules.contains(rule)){
//			rules.add(rule);
//		}
		for(ConstructorParameterRule constructorParameterRule:rules){
		   if(constructorParameterRule.equals(rule) || constructorParameterRule.getClass().isInstance(rule)){
			  return ;
		   }
		}
		rules.add(rule);
	}
	
	/**
	 * 删除构造器规则
	 * @param rule
	 */
	public static void removeConstructorParameterRule(ConstructorParameterRule rule){
		rules.remove(rule);
	}
	
	static ConstructorParameterRule findConstructorParameterRule(Class<?> parameterType,Object parameter){
		for(ConstructorParameterRule rule:rules){
			if(rule.isMatch(parameterType, parameter)){
			   return rule;
			}
		}
		return null;
	}
	
	/**
	 * 检查构造参数
	 * @param c
	 * @param paramList
	 * @return
	 */
	static  boolean  checkConstructor(Constructor<?> c,List<Object> paramList){
		for(int i=0;i<paramList.size();i++){
			Class<?> parameterType = c.getParameterTypes()[i];
			Object parameter = paramList.get(i);
			ConstructorParameterRule rule = findConstructorParameterRule(parameterType,parameter);
			if(rule==null){
			   return false;
			}
		}
		return true;
	}
	
	/**
	 * 对构造参数进行包装
	 * @param context
	 * @param c
	 * @param paramList
	 * @return
	 */
	static  Object[] wrapper(ScriptContext context,Constructor<?> c,List<Object> paramList){
		Object[] newParameters = new Object[paramList.size()];
		for(int i=0;i<paramList.size();i++){
			Class<?> parameterType = c.getParameterTypes()[i];
			Object parameter = paramList.get(i);
			ConstructorParameterRule rule = findConstructorParameterRule(parameterType,parameter);
			if(rule!=null){
				newParameters[i] = rule.convert(context, parameterType, parameter);
			}else{
				newParameters[i] = parameter;
			}
		}
		return newParameters;
	}
}
