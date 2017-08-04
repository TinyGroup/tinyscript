package org.tinygroup.tinyscript.interpret.context;

import java.util.List;

import org.springframework.util.CollectionUtils;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.StatementContext;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.StatementDeclarationContext;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptResult;

public class StatementDeclarationContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.StatementDeclarationContext>{

	public Class<StatementDeclarationContext> getType() {
		return TinyScriptParser.StatementDeclarationContext.class;
	}

	public ScriptResult process(StatementDeclarationContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		List<StatementContext> statementContextList = parseTree.statement();
		ScriptResult result = ScriptResult.VOID_RESULT;
		if(!CollectionUtils.isEmpty(statementContextList)){
		   for(StatementContext statementContext:statementContextList){
			   result = interpret.interpretParseTree(statementContext, segment, context);
		   }
		}
		return result;
	}


}
