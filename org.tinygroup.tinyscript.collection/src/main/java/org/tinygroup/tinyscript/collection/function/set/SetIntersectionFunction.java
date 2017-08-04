package org.tinygroup.tinyscript.collection.function.set;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.collection.function.AbstractIntersectionFunction;

/**
 * Set的交集实现
 * @author yancheng11334
 *
 */
@SuppressWarnings("rawtypes")
public class SetIntersectionFunction extends AbstractIntersectionFunction<Set> {

	public String getBindingTypes() {
		return "java.util.Set";
	}
	
	@SuppressWarnings("unchecked")
	protected Set intersect(Set t1, Set t2) throws ScriptException {
		Set set = new HashSet();
		Iterator it= t1.iterator();
		while(it.hasNext()){
		   Object element = it.next();
		   if(t2.contains(element)){	   
			  set.add(element);
		   }
		}
		return set;
	}

}
