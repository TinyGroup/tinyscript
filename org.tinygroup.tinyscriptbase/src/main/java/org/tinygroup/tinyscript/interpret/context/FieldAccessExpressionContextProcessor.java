package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.AttributeUtil;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.FieldAccessExpressionContext;

public class FieldAccessExpressionContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.FieldAccessExpressionContext>{

	public Class<FieldAccessExpressionContext> getType() {
		return TinyScriptParser.FieldAccessExpressionContext.class;
	}

	public ScriptResult process(FieldAccessExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		Object object = null;
		String fieldName = null;
		try{
		    object = interpret.interpretParseTreeValue(parseTree.expression(), segment, context);
		    fieldName = parseTree.Identifier().getText();
		    Object fieldValue = AttributeUtil.getAttribute(object, fieldName);
			return new ScriptResult(fieldValue);
		}catch(Exception e){
			throw new ScriptException(String.format("[%s]类型的ParserRuleContext处理发生异常:对象[%s],字段[%s]", getType(),object,fieldName),e);
		}
	}

}
