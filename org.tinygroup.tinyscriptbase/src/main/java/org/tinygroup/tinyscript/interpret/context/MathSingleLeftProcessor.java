package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.SingleLeftExpressionContext;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.exception.RunScriptException;

public class MathSingleLeftProcessor implements ParserRuleContextProcessor<TinyScriptParser.SingleLeftExpressionContext>{

	public Class<SingleLeftExpressionContext> getType() {
		return TinyScriptParser.SingleLeftExpressionContext.class;
	}

	public ScriptResult process(SingleLeftExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		String name = parseTree.getChild(1).getText();
		String op = parseTree.getChild(0).getText();
        try{
        	Object value = interpret.interpretParseTreeValue(parseTree.getChild(1), segment, context);
    		Object newValue = ExpressionUtil.executeOperationWithContext(context, "l"+op, name, value);
    		return new ScriptResult(newValue);
		}catch(Exception e){
			throw new RunScriptException(e,parseTree,segment,ScriptException.ERROR_TYPE_EXPRESSION,ResourceBundleUtil.getDefaultMessage("context.math.error2",  name,op));
		}
		
	}

}
