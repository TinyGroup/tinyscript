package org.tinygroup.tinyscript.expression.booleanconvert;

import java.util.Map;

import org.tinygroup.tinyscript.expression.BooleanConverter;

public class MapBooleanConverter implements BooleanConverter{

	public boolean isMatch(Object obj) {
		return obj instanceof Map;
	}

	public Boolean convert(Object obj) {
		 Map map = (Map) obj;
         return map.size() > 0;
	}

}
