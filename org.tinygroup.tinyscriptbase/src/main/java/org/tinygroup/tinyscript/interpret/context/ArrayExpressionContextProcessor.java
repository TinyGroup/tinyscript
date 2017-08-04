package org.tinygroup.tinyscript.interpret.context;

import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.ArrayExpressionContext;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.ExpressionContext;

public class ArrayExpressionContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.ArrayExpressionContext>{

	public Class<ArrayExpressionContext> getType() {
		return ArrayExpressionContext.class;
	}

	public ScriptResult process(ArrayExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		int n = 0;
		if(parseTree.expressionList()!=null){
		   n = parseTree.expressionList().expression()==null?0:parseTree.expressionList().expression().size();
		}
		Object[] array = new Object[n];
		if(n>0){
			List<ExpressionContext> expressionContextList = parseTree.expressionList().expression();
			for(int i=0;i<n;i++){
				ExpressionContext expressionContext = expressionContextList.get(i);
				array[i] = interpret.interpretParseTreeValue(expressionContext, segment, context);
			}
		}
		return new ScriptResult(array);
	}

	
}
