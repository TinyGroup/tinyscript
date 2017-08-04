package org.tinygroup.tinyscript.executor;

import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptException;

public interface TinyScriptOperator {

	/**
	 * 具体执行入口
	 * @param args
	 * @throws ScriptException
	 */
	void execute(String[] args) throws ScriptException;
	
	/**
	 * 获取引擎实例
	 * @return
	 * @throws ScriptException
	 */
	ScriptEngine createScriptEngine() throws ScriptException;
}
