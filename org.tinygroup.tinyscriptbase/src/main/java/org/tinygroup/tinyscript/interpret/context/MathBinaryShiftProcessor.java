package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.MathBinaryShiftExpressionContext;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptResult;

public class MathBinaryShiftProcessor implements ParserRuleContextProcessor<TinyScriptParser.MathBinaryShiftExpressionContext>{

	public Class<MathBinaryShiftExpressionContext> getType() {
		return TinyScriptParser.MathBinaryShiftExpressionContext.class;
	}

	public ScriptResult process(MathBinaryShiftExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		Object a = interpret.interpretParseTreeValue(parseTree.expression().get(0), segment, context);
		Object b = interpret.interpretParseTreeValue(parseTree.expression().get(1), segment, context);
		Object value = ExpressionUtil.executeOperation(parseTree.getChild(1).getText(), a,b);
		return new ScriptResult(value);
	}

}
