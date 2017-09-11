package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.MathUnaryPrefixExpressionContext;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.exception.RunScriptException;

public class MathUnaryPrefixProcessor implements ParserRuleContextProcessor<TinyScriptParser.MathUnaryPrefixExpressionContext>{

	public Class<MathUnaryPrefixExpressionContext> getType() {
		return TinyScriptParser.MathUnaryPrefixExpressionContext.class;
	}

	public ScriptResult process(MathUnaryPrefixExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		String op = parseTree.getChild(0).getText();
		String name = parseTree.getChild(1).getText();
		try{
			if("+".equals(op) || "-".equals(op)){
				return new ScriptResult(ExpressionUtil.executeOperation("l"+op, interpret.interpretParseTreeValue(parseTree.getChild(1), segment, context)));
			}else{
				return new ScriptResult(ExpressionUtil.executeOperation(op, interpret.interpretParseTreeValue(parseTree.getChild(1), segment, context)));
			}
		}catch(Exception e){
			throw new RunScriptException(e,parseTree,segment,ScriptException.ERROR_TYPE_EXPRESSION,String.format("%s进行%s操作发生异常", name,op));
		}
		
	}

}
