package org.tinygroup.tinyscript.collection.objectitem;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;
import org.tinygroup.tinyscript.objectitem.ObjectSingleItemProcessor;

public class ListToListProcessor extends ObjectSingleItemProcessor {

	protected boolean isMatch(Object obj, Object item) {
		return obj instanceof List && item instanceof List;
	}

	protected Object process(ScriptContext context, Object obj, Object item) throws Exception {
		List list = (List) obj;
		List list2 = (List) item;
		List result = new ArrayList(); // 生成新的数组
		ScriptEngine engine = ScriptContextUtil.getScriptEngine(context);
		for (int i = 0; i < list2.size(); i++) {
			int n = ExpressionUtil.convertInteger(list2.get(i));
			if (engine.isIndexFromOne()) {
				result.add(list.get(n - 1));
			} else {
				result.add(list.get(n));
			}

		}
		return result;
	}

	protected void assignValue(ScriptContext context, Object value, Object obj, Object item) throws Exception {
		throw new ScriptException(ResourceBundleUtil.getResourceMessage("collection", "collection.assignment.error"));
	}

}
