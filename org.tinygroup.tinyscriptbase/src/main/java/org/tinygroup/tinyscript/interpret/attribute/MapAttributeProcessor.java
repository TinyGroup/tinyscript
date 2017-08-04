package org.tinygroup.tinyscript.interpret.attribute;

import java.util.Map;

import org.tinygroup.tinyscript.interpret.AttributeProcessor;

/**
 * Map处理器
 * @author yancheng11334
 *
 */
public class MapAttributeProcessor implements AttributeProcessor{

	public boolean isMatch(Object object, Object name) {
		return object instanceof Map;
	}

	public Object getAttribute(Object object, Object name) throws Exception {
		Map map = (Map) object;
		return map.get(name);
	}

}
