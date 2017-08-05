package org.tinygroup.tinyscript.interpret.context;

import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.exception.ReturnException;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.ExpressionContext;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.LambdaBodyContext;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.LambdaExpressionContext;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.LambdaParametersContext;

/**
 * lambda表达式处理器
 * @author yancheng11334
 *
 */
public class LambdaExpressionContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.LambdaExpressionContext>{

	public Class<LambdaExpressionContext> getType() {
		return LambdaExpressionContext.class;
	}

	public ScriptResult process(LambdaExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		try{ 
			
			LambdaFunction lambdaFunction = new InnerLambdaFunction(parseTree,interpret,segment);
			
			//有名字的lambda函数定义与执行是分离的;而匿名lambda函数定义与执行是一体的
			if(lambdaFunction.getFunctionName()==null){
				return new ScriptResult(lambdaFunction);
			}else{
				ScriptContextUtil.setLambdaFunction(context, lambdaFunction);
			}
			
			
		}catch(Exception e){
			throw new ScriptException(String.format("[%s]类型的ParserRuleContext处理发生异常:lambda表达式[%s]", getType(),parseTree.getText()),e);
		}
		return ScriptResult.VOID_RESULT;
	} 
	
	class InnerLambdaFunction implements LambdaFunction{
		private String functionName;
        private String[] parameterNames;
        private LambdaBodyContext lambdaBodyContext;
        private ScriptInterpret interpret;
        private ScriptSegment segment;
        public InnerLambdaFunction(LambdaExpressionContext lambdaExpressionContext,ScriptInterpret interpret,ScriptSegment segment){
        	this.interpret = interpret;
        	this.segment = segment;
        	LambdaParametersContext lambdaParameters=lambdaExpressionContext.lambdaParameters();
        	//函数名()最少需要3个节点,排除一个变量的情况
        	if(lambdaParameters.Identifier()!=null){
        		if(lambdaParameters.getChildCount()>=3){
        		   functionName = lambdaParameters.Identifier().getText();
        		}else if(lambdaParameters.getChildCount()==1){
        			//仅一个参数
        			parameterNames = new String[1];
        			parameterNames[0] = lambdaParameters.Identifier().getText();
        		}
        		
        	}
        	
        	//参数设置
        	if(lambdaParameters.expressionList()!=null){
        		List<ExpressionContext> expressionList = lambdaParameters.expressionList().expression();
        		parameterNames = new String[expressionList.size()];
        		for(int i=0;i<expressionList.size();i++){
        			parameterNames[i] = expressionList.get(i).getText();
        		}
        	}
        	
        	lambdaBodyContext = lambdaExpressionContext.lambdaBody();
        }
        
		public String getFunctionName() {
			return functionName;
		}

		public String[] getParamterNames() {
			return parameterNames;
		}

		public ScriptResult execute(ScriptContext context, Object... parameters)
				throws Exception {
			
			if((parameterNames==null || parameterNames.length==0) && (parameters==null || parameters.length==0)){
				return executeLambda(context);
			}else if(parameterNames!=null && parameters!=null && parameterNames.length==parameters.length){
				for(int i=0;i<parameterNames.length;i++){
					context.getItemMap().put(parameterNames[i], parameters[i]);
				}
				try{
					return executeLambda(context);
				}finally{
					for(int i=0;i<parameterNames.length;i++){
						context.getItemMap().remove(parameterNames[i]);
					}
				}
				
			}else{
				throw new ScriptException("执行lambda函数发生异常:参数列表与定义不一致");
			}
			
		}
		
		private ScriptResult executeLambda(ScriptContext context) throws Exception{
			//运行lambda表达式
			try{
				return interpret.interpretParseTree(lambdaBodyContext, segment, context);
			}catch(ReturnException e){
				return new ScriptResult(e.getValue());
			}
		}
		
	}

}