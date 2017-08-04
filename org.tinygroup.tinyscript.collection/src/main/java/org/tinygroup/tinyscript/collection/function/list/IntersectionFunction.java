package org.tinygroup.tinyscript.collection.function.list;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.collection.function.AbstractIntersectionFunction;

/**
 * List交集函数
 * @author yancheng11334
 *
 */
@SuppressWarnings("rawtypes")
public class IntersectionFunction extends AbstractIntersectionFunction<List> {
	
	public String getBindingTypes() {
		return "java.util.List";
	}

	@SuppressWarnings("unchecked")
	protected List intersect(List t1, List t2) throws ScriptException {
		List list = new ArrayList();
		for(Object element:t1){
		    if(t2.contains(element)){
		       list.add(element);
		    }
		}
		return list;
	}

}
