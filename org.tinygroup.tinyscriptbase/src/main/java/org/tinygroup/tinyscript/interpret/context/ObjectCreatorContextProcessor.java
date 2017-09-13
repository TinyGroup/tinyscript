package org.tinygroup.tinyscript.interpret.context;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ClassInstanceUtil;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.ExpressionContext;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.ObjectCreatorContext;

/**
 * 一般对象创建
 * @author yancheng11334
 *
 */
public class ObjectCreatorContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.ObjectCreatorContext>{

	public Class<ObjectCreatorContext> getType() {
		return ObjectCreatorContext.class;
	}

	public ScriptResult process(ObjectCreatorContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		String className = null;
		try{
			className = parseTree.qualifiedName().getText();
			List<Object> paramList = new ArrayList<Object>();
			if (parseTree.expressionList() != null) {
				for(ExpressionContext expressionContext:parseTree
						.expressionList().expression()){
					if(expressionContext!=null){
						paramList.add(interpret.interpretParseTreeValue(expressionContext, segment, context));
					}
				}
			}
			
			Object obj = ClassInstanceUtil.newInstance(segment, context, className, paramList);
			return new ScriptResult(obj);
		}catch (Exception e) {
			throw new ScriptException(null,e); //忽略的异常层次
		}
		
	}

}
