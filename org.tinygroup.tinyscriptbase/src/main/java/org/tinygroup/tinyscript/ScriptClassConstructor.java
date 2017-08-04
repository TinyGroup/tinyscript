package org.tinygroup.tinyscript;

/**
 * 脚本类的构造器
 * @author yancheng11334
 *
 */
public interface ScriptClassConstructor {
	
    String[] getParamterNames();
    
    boolean  isMatch(Object... parameters);
	
    ScriptClassInstance newInstance(ScriptContext context, Object... parameters) throws ScriptException;
}
