package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.SingleRightExpressionContext;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptResult;

public class MathSingleRightProcessor implements ParserRuleContextProcessor<TinyScriptParser.SingleRightExpressionContext>{

	public Class<SingleRightExpressionContext> getType() {
		return TinyScriptParser.SingleRightExpressionContext.class;
	}

	public ScriptResult process(SingleRightExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		String name = parseTree.getChild(0).getText();
		String op = parseTree.getChild(1).getText();
		Object value = interpret.interpretParseTreeValue(parseTree.getChild(0), segment, context);
		ExpressionUtil.executeOperationWithContext(context, op, name, value);
		return new ScriptResult(value);
	}

}
