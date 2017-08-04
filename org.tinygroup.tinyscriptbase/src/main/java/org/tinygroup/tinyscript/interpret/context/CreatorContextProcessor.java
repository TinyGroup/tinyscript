package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.CreatorContext;

/**
 * 创建new对象
 * 
 * @author yancheng11334
 * 
 */
public class CreatorContextProcessor implements
		ParserRuleContextProcessor<TinyScriptParser.CreatorContext> {

	public Class<CreatorContext> getType() {
		return TinyScriptParser.CreatorContext.class;
	}

	public ScriptResult process(CreatorContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		//获得类名
		String className = null;
		try {
			className = parseTree.getChild(1).getText();
			
			if(parseTree.objectCreator()!=null){
			   //执行new对象逻辑
			   return interpret.interpretParseTree(parseTree.objectCreator(), segment, context);
			}else if(parseTree.arrayCreator()!=null){
			   //执行new数组
				return interpret.interpretParseTree(parseTree.arrayCreator(), segment, context);
			}else{
				throw new ScriptException("new指令匹配的处理数据结构失败!");
			}

		} catch (Exception e) {
			throw new ScriptException(String.format("[%s]类型的ParserRuleContext处理发生异常:创建对象[%s]", getType(),className),e);
		}
	}
	
}
