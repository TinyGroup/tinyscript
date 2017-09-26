package org.tinygroup.tinyscript.function;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptFunction;
import org.tinygroup.tinyscript.config.FunctionConfig;
import org.tinygroup.tinyscript.config.ScriptFunctionConfig;
import org.tinygroup.tinyscript.interpret.ExpressionParameter;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 抽象脚本函数
 * @author yancheng11334
 *
 */
public abstract class AbstractScriptFunction implements ScriptFunction{

	private ScriptEngine scriptEngine;
	
	public void setScriptEngine(ScriptEngine engine) {
		scriptEngine = engine;
	}

	public ScriptEngine getScriptEngine() {
		return scriptEngine;
	}
	
	public boolean  enableExpressionParameter(){
		return false;
	}

	public String getBindingTypes() {
		return null;
	}

	/**
	 * 获得参数实际值
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	protected Object getValue(Object parameter) throws Exception{
		if(parameter!=null && parameter instanceof ExpressionParameter){
		   return ((ExpressionParameter)parameter).eval();
		}
		return parameter;
	}
	
	/**
	 * 获得参数的表达式
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	protected String getExpression(Object parameter) throws Exception{
		if(parameter!=null && parameter instanceof ExpressionParameter){
		   return ((ExpressionParameter)parameter).getExpression();
		}
		return (String)parameter;
	}
	
	/**
	 * 执行动态布尔表达式
	 * @param expression
	 * @param context
	 * @return
	 * @throws ScriptException
	 */
	public boolean executeDynamicBoolean(String expression, ScriptContext context) throws ScriptException{
		Object result = getScriptEngine().execute(expression,context);
		if(result instanceof Boolean){
		   //逻辑表达式
		   return (Boolean) result;
		}else if(result instanceof String){
		   //逻辑表达式的字符串
		   String newExpression = (String) result;
		   newExpression  = convertExpression(newExpression);
		   return (Boolean)getScriptEngine().execute(newExpression,context);
		}else{
		   throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.unrecognized.class", result.getClass().getName()));
		}
	}
	
	/**
	 * 执行动态对象表达式
	 * @param expression
	 * @param context
	 * @return
	 * @throws ScriptException
	 */
	@SuppressWarnings("unchecked")
	public <T> T executeDynamicObject(String expression, ScriptContext context) throws ScriptException{
		Object result = getScriptEngine().execute(expression,context);
		if(result instanceof String){
		   //逻辑表达式的字符串
		   String newExpression = (String) result;
		   newExpression  = convertExpression(newExpression);
		   return (T)getScriptEngine().execute(newExpression,context);
		}
		return (T) result;
	}
	
	/**
	 * 转换一般表达式为可执行脚本
	 * @param expression
	 * @return
	 */
	protected String convertExpression(String expression){
		if(!expression.startsWith("return")){
			expression = "return "+expression;
		}
		if(!expression.endsWith(";")){
			expression = expression+";";
		}
		return expression;
	}
	
	/**
	 * 检查函数的输入参数
	 * @param parameters
	 * @param num
	 * @return
	 */
	protected boolean checkParameters(Object[] parameters,int num){
		if(parameters!=null && parameters.length==num){
			//参数不能为null
		    for(Object parameter:parameters){
		       if(parameter==null){
		    	  return false;
		       }
		    }
			return true;
		}
		//检验参数失败
		return false;
	}
	
	/**
	 * 返回函数名的通用处理方法
	 */
	public List<FunctionConfig> getFunctionConfigs(){
		//如果需要返回详细描述、字段等特征，需要的函数自行覆盖实现
		List<FunctionConfig> configs = new ArrayList<FunctionConfig>();
		String names = getNames();
		if(names!=null){
		   String[] ss = names.split(",");
		   for(String name:ss){
			   if(!StringUtil.isEmpty(name)){
				  configs.add(new ScriptFunctionConfig(name)); 
			   }
		   }
		}
		return configs;
	}
}
