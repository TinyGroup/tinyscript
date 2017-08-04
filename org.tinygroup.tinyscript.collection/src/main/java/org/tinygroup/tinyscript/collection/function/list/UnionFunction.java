package org.tinygroup.tinyscript.collection.function.list;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.collection.function.AbstractUnionFunction;

/**
 * List并集实现
 * @author yancheng11334
 *
 */
@SuppressWarnings("rawtypes")
public class UnionFunction extends AbstractUnionFunction<List> {

	public String getBindingTypes() {
		return "java.util.List";
	}

	@SuppressWarnings("unchecked")
	protected List unite(List t1, List t2) throws ScriptException {
      List list = new ArrayList(t1);
      for (Object element : t2) {
          if (!list.contains(element)) {
              list.add(element);
          }
      }
      return list;
	}

}
