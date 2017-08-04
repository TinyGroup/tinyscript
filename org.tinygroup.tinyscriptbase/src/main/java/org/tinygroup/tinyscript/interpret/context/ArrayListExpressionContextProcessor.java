package org.tinygroup.tinyscript.interpret.context;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.ArrayListExpressionContext;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.ExpressionContext;

public class ArrayListExpressionContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.ArrayListExpressionContext>{

	public Class<ArrayListExpressionContext> getType() {
		return ArrayListExpressionContext.class;
	}

	public ScriptResult process(ArrayListExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		List<Object> paraList = new ArrayList<Object>();
		try{
			if(parseTree.expressionList()!=null){
		    	for(ExpressionContext expressionContext:parseTree.expressionList().expression()){
		    		if(expressionContext!=null){
		    			paraList.add(interpret.interpretParseTreeValue(expressionContext, segment, context));
		    		}
		    	}
		    }
		}catch(Exception e){
			throw new ScriptException(String.format("[%s]类型的ParserRuleContext处理发生异常", getType()),e);
		}
	    
		return new ScriptResult(paraList);
	}

}
