package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.exception.RunScriptException;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.LogicalConnectExpressionContext;

public class MathLogicalConnectProcessor implements ParserRuleContextProcessor<TinyScriptParser.LogicalConnectExpressionContext>{

	public Class<LogicalConnectExpressionContext> getType() {
		return TinyScriptParser.LogicalConnectExpressionContext.class;
	}

	public ScriptResult process(LogicalConnectExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		String name = null;
		String op = null;
		try{
			
			Object a = interpret.interpretParseTreeValue(parseTree.expression().get(0), segment, context);
			name = parseTree.expression().get(0).getText();
			op = parseTree.getChild(1).getText();
			
			if(checkShortAnd(a,op)){
				//执行短路与中断
				return new ScriptResult(a);
			}else if(checkShortOr(a,op)){
				//执行短路或中断
				return new ScriptResult(a);
			}else{
				//执行正常逻辑运算
				Object b = interpret.interpretParseTreeValue(parseTree.expression().get(1), segment, context);
				return new ScriptResult(ExpressionUtil.executeOperation(op, a,b));
			}
			
		}catch(Exception e){
			throw new RunScriptException(e,parseTree,segment,ScriptException.ERROR_TYPE_EXPRESSION,ResourceBundleUtil.getDefaultMessage("context.math.error5", name,op));
		}
		
	}
	
	//检查短路与(首项为假,之后逻辑无需判断)
	private boolean checkShortAnd(Object obj,String op){
		if(obj!=null && obj instanceof Boolean && "&&".equals(op)){
		   return !((Boolean)obj).booleanValue();
		}
		return false;
	}
	
	//检查短路或(首项为真,之后逻辑无需判断)
	private boolean checkShortOr(Object obj,String op){
		if(obj!=null && obj instanceof Boolean && "||".equals(op)){
		   return ((Boolean)obj).booleanValue();
		}
		return false;
	}

}
