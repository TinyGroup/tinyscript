package org.tinygroup.tinyscript.interpret.context;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
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
		    }else if(parseTree.expressionRange()!=null){
		    	Object left = interpret.interpretParseTreeValue(parseTree.expressionRange().getChild(0), segment, context);
		    	Object right = interpret.interpretParseTreeValue(parseTree.expressionRange().getChild(2), segment, context);
		    	List<Object> result = ExpressionUtil.createRange(left, right);
		    	if(result!=null){
		    	   paraList.addAll(result);
		    	}
		    }
		}catch(Exception e){
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("context.common.error", getType()),e);
		}
	    
		return new ScriptResult(paraList);
	}

}
