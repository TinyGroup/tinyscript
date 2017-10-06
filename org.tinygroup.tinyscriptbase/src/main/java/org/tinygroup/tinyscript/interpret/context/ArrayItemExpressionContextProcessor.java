package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.objectitem.ObjectItemUtil;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.ArrayItemExpressionContext;

public class ArrayItemExpressionContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.ArrayItemExpressionContext>{

	public Class<ArrayItemExpressionContext> getType() {
		return ArrayItemExpressionContext.class;
	}

	public ScriptResult process(ArrayItemExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		String objName = null;
		String itemName = null;
		try{
			objName = parseTree.getChild(0).getText();
		    itemName = parseTree.getChild(2).getText();
			Object obj = interpret.interpretParseTreeValue(parseTree.getChild(0), segment, context);
			Object item = interpret.interpretParseTreeValue(parseTree.getChild(2), segment, context);
			Object value = null;
			if(obj instanceof String){
				value = ObjectItemUtil.operate(context, ((String) obj).toCharArray(), item);
			}else {
				value = ObjectItemUtil.operate(context, obj, item);
			}
			return new ScriptResult(value);
		}catch(Exception e){
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("context.arrayitem.error",  getType(),objName,itemName),e);
		}
	}

}
