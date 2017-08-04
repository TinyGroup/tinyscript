package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.MathBinaryBitwiseContext;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptResult;

public class MathBinaryBitwiseProcessor implements ParserRuleContextProcessor<TinyScriptParser.MathBinaryBitwiseContext>{

	public Class<MathBinaryBitwiseContext> getType() {
		return TinyScriptParser.MathBinaryBitwiseContext.class;
	}

	public ScriptResult process(MathBinaryBitwiseContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		Object a = interpret.interpretParseTreeValue(parseTree.expression().get(0), segment, context);
		Object b = interpret.interpretParseTreeValue(parseTree.expression().get(1), segment, context);
		Object value = ExpressionUtil.executeOperation(parseTree.getChild(1).getText(), a,b);
		return new ScriptResult(value);
	}

}
