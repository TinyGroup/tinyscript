package org.tinygroup.tinyscript.interpret.context;

import org.apache.commons.lang.ObjectUtils;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.CaseSwitchBlockContext;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.SwitchContext;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.exception.BreakException;

public class SwitchContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.SwitchContext>{

	public Class<SwitchContext> getType() {
		return TinyScriptParser.SwitchContext.class;
	}

	public ScriptResult process(SwitchContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		Object value = interpret.interpretParseTreeValue(parseTree.parExpression(), segment, context);
		
		//执行case块
		boolean matchTag = false;
		if(parseTree.caseSwitchBlock()!=null){
		   for(CaseSwitchBlockContext caseSwitchBlockContext:parseTree.caseSwitchBlock()){
			   Object caseValue = interpret.interpretParseTreeValue(caseSwitchBlockContext.constantExpression().expression(), segment, context);
			   if(ObjectUtils.equals(value, caseValue)){
				   matchTag = true;
				   try{
					   interpret.interpretParseTree(caseSwitchBlockContext.blockStatement(), segment, context);
				   }catch(BreakException e){
					   break;
				   }
			   }
		   }
		}
		
		//执行默认块
		if(!matchTag && parseTree.defaultSwitchBlock()!=null){
		   interpret.interpretParseTree(parseTree.defaultSwitchBlock().blockStatement(), segment, context);
		}
		
		return ScriptResult.VOID_RESULT;
	}

}
