package org.tinygroup.tinyscript.collection.function.map;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.collection.function.AbstractUnionFunction;

/**
 * Map的并集运算
 * @author yancheng11334
 *
 */
@SuppressWarnings("rawtypes")
public class MapUnionFunction extends AbstractUnionFunction<Map>{

	public String getBindingTypes() {
		return "java.util.Map";
	}
	
	@SuppressWarnings("unchecked")
	protected Map unite(Map t1, Map t2) throws ScriptException {
		Map map = new HashMap(t1);
		map.putAll(t2);
		return map;
	}

}
