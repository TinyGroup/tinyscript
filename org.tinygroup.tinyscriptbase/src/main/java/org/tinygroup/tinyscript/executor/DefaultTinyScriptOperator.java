package org.tinygroup.tinyscript.executor;

import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptEngineFactory;
import org.tinygroup.tinyscript.ScriptException;

public class DefaultTinyScriptOperator extends AbstractTinyScriptOperator{

	public ScriptEngine createScriptEngine() throws ScriptException {
		return ScriptEngineFactory.createByBean();
	}

}
