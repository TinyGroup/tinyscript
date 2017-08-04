package org.tinygroup.tinyscript;

/**
 * tiny脚本的类结构定义
 * @author yancheng11334
 *
 */
public interface ScriptClass {

	String getClassName();
	
	ScriptClassMethod getScriptMethod(String methodName);
	
	ScriptClassMethod[] getScriptMethods();
	
	ScriptClassField getScriptField(String fieldName);
	
	ScriptClassField[] getScriptFields();
	
	ScriptClassConstructor[] getScriptClassConstructors();
	
	ScriptClassInstance newInstance(ScriptContext context, Object... parameters) throws ScriptException;
	
	ScriptSegment getScriptSegment();
}
