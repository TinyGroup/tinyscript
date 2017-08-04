package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.exception.ReturnException;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.BlockStatementContext;

public class BlockStatementContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.BlockStatementContext>{

	public Class<BlockStatementContext> getType() {
		return TinyScriptParser.BlockStatementContext.class;
	}
	
	public ScriptResult process(BlockStatementContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		ScriptResult result = null;
		if(parseTree.localVariableDeclarationStatement()!=null){
			result = interpret.interpretParseTree(parseTree.localVariableDeclarationStatement(), segment, context);
		}
		if(parseTree.statement()!=null){
			result = interpret.interpretParseTree(parseTree.statement(), segment, context);
			if(parseTree.statement().getChild(0).getText().equals("return")){
			   //执行返回中断逻辑
			   throw new ReturnException(result.getResult());
			}
		}
		return result;
	}

	

}
