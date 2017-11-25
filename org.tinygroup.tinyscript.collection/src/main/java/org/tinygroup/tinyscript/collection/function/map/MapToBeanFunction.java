package org.tinygroup.tinyscript.collection.function.map;

import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractToBeanFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

public class MapToBeanFunction  extends AbstractToBeanFunction {
	
	public String getBindingTypes() {
		return "java.util.Map";
	}

	@SuppressWarnings("unchecked")
	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames())); 
			}else if(this.checkParameters(parameters, 2)){
				Map<String,Object> map = (Map<String,Object>)parameters[0];
				if(parameters[1] instanceof String){
				   return toBean(map,(String)parameters[1]);
				}else if(parameters[1] instanceof Class){
				   return toBean(map,(Class<?>)parameters[1]);
				}
			}
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
		}catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()),e); 
		}
	}

}
