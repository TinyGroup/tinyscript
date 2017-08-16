package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.ScriptUtil;
import org.tinygroup.tinyscript.interpret.custom.CustomUtil;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.CustomScriptExpressionContext;

public class CustomScriptExpressionContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.CustomScriptExpressionContext>{

	//用户自定义bean
	private static final  String CUSTOM_BEAN_NAME = "customBean"; //
	
	public Class<CustomScriptExpressionContext> getType() {
		return CustomScriptExpressionContext.class;
	}

	public ScriptResult process(CustomScriptExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		try{
			 Object customObj = null;
			 if(parseTree.Identifier()!=null){
				//用户自定义变量
				customObj = interpret.interpretParseTreeValue(parseTree.Identifier(), segment, context);
			 }else{
				//通过上下文和全局配置查询用户自定义bean
				customObj = ScriptUtil.getVariableValue(context, getBeanName(context));
			 }
			 String rule = parseTree.CUSTOM_SCRIPT().getText();
			 rule = rule.substring(2, rule.length()-2); //去掉[[ ]]
			 Object value = CustomUtil.executeRule(customObj, rule, context);
			 if(value!=null && value instanceof ScriptResult){
			    return (ScriptResult) value;
			 }
			 return new ScriptResult(value);
		}catch(Exception e){
			throw new ScriptException(String.format("[%s]类型的ParserRuleContext处理发生异常", getType()),e);
		}
	}
	
	private String getBeanName(ScriptContext context) throws ScriptException{
		//首先从上下文查询用户自定义的bean名称
		String beanName = context.get(CUSTOM_BEAN_NAME);
		if(beanName==null){
		   //再从全局配置查询用户定义的bean名称
		   beanName = ConfigurationUtil.getConfigurationManager().getConfiguration(CUSTOM_BEAN_NAME);
		}
		
		if(beanName!=null){
		   return beanName;
		}else{
		   throw new ScriptException(String.format("从上下文环境和全局配置项没有找到[%s],获取用户自定义bean名称失败", CUSTOM_BEAN_NAME));
		}
	}
	
}
