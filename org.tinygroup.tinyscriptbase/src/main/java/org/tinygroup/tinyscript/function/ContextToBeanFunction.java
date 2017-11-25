package org.tinygroup.tinyscript.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 通过上下文构造值对象Bean
 * @author yancheng11334
 *
 */
public class ContextToBeanFunction extends AbstractToBeanFunction{

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames())); 
			}else if(this.checkParameters(parameters, 1)){
				if(parameters[0] instanceof String){
				   return toBean(getParentItemMap(context),(String)parameters[0]);
				}else if(parameters[0] instanceof Class){
				   return toBean(getParentItemMap(context),(Class<?>)parameters[0]);
				}
			}
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
		}catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()),e); 
		}
	}
	
	private Map<String,Object> getParentItemMap(ScriptContext context){
		Map<String,Object> map= new HashMap<String,Object>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		ScriptContext temp = context;
		while(temp.getParent()!=null){
			list.add(0, temp.getItemMap());
			temp = (ScriptContext) temp.getParent();
		}
		for(Map<String,Object> newMap:list){
			map.putAll(newMap); //保证子环境覆盖父环境
		}
		return map;
	}

}
