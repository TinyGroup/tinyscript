package org.tinygroup.tinyscript.collection.function.map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.collection.function.AbstractXorFunction;

@SuppressWarnings("rawtypes")
public class MapXorFunction extends AbstractXorFunction<Map>{

	public String getBindingTypes() {
		return "java.util.Map";
	}

	@SuppressWarnings("unchecked")
	protected Map xor(Map t1, Map t2) throws ScriptException {
		Map map = new HashMap(t1);
		map.putAll(t2);
		Set keys = map.keySet();
		Set all = new HashSet();
		for(Object obj:keys){
		   if(t1.containsKey(obj) && t2.containsKey(obj)){
		     all.add(obj);
		   }
		}
		for(Object obj:all){
			map.remove(obj);
		}
		return map;
	}

}
