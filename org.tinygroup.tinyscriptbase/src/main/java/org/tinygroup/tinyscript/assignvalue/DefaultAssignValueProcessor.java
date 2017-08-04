package org.tinygroup.tinyscript.assignvalue;

import org.tinygroup.tinyscript.ScriptContext;

/**
 * 默认赋值操作处理器
 * @author yancheng11334
 *
 */
public class DefaultAssignValueProcessor implements AssignValueProcessor{

	public boolean isMatch(String name,
			ScriptContext context) {
		return true;
	}

	public void process(String name, Object value, 
			ScriptContext context) throws Exception {
         context.put(name, value);		
	}

	
}
