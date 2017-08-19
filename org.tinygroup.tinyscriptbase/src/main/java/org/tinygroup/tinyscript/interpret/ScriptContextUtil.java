package org.tinygroup.tinyscript.interpret;

import org.tinygroup.tinyscript.ScriptClassInstance;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptException;

/**
 * 脚本上下文工具类
 * @author yancheng11334
 *
 */
public class ScriptContextUtil {

	private static final String LAMBDA_PRE = "$lambda";
	
	private static final String INSTANCE_NAME = "$intance";
	
	private static final String ENGINE_NAME = "$scriptEngine";
	
	private static final String DYNAMIC_FUNCTION_NAME = "$dynamicFunctionName";
	
	public static void setLambdaFunction(ScriptContext context,LambdaFunction function){
		if(context!=null && function!=null){
		   if(function.getFunctionName()!=null){
			   //有名字的lambda函数
			   context.put(LAMBDA_PRE+function.getFunctionName(), function);
		   }else{
			   //匿名lambda函数
			   context.put(LAMBDA_PRE, function);
		   }
		}
	}
	
	public static LambdaFunction getLambdaFunction(ScriptContext context,String functionName){
		if(context!=null){
		   if(functionName!=null){
			   return context.get(LAMBDA_PRE+functionName);
		   }else{
			   return context.get(LAMBDA_PRE);
		   }
		}
		return null;
	}
	
	public static ScriptClassInstance getScriptClassInstance(ScriptContext context){
		if(context!=null){
		   return (ScriptClassInstance) context.get(INSTANCE_NAME);
		}
		return null;
	}
	
	public static void setScriptClassInstance(ScriptContext context,ScriptClassInstance instance){
		if(context!=null){
		   context.put(INSTANCE_NAME, instance);
		}
	}
	
	public static ScriptEngine getScriptEngine(ScriptContext context){
		return context.get(ENGINE_NAME);
	}
	
	public static void setScriptEngine(ScriptContext context,ScriptEngine engine){
		context.put(ENGINE_NAME, engine);
	}
	
	public static String getDynamicFunctionName(ScriptContext context){
		return context.get(DYNAMIC_FUNCTION_NAME);
	}
	
	public static void addDynamicFunctionName(ScriptContext context,String name){
		context.put(DYNAMIC_FUNCTION_NAME, name);
	}
	
	public static void removeDynamicFunctionName(ScriptContext context,String name){
		String value = context.get(DYNAMIC_FUNCTION_NAME);
		if(name!=null && name.equals(value)){
		   context.remove(DYNAMIC_FUNCTION_NAME);
		}
	}
	
	/**
	 * 转换一般表达式为可执行脚本
	 * 
	 * @param expression
	 * @return
	 */
	public static String convertExpression(String expression) {
		if (expression.indexOf("->")>0){
			return expression;
		}
		if (!expression.startsWith("return")) {
			expression = "return " + expression;
		}
		if (!expression.endsWith(";")) {
			expression = expression + ";";
		}
		return expression;
	}
	
	/**
	 * 执行脚本
	 * @param context
	 * @param expression
	 * @return
	 * @throws ScriptException
	 */
	public static Object executeExpression(ScriptContext context,String expression) throws ScriptException {
		String newExpression = convertExpression(expression);
		ScriptEngine engine = getScriptEngine(context);
		return engine.execute(newExpression, context);
	}
	
	/**
	 * 批量执行脚本
	 * @param context
	 * @param expressions
	 * @return
	 * @throws ScriptException
	 */
	public static Object[] executeExpression(ScriptContext context,String... expressions) throws ScriptException {
		Object[] result = new Object[expressions.length];
		ScriptEngine engine = getScriptEngine(context);
		for(int i=0;i<result.length;i++){
			result[i] = engine.execute(convertExpression(expressions[i]), context);
		}
		return result;
	}
}
