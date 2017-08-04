package org.tinygroup.tinyscript.interpret.directive;

import org.tinygroup.tinyscript.interpret.InstanceOfProcessor;

public class JavaInstanceOfProcessor implements InstanceOfProcessor{

	public boolean isMatch(Object object, Object type) {
		return type instanceof Class;
	}

	public boolean isInstance(Object object, Object type) throws Exception {
		Class classType = (Class) type;
		return classType.isInstance(object);
	}

}
