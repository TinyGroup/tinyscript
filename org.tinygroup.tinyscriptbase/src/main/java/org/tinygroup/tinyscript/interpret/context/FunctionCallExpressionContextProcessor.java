package org.tinygroup.tinyscript.interpret.context;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptClassInstance;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.impl.ScriptMethodContext;
import org.tinygroup.tinyscript.interpret.FunctionCallUtil;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.call.FunctionCallExpressionParameter;
import org.tinygroup.tinyscript.interpret.exception.RunScriptException;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.ExpressionContext;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.FunctionCallExpressionContext;

public class FunctionCallExpressionContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.FunctionCallExpressionContext>{

	public Class<FunctionCallExpressionContext> getType() {
		return TinyScriptParser.FunctionCallExpressionContext.class;
	}

	public ScriptResult process(FunctionCallExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		String name = null;
		try{
			    Object object = interpret.interpretParseTreeValue(parseTree.expression(), segment, context);
			    name = parseTree.Identifier().getText();
			    
			    ScriptMethodContext scriptMethodContext = new ScriptMethodContext();
			    scriptMethodContext.setParent(context);
			    if(object instanceof ScriptClassInstance){
			    	scriptMethodContext.setScriptClassInstance((ScriptClassInstance)object);
			    }
			    
			    List<Object> paraList = new ArrayList<Object>();
			    if(parseTree.expressionList()!=null){
			    	for(ExpressionContext expressionContext:parseTree.expressionList().expression()){
			    		if(expressionContext!=null){
			    			//延迟计算参数 
			    			paraList.add(new FunctionCallExpressionParameter(expressionContext, segment, context));
			    		}
			    	}
			    }
			    //统一方法调度入口
			    Object value = FunctionCallUtil.operate(segment, scriptMethodContext, object, name, paraList.toArray());
			    if(value!=null && value instanceof ScriptResult){
			       return (ScriptResult) value;
			    }
			    return new ScriptResult(value);
			    
		}catch(NoSuchMethodException e){
			throw new RunScriptException(e,parseTree,segment,ScriptException.ERROR_TYPE_FUNCTION,ResourceBundleUtil.getDefaultMessage("context.call.error1", name));
		}catch(Exception e){
			throw new RunScriptException(e,parseTree,segment,ScriptException.ERROR_TYPE_FUNCTION,ResourceBundleUtil.getDefaultMessage("context.call.error2", name));
		}
	    
	}
	
}
