package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.ConditionalTernaryExpressionContext;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptResult;

public class MathConditionalTernaryProcessor implements ParserRuleContextProcessor<TinyScriptParser.ConditionalTernaryExpressionContext>{

	public Class<ConditionalTernaryExpressionContext> getType() {
		return TinyScriptParser.ConditionalTernaryExpressionContext.class;
	}

	public ScriptResult process(ConditionalTernaryExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		boolean condition = ExpressionUtil.getBooleanValue(interpret.interpretParseTreeValue(parseTree.expression(0), segment, context));
		if(condition){
			return interpret.interpretParseTree(parseTree.expression(1), segment, context);
		}else{
			return interpret.interpretParseTree(parseTree.expression(2), segment, context);
		}
	}

}
