package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.BreakContext;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.exception.BreakException;

public class BreakContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.BreakContext>{

	public Class<BreakContext> getType() {
		return TinyScriptParser.BreakContext.class;
	}

	public ScriptResult process(BreakContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		if(parseTree.expression()!=null){
        	//不符合表达式不做任何操作
        	if(!ExpressionUtil.getBooleanValue(interpret.interpretParseTreeValue(parseTree.expression(), segment, context))){
        		return ScriptResult.VOID_RESULT;
        	}
        }
        //执行中断 
		throw new BreakException();
	}

}
