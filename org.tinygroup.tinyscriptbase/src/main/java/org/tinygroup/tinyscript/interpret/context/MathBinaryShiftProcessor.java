package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.MathBinaryShiftExpressionContext;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.exception.RunScriptException;

public class MathBinaryShiftProcessor implements ParserRuleContextProcessor<TinyScriptParser.MathBinaryShiftExpressionContext>{

	public Class<MathBinaryShiftExpressionContext> getType() {
		return TinyScriptParser.MathBinaryShiftExpressionContext.class;
	}

	public ScriptResult process(MathBinaryShiftExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		Object a = null;
		Object b = null;
		String aName = null;
		String bName = null;
		try{
			aName = parseTree.expression().get(0).getText();
			bName = parseTree.expression().get(1).getText();
			a = interpret.interpretParseTreeValue(parseTree.expression().get(0), segment, context);
			b = interpret.interpretParseTreeValue(parseTree.expression().get(1), segment, context);
			Object value = ExpressionUtil.executeOperation(parseTree.getChild(1).getText(), a,b);
			return new ScriptResult(value);
		}catch(Exception e){
			throw new RunScriptException(e,parseTree,segment,ScriptException.ERROR_TYPE_EXPRESSION,ResourceBundleUtil.getDefaultMessage("context.math.error1", aName,a,bName,b));
		}
	}

}
