package org.tinygroup.tinyscript.objectitem;

import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;

public class ListItemProcessor extends  ObjectSingleItemProcessor{

	protected boolean isMatch(Object obj, Object item) {
		return obj instanceof List;
	}

	protected Object process(ScriptContext context, Object obj, Object item)
			throws Exception {
		List list = (List) obj;
		int n = ExpressionUtil.convertInteger(item);
		ScriptEngine engine  = ScriptContextUtil.getScriptEngine(context);
		if(engine.isIndexFromOne()){
		   return list.get(n-1);
		}else{
		   return list.get(n);
		}
	}

	protected void assignValue(ScriptContext context, Object value, Object obj,
			Object item) throws Exception {
		List list = (List) obj;
		int n = ExpressionUtil.convertInteger(item);
		ScriptEngine engine  = ScriptContextUtil.getScriptEngine(context);
		if(engine.isIndexFromOne()){
			list.set(n-1, value);
		}else{
			list.set(n, value);
		}
		
	}


}
