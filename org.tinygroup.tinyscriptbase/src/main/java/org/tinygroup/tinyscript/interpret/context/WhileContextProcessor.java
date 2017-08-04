package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.exception.BreakException;
import org.tinygroup.tinyscript.interpret.exception.ContinueException;

/**
 * Created by luoguo on 16/8/10.
 */
public class WhileContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.WhileContext>{
    public Class<TinyScriptParser.WhileContext> getType() {
        return TinyScriptParser.WhileContext.class;
    }

    public ScriptResult process(TinyScriptParser.WhileContext parseTree, ScriptInterpret interpret, ScriptSegment segment, ScriptContext context) throws Exception {
        while(ExpressionUtil.getBooleanValue(interpret.interpretParseTreeValue(parseTree.parExpression().expression(), segment, context))){
        	try{
        		interpret.interpretParseTree(parseTree.statement(), segment, context);
        	}catch(ContinueException e){
        		continue;
        	}catch(BreakException e){
        		break;
        	}
        }
        return ScriptResult.VOID_RESULT;
    }
}
