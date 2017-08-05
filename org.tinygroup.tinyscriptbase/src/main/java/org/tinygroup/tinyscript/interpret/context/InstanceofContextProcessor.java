package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ClassInstanceUtil;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;

public class InstanceofContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.InstanceofExpressionContext>{

	public Class<TinyScriptParser.InstanceofExpressionContext> getType() {
		return TinyScriptParser.InstanceofExpressionContext.class;
	}

	public ScriptResult process(TinyScriptParser.InstanceofExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		Object a = interpret.interpretParseTreeValue(parseTree.expression().get(0), segment, context);
		Object b = interpret.interpretParseTreeValue(parseTree.expression().get(1), segment, context);
		
		return new ScriptResult(ClassInstanceUtil.isInstance(a, b));
	}

}