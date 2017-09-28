package org.tinygroup.tinyscript.collection.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 差集抽象函数
 * @author yancheng11334
 *
 * @param <T>
 */
public abstract class AbstractSubtractFunction<T> extends AbstractScriptFunction {

	public String getNames() {
		return "subtract";
	}

	@SuppressWarnings("unchecked")
	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
			if(parameters == null || parameters.length == 0){
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			}else if(checkParameters(parameters, 2)){
				T t1 = (T) parameters[0];
				T t2 = (T) parameters[1];
				return subtract(t1,t2);
			}else{
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		}catch(ScriptException e){
			throw e;
		}catch(Exception e){
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}
	
	/**
	 * 具体差集逻辑
	 * @param t1
	 * @param t2
	 * @return
	 * @throws ScriptException
	 */
	protected abstract T subtract(T t1,T t2) throws ScriptException;

}
