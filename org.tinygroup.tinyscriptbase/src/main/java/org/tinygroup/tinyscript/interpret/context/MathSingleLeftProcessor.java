package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.SingleLeftExpressionContext;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptResult;

public class MathSingleLeftProcessor implements ParserRuleContextProcessor<TinyScriptParser.SingleLeftExpressionContext>{

	public Class<SingleLeftExpressionContext> getType() {
		return TinyScriptParser.SingleLeftExpressionContext.class;
	}

	public ScriptResult process(SingleLeftExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		String name = parseTree.getChild(1).getText();
		String op = parseTree.getChild(0).getText();
		Object value = interpret.interpretParseTreeValue(parseTree.getChild(1), segment, context);
		Object newValue = ExpressionUtil.executeOperationWithContext(context, "l"+op, name, value);
		return new ScriptResult(newValue);
	}

}
