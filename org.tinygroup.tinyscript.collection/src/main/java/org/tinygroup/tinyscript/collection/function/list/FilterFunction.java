package org.tinygroup.tinyscript.collection.function.list;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;

/**
 * 序列过滤函数
 * @author yancheng11334
 *
 */
public class FilterFunction extends AbstractScriptFunction {

	
	public String getNames() {
		return "filter";
	}

	public String getBindingTypes() {
		return "java.util.List";
	}
	
	@SuppressWarnings("rawtypes")
	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
        try{
        	if (parameters == null || parameters.length == 0) {
        		throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if(checkParameters(parameters, 2)){
				 List list = (List) getValue(parameters[0]);
				 LambdaFunction function = (LambdaFunction) parameters[1];
				 return filter(context,list,function);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
				
        }catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List filter(ScriptContext context,List list,LambdaFunction function) throws Exception{
		ScriptContext subContext = new DefaultScriptContext();
		subContext.setParent(context);
		
		List result = new ArrayList();
	   	for(Object obj:list){
	   		ScriptContextUtil.setCurData(subContext, obj);
	   		if((Boolean)function.execute(subContext, obj).getResult()){
	   			result.add(obj);
	   		}
	   	}
	   	return result;
	}

}
