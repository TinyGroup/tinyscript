package org.tinygroup.tinyscript.function.random;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 随机数函数抽象类
 * @author yancheng11334
 *
 */
public abstract class AbstractRandFunction<T> extends AbstractScriptFunction{
	
	public boolean  enableExpressionParameter(){
		return true;
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
			if(parameters==null || parameters.length==0){
			   return rand(null);
			}else if(this.checkParameters(parameters, 1)){
			   return rand(getValue(parameters[0]));
			}else{
			   throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		}catch(ScriptException e){
			throw e;
		}catch(Exception e){
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()),e);
		}
	}
	
	/**
	 * 生成不同范围的随机数
	 * @param limit
	 * @return
	 */
	protected abstract T rand(Object limit) throws ScriptException;

}
