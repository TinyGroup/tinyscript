package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.assignvalue.AssignValueUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptResult;

public class VariableDeclaratorContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.VariableDeclaratorContext>{

	public Class<TinyScriptParser.VariableDeclaratorContext> getType() {
		return TinyScriptParser.VariableDeclaratorContext.class;
	}

	public ScriptResult process(
			TinyScriptParser.VariableDeclaratorContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		if(parseTree.getChildCount()==3){
		   String name =  parseTree.getChild(0).getText();
		   Object value = interpret.interpretParseTreeValue(parseTree.getChild(2), segment, context);
		   AssignValueUtil.operate(name, value, context);
		   return new ScriptResult(value);
		}else if(parseTree.getChildCount()==1){
		   String name =  parseTree.getChild(0).getText();
		   AssignValueUtil.operate(name, null, context);
		}
		return ScriptResult.NULL_RESULT;
	}

}
