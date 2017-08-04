package org.tinygroup.tinyscript;

public interface ScriptClassMethod {

	String getMethodName();
	
	String[] getParamterNames();
	
	Object execute(ScriptContext context, Object... parameters) throws ScriptException;
}
