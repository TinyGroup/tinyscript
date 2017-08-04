package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.DoContext;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.exception.BreakException;
import org.tinygroup.tinyscript.interpret.exception.ContinueException;

public class DoContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.DoContext>{

	public Class<DoContext> getType() {
		return TinyScriptParser.DoContext.class;
	}

	public ScriptResult process(DoContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		try{
			do{
				try{
	        		interpret.interpretParseTree(parseTree.statement(), segment, context);
	        	}catch(ContinueException e){
	        		continue;
	        	}catch(BreakException e){
	        		break;
	        	}
			}while(ExpressionUtil.getBooleanValue(interpret.interpretParseTreeValue(parseTree.parExpression().expression(), segment, context)));
		}catch(Exception e){
			throw new ScriptException(String.format("[%s]类型的ParserRuleContext处理发生异常", getType()),e);
		}
		
		return ScriptResult.VOID_RESULT;
	}

}
