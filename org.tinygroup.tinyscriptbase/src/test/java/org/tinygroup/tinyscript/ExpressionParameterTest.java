package org.tinygroup.tinyscript;

import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptEngine;
import org.tinygroup.tinyscript.interpret.ExpressionParameter;

import junit.framework.TestCase;

/**
 * 测试表达式
 * @author yancheng11334
 *
 */
public class ExpressionParameterTest extends TestCase{

	private ScriptEngine scriptEngine;

	protected void setUp() throws Exception {
		scriptEngine = new DefaultScriptEngine();
		scriptEngine.start();
	}
	
	public void testBase() throws Exception{
		scriptEngine.addScriptFunction(new ShowFunction());
		assertEquals(6, scriptEngine.execute("return show(6);"));
		assertEquals(6, scriptEngine.execute("return show(1+2+3);"));
		
		scriptEngine.addScriptFunction(new SameFunction()); //带双引号的表达式
		assertEquals(false, scriptEngine.execute("return same(\"1\",\"2\",\"str1==str2\");"));
		assertEquals(true, scriptEngine.execute("return same(\"2\",\"2\",\"str1==str2\");"));
		
		scriptEngine.addScriptFunction(new Same2Function()); //不带双引号的表达式
		assertEquals(false, scriptEngine.execute("return same2(\"1\",\"2\",str3==str4);"));
		assertEquals(true, scriptEngine.execute("return same2(\"2\",\"2\",str3==str4);"));
	}
	
	class ShowFunction extends AbstractScriptFunction{

		public String getNames() {
			return "show";
		}
		
		public boolean  enableExpressionParameter(){
			return true;
		}

		public Object execute(ScriptSegment segment, ScriptContext context,
				Object... parameters) throws ScriptException {
			try{
				if(parameters[0] instanceof ExpressionParameter){
		        	ExpressionParameter expressionParameter = (ExpressionParameter) parameters[0];
		        	return expressionParameter.eval();
		        }else{
		        	return parameters[0];
		        }
			}catch(Exception e){
				throw new ScriptException(e);
			}
		}
	}
	
	class SameFunction extends AbstractScriptFunction{

		public String getNames() {
			return "same";
		}

		public Object execute(ScriptSegment segment, ScriptContext context,
				Object... parameters) throws ScriptException {
			try{
				String a = (String) parameters[0];
				String b = (String) parameters[1];
				context.put("str1", a);
				context.put("str2", b);
				String expression = "return "+ parameters[2].toString()+";";
				return getScriptEngine().execute(expression, context);
			}catch(Exception e){
				throw new ScriptException(e);
			}
		}
		
	}
	
	class Same2Function extends AbstractScriptFunction{

		public String getNames() {
			return "same2";
		}
		
		public boolean  enableExpressionParameter(){
			return true;
		}

		public Object execute(ScriptSegment segment, ScriptContext context,
				Object... parameters) throws ScriptException {
			try{
				String a = (String) getValue(parameters[0]);
				String b =  (String) getValue(parameters[1]);
				context.put("str3", a);
				context.put("str4", b);
				String expression = "return "+ getExpression(parameters[2])+";";
				return getScriptEngine().execute(expression, context);
			}catch(Exception e){
				throw new ScriptException(e);
			}
		}
		
	}
}
