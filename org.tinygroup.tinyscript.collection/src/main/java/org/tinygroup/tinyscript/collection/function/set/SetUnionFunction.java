package org.tinygroup.tinyscript.collection.function.set;

import java.util.HashSet;
import java.util.Set;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.collection.function.AbstractUnionFunction;

/**
 * Set的并集实现
 * @author yancheng11334
 *
 */
@SuppressWarnings("rawtypes")
public class SetUnionFunction extends AbstractUnionFunction<Set>{

	public String getBindingTypes() {
		return "java.util.Set";
	}
	
	@SuppressWarnings("unchecked")
	protected Set unite(Set t1, Set t2) throws ScriptException {
		Set set = new HashSet(t1);
		set.addAll(t2);
		return set;
	}

}
