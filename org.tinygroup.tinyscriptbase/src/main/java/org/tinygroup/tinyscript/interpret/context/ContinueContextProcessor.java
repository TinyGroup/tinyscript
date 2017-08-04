package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.exception.ContinueException;

/**
 * Created by luoguo on 16/8/10.
 */
public class ContinueContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.ContinueContext>{
    public Class<TinyScriptParser.ContinueContext> getType() {
        return TinyScriptParser.ContinueContext.class;
    }

    public ScriptResult process(TinyScriptParser.ContinueContext parseTree, ScriptInterpret interpret, ScriptSegment segment, ScriptContext context) throws Exception {
        if(parseTree.expression()!=null){
        	//不符合表达式不做任何操作
        	if(!ExpressionUtil.getBooleanValue(interpret.interpretParseTreeValue(parseTree.expression(), segment, context))){
        		return ScriptResult.VOID_RESULT;
        	}
        }
        //执行循环
        throw new ContinueException();
    }
}
