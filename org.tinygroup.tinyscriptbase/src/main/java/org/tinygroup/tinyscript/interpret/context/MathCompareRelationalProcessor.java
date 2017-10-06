package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.LogicalCompareExpressionContext;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.exception.RunScriptException;

public class MathCompareRelationalProcessor implements ParserRuleContextProcessor<TinyScriptParser.LogicalCompareExpressionContext>{

	public Class<LogicalCompareExpressionContext> getType() {
		return TinyScriptParser.LogicalCompareExpressionContext.class;
	}

	public ScriptResult process(LogicalCompareExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		String aName = null;
		String bName = null;
		String op=null;
		Object a = null;
		Object b = null;
		try{
			aName = parseTree.expression().get(0).getText();
			bName = parseTree.expression().get(1).getText();
			op = parseTree.getChild(1).getText();
			a = interpret.interpretParseTreeValue(parseTree.expression().get(0), segment, context);
			b = interpret.interpretParseTreeValue(parseTree.expression().get(1), segment, context);
			Object value = ExpressionUtil.executeOperation(parseTree.getChild(1).getText(), a,b);
			return new ScriptResult(value);
		}catch(Exception e){
			throw new RunScriptException(e,parseTree,segment,ScriptException.ERROR_TYPE_EXPRESSION,ResourceBundleUtil.getDefaultMessage("context.math.error3", aName,a,bName,b,op));
		}
		
	}

}
