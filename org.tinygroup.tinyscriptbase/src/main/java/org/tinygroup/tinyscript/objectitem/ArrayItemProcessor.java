package org.tinygroup.tinyscript.objectitem;

import java.lang.reflect.Array;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

public class ArrayItemProcessor implements ObjectItemProcessor{
    

	public boolean isMatch(Object obj, Object... items) {
		return obj.getClass().isArray();
	}

	public Object process(ScriptContext context, Object obj, Object... items)
			throws Exception {
		return get(obj,0,items);
	}
	
	public void assignValue(ScriptContext context, Object value, Object obj,
			Object... items) throws Exception {
		set(obj,value,0,items);
	}
	
	private Object get(Object array,int dim,Object...items) throws Exception{
		int n = ExpressionUtil.convertInteger(items[dim]);
		if(dim==items.length-1){
		   return Array.get(array, n);
		}else{
		   Object newArray = Array.get(array, n);
		   return get(newArray,dim+1,items);
		}
	}
	
	private void set(Object array,Object value,int dim,Object...items) throws Exception{
		int n = ExpressionUtil.convertInteger(items[dim]);
		if(dim==items.length-1){
		   Array.set(array, n, value);
		}else{
		   Object newArray = Array.get(array, n);
		   set(newArray,value,dim+1,items);
		}
	}

}
