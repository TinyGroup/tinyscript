package org.tinygroup.tinyscript.interpret.context;

import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.impl.ArrayScriptContext;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.ArrayInitializerContext;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.VariableInitializerContext;

public class ArrayInitializerContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.ArrayInitializerContext>{

	public Class<ArrayInitializerContext> getType() {
		return ArrayInitializerContext.class;
	}

	public ScriptResult process(ArrayInitializerContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		List<VariableInitializerContext>  variableInitializerContextList = parseTree.variableInitializer();
		
		ArrayScriptContext newContext = new ArrayScriptContext(context,variableInitializerContextList.size());
		
		for(int i=0;i<variableInitializerContextList.size();i++){
			VariableInitializerContext variableInitializerContext = variableInitializerContextList.get(i);
			if(variableInitializerContext.expression()!=null){
				int n = newContext.getItemMap().size();
				Object object = interpret.interpretParseTreeValue(variableInitializerContext.expression(), segment, context);
				newContext.put(String.valueOf(n), object);
			}else if(variableInitializerContext.arrayInitializer()!=null){
				int n = newContext.getSubContextMap().size();
				ArrayScriptContext subContext = (ArrayScriptContext) interpret.interpretParseTreeValue(variableInitializerContext.arrayInitializer(), segment, context);
				newContext.putSubContext(String.valueOf(n), subContext);
				subContext.setOrder(n);
			}
		}
		return new ScriptResult(newContext);
	}

}
