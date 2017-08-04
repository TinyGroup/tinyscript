package org.tinygroup.tinyscript.collection.function.set;

import java.util.HashSet;
import java.util.Set;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.collection.function.AbstractSubtractFunction;

/**
 * Set的差集实现
 * @author yancheng11334
 *
 */
@SuppressWarnings("rawtypes")
public class SetSubtractFunction extends  AbstractSubtractFunction<Set> {

	public String getBindingTypes() {
		return "java.util.Set";
	}
	
	@SuppressWarnings("unchecked")
	protected Set subtract(Set t1, Set t2) throws ScriptException {
		Set set = new HashSet(t1);
		set.removeAll(t2);
		return set;
	}

}
