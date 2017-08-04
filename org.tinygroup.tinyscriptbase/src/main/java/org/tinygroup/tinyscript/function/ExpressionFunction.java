package org.tinygroup.tinyscript.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;

/**
 * 可执行表达式的函数
 * @author yancheng11334
 *
 */
public abstract class ExpressionFunction extends AbstractScriptFunction {

	protected final static String END_TAG = ";";
	protected final static String ELEMENT_TAG = "#";
	protected final static String ELEMENT_NAME = "_tiny_element";
	
	/**
	 * 执行表达式
	 * @param expression
	 * @param context
	 * @return
	 * @throws ScriptException
	 */
	public boolean execute(String expression, ScriptContext context) throws ScriptException{
		return (Boolean)getScriptEngine().execute(expression,context);
	}
	
	/**
	 * 检查表达式
	 * @param expression
	 * @return
	 */
	protected String checkExpression(String expression){
		expression = expression.replaceAll(ELEMENT_TAG, ELEMENT_NAME);
		if(!expression.startsWith("return")){
			expression = "return "+expression;
		}
		if(!expression.endsWith(";")){
			expression = expression+";";
		}
		return expression;
	}
}
