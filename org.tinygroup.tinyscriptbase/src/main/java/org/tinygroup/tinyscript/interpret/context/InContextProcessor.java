package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.exception.RunScriptException;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.InExpressionContext;

/**
 * 处理in指令
 * @author yancheng11334
 *
 */
public class InContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.InExpressionContext>{

	public Class<InExpressionContext> getType() {
		return InExpressionContext.class;
	}

	public ScriptResult process(InExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		try{
			Object item = interpret.interpretParseTreeValue(parseTree.expression().get(0), segment, context);
			Object collection = interpret.interpretParseTreeValue(parseTree.expression().get(1), segment, context);
			
		    boolean tag = ExpressionUtil.in(collection, item);
			return new ScriptResult(tag);
		}catch(Exception e){
			throw new RunScriptException(e,parseTree,segment,ScriptException.ERROR_TYPE_DIRECTIVE,ResourceBundleUtil.getDefaultMessage("context.directive.error", "in"));
		}
		
	}

}
