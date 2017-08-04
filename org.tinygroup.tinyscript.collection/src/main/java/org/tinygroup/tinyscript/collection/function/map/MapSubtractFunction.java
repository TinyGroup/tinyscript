package org.tinygroup.tinyscript.collection.function.map;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.collection.function.AbstractSubtractFunction;

/**
 * Map的差集实现
 * @author yancheng11334
 *
 */
@SuppressWarnings("rawtypes")
public class MapSubtractFunction extends  AbstractSubtractFunction<Map> {

	public String getBindingTypes() {
		return "java.util.Map";
	}
	
	@SuppressWarnings("unchecked")
	protected Map subtract(Map t1, Map t2) throws ScriptException {
		Map map = new HashMap(t1);
		for(Object key:t2.keySet()){
		    if(map.containsKey(key)){
		       map.remove(key);
		    }
		}
		return map;
	}

}
