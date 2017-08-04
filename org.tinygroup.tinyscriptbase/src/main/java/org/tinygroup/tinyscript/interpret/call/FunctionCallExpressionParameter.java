package org.tinygroup.tinyscript.interpret.call;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.impl.AbstractScriptEngine;
import org.tinygroup.tinyscript.interpret.ExpressionParameter;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.ExpressionContext;

/**
 * 默认的表达式参数实现
 * @author yancheng11334
 *
 */
public class FunctionCallExpressionParameter implements ExpressionParameter{

	private ExpressionContext expressionContext;
	private ScriptSegment segment;
	private ScriptContext context;
	private Object cache; //缓存计算结果(允许null)
	private boolean compute = false;
	
	public FunctionCallExpressionParameter(ExpressionContext expressionContext,
			ScriptSegment segment, ScriptContext context) {
		super();
		this.expressionContext = expressionContext;
		this.segment = segment;
		this.context = context;
	}

	public void setScriptEngine(ScriptEngine engine) {
		
	}

	public ScriptEngine getScriptEngine() {
		return segment.getScriptEngine();
	}

	public String getExpression() {
		return expressionContext.getText();
	}

	public Object eval() throws Exception {
		if(!compute){
		   cache = evalWithScriptInterpret();
		   compute = true;
		}
		return cache;
	}
	
	private Object evalWithScriptInterpret()  throws Exception {
		ScriptEngine engine = getScriptEngine();
		if(engine instanceof AbstractScriptEngine){
			AbstractScriptEngine abstractScriptEngine = (AbstractScriptEngine) engine;
			return abstractScriptEngine.getScriptInterpret().interpretParseTreeValue(expressionContext, segment, context);
		}else{
		   throw new ScriptException("本ScriptEngine实现不支持获取ScriptInterpret操作!");
		}
	}

}
