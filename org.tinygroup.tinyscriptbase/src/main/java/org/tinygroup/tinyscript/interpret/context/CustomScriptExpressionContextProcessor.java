package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.ScriptUtil;
import org.tinygroup.tinyscript.interpret.custom.CustomUtil;
import org.tinygroup.tinyscript.interpret.exception.RunScriptException;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.CustomScriptExpressionContext;

public class CustomScriptExpressionContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.CustomScriptExpressionContext>{
	
	public Class<CustomScriptExpressionContext> getType() {
		return CustomScriptExpressionContext.class;
	}

	public ScriptResult process(CustomScriptExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		String rule = null;
		try{
			 Object customObj = null;
			 if(parseTree.Identifier()!=null){
				//用户自定义变量
				customObj = interpret.interpretParseTreeValue(parseTree.Identifier(), segment, context);
			 }else{
				//通过上下文和全局配置查询用户自定义bean
				customObj = ScriptUtil.getVariableValue(context, ScriptContextUtil.getCustomBeanName(context));
			 }
			 rule = parseTree.CUSTOM_SCRIPT().getText();
			 rule = rule.substring(2, rule.length()-2); //去掉[[ ]]
			 Object value = CustomUtil.executeRule(customObj, rule, context);
			 if(value!=null && value instanceof ScriptResult){
			    return (ScriptResult) value;
			 }
			 return new ScriptResult(value);
		}catch(Exception e){
			throw new RunScriptException(e,parseTree,segment,ScriptException.ERROR_TYPE_RUNNING,String.format("执行%s用户自定义规则发生异常", rule));
		}
	}
	
}
