package org.tinygroup.tinyscript.collection.function.list;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.collection.function.AbstractSubtractFunction;

/**
 * List的差集实现
 * @author yancheng11334
 *
 */
@SuppressWarnings("rawtypes")
public class SubtractFunction extends AbstractSubtractFunction<List> {
	
	public String getBindingTypes() {
		return "java.util.List";
	}

	@SuppressWarnings("unchecked")
	protected List subtract(List t1, List t2) throws ScriptException {
		List list = new ArrayList(t1);
		for (Object element : t2) {
            if (list.contains(element)) {
                list.remove(element);
            }
        }
		return list;
	}

}
