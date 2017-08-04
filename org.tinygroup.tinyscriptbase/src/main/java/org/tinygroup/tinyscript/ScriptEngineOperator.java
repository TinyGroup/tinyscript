package org.tinygroup.tinyscript;

/**
 * 管理tiny脚本引擎
 * @author yancheng11334
 *
 */
public interface ScriptEngineOperator {

	/**
     * 设置脚本引擎
     * @param engine
     */
    void setScriptEngine(ScriptEngine engine);
    
    /**
     * 读取脚本引擎
     * @return
     */
    ScriptEngine getScriptEngine();
}
