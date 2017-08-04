package org.tinygroup.tinyscript;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;

public class TimeFunction extends AbstractScriptFunction{

	public String getNames() {
		return "time";
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		return 1000L;
	}

	
}
