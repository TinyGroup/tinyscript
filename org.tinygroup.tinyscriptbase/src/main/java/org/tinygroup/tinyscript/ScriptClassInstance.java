package org.tinygroup.tinyscript;

/**
 * tiny脚本的类实例
 * @author yancheng11334
 *
 */
public interface ScriptClassInstance {

	ScriptClass getScriptClass();
	
	Object  getField(String fieldName);
	
	boolean existField(String fieldName);
	
	void setField(String fieldName,Object value);
	
	Object execute(ScriptContext context,String methodName,Object... parameters) throws ScriptException;

	void setScriptContext(ScriptContext context);
}
