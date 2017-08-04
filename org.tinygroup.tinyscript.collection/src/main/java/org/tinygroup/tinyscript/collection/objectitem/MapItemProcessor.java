package org.tinygroup.tinyscript.collection.objectitem;

import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.objectitem.ObjectSingleItemProcessor;

public class MapItemProcessor extends ObjectSingleItemProcessor{

	public boolean isMatch(Object obj, Object item) {
		return obj instanceof Map;
	}

	protected Object process(ScriptContext context, Object obj, Object item)
			throws Exception {
		Map map = (Map) obj;
		return map.get(item);
	}

	protected void assignValue(ScriptContext context, Object value, Object obj,
			Object item) throws Exception {
		Map map = (Map) obj;
		map.put(item, value);
	}

}
