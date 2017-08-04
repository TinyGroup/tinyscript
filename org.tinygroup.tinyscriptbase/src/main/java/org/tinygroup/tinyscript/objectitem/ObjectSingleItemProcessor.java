package org.tinygroup.tinyscript.objectitem;

import org.tinygroup.tinyscript.ScriptContext;

/**
 * 单项下标处理器(简化处理逻辑)
 * @author yancheng11334
 *
 */
public abstract class ObjectSingleItemProcessor implements ObjectItemProcessor{

	public boolean isMatch(Object obj, Object... items) {
		return checkItems(items) && isMatch(obj,items[0]);
	}


	public Object process(ScriptContext context, Object obj, Object... items)
			throws Exception {
		return process(context,obj,items[0]);
	}

	public void assignValue(ScriptContext context, Object value, Object obj,
			Object... items) throws Exception {
		assignValue(context,value,obj,items[0]);
	}
	
	private boolean checkItems(Object... items){
		return items!=null && items.length==1;
	}
	
	protected abstract boolean isMatch(Object obj,Object item);
	
	protected abstract Object process(ScriptContext context, Object obj, Object item)
			throws Exception ;
	
	protected abstract void assignValue(ScriptContext context, Object value, Object obj,
			Object item) throws Exception ;

}
