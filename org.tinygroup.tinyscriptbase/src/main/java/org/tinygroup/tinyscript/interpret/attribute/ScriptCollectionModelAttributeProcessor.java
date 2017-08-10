package org.tinygroup.tinyscript.interpret.attribute;

import org.tinygroup.tinyscript.ScriptCollectionModel;
import org.tinygroup.tinyscript.collection.CollectionModelUtil;
import org.tinygroup.tinyscript.interpret.AttributeProcessor;

public class ScriptCollectionModelAttributeProcessor implements AttributeProcessor{

	public boolean isMatch(Object object, Object name) {
		return CollectionModelUtil.getScriptCollectionModel(object)!=null;
	}

	public Object getAttribute(Object object, Object name) throws Exception {
		ScriptCollectionModel model = CollectionModelUtil.getScriptCollectionModel(object);
		return model.getAttribute(object, name);
	}

}
