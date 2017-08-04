package org.tinygroup.tinyscript.interpret.attribute;

import org.tinygroup.tinyscript.ScriptClassInstance;
import org.tinygroup.tinyscript.interpret.AttributeProcessor;

public class ScriptClassInstanceAttributeProcessor implements AttributeProcessor{

	public boolean isMatch(Object object, Object name) {
		return object instanceof ScriptClassInstance;
	}

	public Object getAttribute(Object object, Object name) throws Exception {
		ScriptClassInstance classInstance = (ScriptClassInstance) object;
		return classInstance.getField(name.toString());
	}

}
