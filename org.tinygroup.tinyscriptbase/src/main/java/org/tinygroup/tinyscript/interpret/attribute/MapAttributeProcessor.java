package org.tinygroup.tinyscript.interpret.attribute;

import java.util.Map;

import org.tinygroup.tinyscript.ScriptCollectionModel;
import org.tinygroup.tinyscript.collection.CollectionModelUtil;
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

	@SuppressWarnings("rawtypes")
	public Object getAttribute(Object object, Object name) throws Exception {
		Map map = (Map) object;
		if(map.containsKey(name)){
		   //优先走map键值逻辑
		   return map.get(name);
		}else{
		   //调用子元素的调用逻辑
		   ScriptCollectionModel model = CollectionModelUtil.getScriptCollectionModel(object);
		   return model.getAttribute(object, name);
		}
	}

}
