package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.BinaryRightExpressionContext;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.assignvalue.AssignValueUtil;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.exception.RunScriptException;

public class MathBinaryRightProcessor implements ParserRuleContextProcessor<TinyScriptParser.BinaryRightExpressionContext>{

	public Class<BinaryRightExpressionContext> getType() {
		return TinyScriptParser.BinaryRightExpressionContext.class;
	}

	public ScriptResult process(BinaryRightExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		String name = parseTree.getChild(0).getText();
		String op = parseTree.getChild(1).getText();
		try{
			if("=".equals(op)){
				Object value= interpret.interpretParseTreeValue(parseTree.getChild(2), segment, context);
				AssignValueUtil.operate(name, value, context); //执行赋值操作
			    return new ScriptResult(value);
			}else{
				Object aValue = interpret.interpretParseTreeValue(parseTree.getChild(0), segment, context);
				Object bValue = interpret.interpretParseTreeValue(parseTree.getChild(2), segment, context);
				String newOp = op.substring(0, op.length()-1);
				Object newValue = ExpressionUtil.executeOperation(newOp, aValue,bValue);
				AssignValueUtil.operate(name, newValue, context); //执行赋值操作
				return new ScriptResult(newValue);
			}
		}catch(Exception e){
			throw new RunScriptException(e,parseTree,segment,ScriptException.ERROR_TYPE_RUNNING,String.format("%s进行赋值%s操作发生异常", name,op));
		}
		
		
	}


}
