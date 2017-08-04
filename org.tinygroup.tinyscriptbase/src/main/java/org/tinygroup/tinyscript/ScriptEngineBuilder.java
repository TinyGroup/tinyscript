package org.tinygroup.tinyscript;

/**
 * 脚本引擎组装器
 * @author yancheng11334
 *
 */
public interface ScriptEngineBuilder {

	public static final String  DEFAULT_BEAN_NAME = "scriptEngineBuilder";
	/**
	 * 注册组件
	 * @param engine
	 * @throws ScriptException
	 */
	void registerComponent(ScriptEngine engine) throws ScriptException;
	
	/**
	 * 注册处理器
	 * @param engine
	 * @throws ScriptException
	 */
	void registerProcessor(ScriptEngine engine) throws ScriptException;
}
