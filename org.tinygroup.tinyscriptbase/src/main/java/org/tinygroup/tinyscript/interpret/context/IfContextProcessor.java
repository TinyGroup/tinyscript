package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.ElseifDirectiveContext;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptResult;

/**
 * Created by luoguo on 16/8/10.
 */
public class IfContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.IfContext>{
    public Class<TinyScriptParser.IfContext> getType() {
        return TinyScriptParser.IfContext.class;
    }

    public ScriptResult process(TinyScriptParser.IfContext parseTree, ScriptInterpret interpret, ScriptSegment segment, ScriptContext context) throws Exception {

        //执行if块
        if(ExpressionUtil.getBooleanValue(interpret.interpretParseTreeValue(parseTree.ifDirective().parExpression(), segment, context))){
           if(interpret.containsReturn(parseTree.ifDirective().statement())){
        	  return interpret.interpretParseTree(parseTree.ifDirective().statement(), segment, context);
           }else{
        	  interpret.interpretParseTree(parseTree.ifDirective().statement(), segment, context);
        	  return ScriptResult.VOID_RESULT;
           }
        }
      
        //执行elseif块
        if(parseTree.elseifDirective()!=null){
    	   for(ElseifDirectiveContext elseifDirectiveContext:parseTree.elseifDirective()){
    		   if(ExpressionUtil.getBooleanValue(interpret.interpretParseTreeValue(elseifDirectiveContext.parExpression(), segment, context))){
    			   if(interpret.containsReturn(elseifDirectiveContext.statement())){
    				   return interpret.interpretParseTree(elseifDirectiveContext.statement(), segment, context);
    			   }else{
    				   interpret.interpretParseTree(elseifDirectiveContext.statement(), segment, context);
    				   return ScriptResult.VOID_RESULT;
    			   }
    			  
    		   }
    	   }
        }
        
        //执行else块
        if(parseTree.elseDirective()!=null){
           if(interpret.containsReturn(parseTree.elseDirective().statement())){
        	   return interpret.interpretParseTree(parseTree.elseDirective().statement(), segment, context);
           }else{
        	   interpret.interpretParseTree(parseTree.elseDirective().statement(), segment, context);
        	   return ScriptResult.VOID_RESULT;
           }
           
        }
        
        return ScriptResult.VOID_RESULT;
    }
}
