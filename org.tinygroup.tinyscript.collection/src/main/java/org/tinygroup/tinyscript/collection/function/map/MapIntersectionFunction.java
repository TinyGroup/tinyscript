package org.tinygroup.tinyscript.collection.function.map;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.collection.function.AbstractIntersectionFunction;

/**
 * Map的交集运算
 * @author yancheng11334
 *
 */
@SuppressWarnings("rawtypes")
public class MapIntersectionFunction extends AbstractIntersectionFunction<Map> {

	public String getBindingTypes() {
		return "java.util.Map";
	}
	
	@SuppressWarnings("unchecked")
	protected Map intersect(Map t1, Map t2) throws ScriptException {
		Map map = new HashMap();
		for(Object key:t1.keySet()){
		    if(t2.containsKey(key)){
		       map.put(key, t1.get(key));
		    }
		}
		return map;
	}

}
