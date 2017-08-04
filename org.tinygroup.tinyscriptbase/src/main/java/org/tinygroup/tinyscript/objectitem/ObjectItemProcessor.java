package org.tinygroup.tinyscript.objectitem;

import org.tinygroup.tinyscript.ScriptContext;

/**
 * 元素下标处理器
 * @author yancheng11334
 *
 */
public interface ObjectItemProcessor {

	boolean isMatch(Object obj,Object... items);
	
	Object process(ScriptContext context,Object obj,Object... items) throws Exception;
	
	void  assignValue(ScriptContext context,Object value,Object obj,Object... items) throws Exception;
}
