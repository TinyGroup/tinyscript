package org.tinygroup.tinyscript.collection.function.list;

import java.util.Iterator;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.exception.NotMatchException;

/**
 * 过滤自身数据
 * @author yancheng11334
 *
 */
public class RemoveFunction extends AbstractScriptFunction {

	public String getNames() {
		return "remove";
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
				 return remove(context,list,function);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
				
        }catch (ScriptException e) {
			throw e;
		} catch (ClassCastException e) {
			throw new NotMatchException(e);
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private List remove(ScriptContext context,List list,LambdaFunction function) throws Exception{
		ScriptContext subContext = new DefaultScriptContext();
		subContext.setParent(context);
		
		Iterator it = list.iterator();
		while(it.hasNext()){
			Object obj = it.next();
			if(!(Boolean)function.execute(subContext, obj).getResult()){
			   it.remove();
			}
		}
		return list;
	}

}
