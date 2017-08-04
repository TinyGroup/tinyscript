package org.tinygroup.tinyscript.interpret.context;

import java.util.List;

import org.springframework.util.CollectionUtils;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.BlockStatementContext;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptResult;

/**
 * Created by luoguo on 16/8/10.
 */
public class MethodDeclarationContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.MethodDeclarationContext>{
    public Class<TinyScriptParser.MethodDeclarationContext> getType() {
        return TinyScriptParser.MethodDeclarationContext.class;
    }

    public ScriptResult process(TinyScriptParser.MethodDeclarationContext parseTree, ScriptInterpret interpret, ScriptSegment segment, ScriptContext context) throws Exception {
    	List<BlockStatementContext> blockStatementContextList = parseTree.methodBody().block().blockStatement();
    	
    	if(!CollectionUtils.isEmpty(blockStatementContextList)){
    	   for(BlockStatementContext blockStatementContext:blockStatementContextList){
    		   interpret.interpretParseTree(blockStatementContext, segment, context);
    	   }
    	}
    	return ScriptResult.VOID_RESULT;
    }
}
