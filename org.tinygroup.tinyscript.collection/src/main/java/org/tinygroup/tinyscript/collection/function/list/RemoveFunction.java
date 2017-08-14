package org.tinygroup.tinyscript.collection.function.list;

import java.util.Iterator;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.ExpressionFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;

/**
 * 过滤自身数据
 * @author yancheng11334
 *
 */
public class RemoveFunction extends ExpressionFunction {

	public String getNames() {
		return "remove";
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
				 return remove(context,list,expression);
			} else {
				throw new ScriptException(String.format("%s函数的参数格式不正确!", getNames()));
			}
				
        }catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(String.format("%s函数执行发生异常:", getNames()),e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private List remove(ScriptContext context,List list,String expression) throws Exception{
		ScriptContext subContext = new DefaultScriptContext();
		subContext.setParent(context);
		String newExpression = checkExpression(expression);
		Iterator it = list.iterator();
		while(it.hasNext()){
			Object obj = it.next();
			subContext.put(ELEMENT_NAME, obj);
			if(!executeDynamicBoolean(newExpression,subContext)){
			   it.remove();
			}
		}
		return list;
	}

}
