package org.tinygroup.tinyscript.collection.function.list;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.ExpressionFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;

/**
 * 序列过滤函数
 * @author yancheng11334
 *
 */
public class FilterFunction extends ExpressionFunction {

	
	public String getNames() {
		return "filter";
	}

	public String getBindingTypes() {
		return "java.util.List";
	}
	
	public boolean  enableExpressionParameter(){
		return true;
	}

	@SuppressWarnings("rawtypes")
	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
        try{
        	if (parameters == null || parameters.length == 0) {
        		throw new ScriptException(String.format("%s函数的参数为空!", getNames()));
			} else if(checkParameters(parameters, 2)){
				 List list = (List) getValue(parameters[0]);
				 String  expression = getExpression(parameters[1]);
				 return filter(context,list,expression);
			} else {
				throw new ScriptException(String.format("%s函数的参数格式不正确!", getNames()));
			}
				
        }catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(String.format("%s函数执行发生异常:", getNames()),e);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List filter(ScriptContext context,List list,String expression) throws Exception{
		ScriptContext subContext = new DefaultScriptContext();
		subContext.setParent(context);
		String newExpression = checkExpression(expression);

		List result = new ArrayList();
	   	for(Object obj:list){
	   		subContext.put(ELEMENT_NAME, obj);
	   		if(executeDynamicBoolean(newExpression,subContext)){
	   			result.add(obj);
	   		}
	   	}
	   	return result;
	}

}
