package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ClassInstanceUtil;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.exception.RunScriptException;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;

public class InstanceofContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.InstanceofExpressionContext>{

	public Class<TinyScriptParser.InstanceofExpressionContext> getType() {
		return TinyScriptParser.InstanceofExpressionContext.class;
	}

	public ScriptResult process(TinyScriptParser.InstanceofExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		try{
			Object a = interpret.interpretParseTreeValue(parseTree.expression().get(0), segment, context);
			Object b = interpret.interpretParseTreeValue(parseTree.expression().get(1), segment, context);
			
			return new ScriptResult(ClassInstanceUtil.isInstance(a, b));
		}catch(Exception e){
			throw new RunScriptException(e,parseTree,segment,ScriptException.ERROR_TYPE_DIRECTIVE,ResourceBundleUtil.getDefaultMessage("context.directive.error", "instance"));
		}
		
	}

}
