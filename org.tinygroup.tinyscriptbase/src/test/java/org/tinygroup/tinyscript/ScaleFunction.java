package org.tinygroup.tinyscript;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;

public class ScaleFunction extends AbstractScriptFunction{

	public String getNames() {
		return "scale";
	}
	
	public String getBindingTypes() {
		return "java.lang.Integer";
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		Integer object = (Integer) parameters[0];
		Integer scale = (Integer) parameters[1];
		return object.intValue()*scale.intValue();
	}

}
