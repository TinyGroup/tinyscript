package org.tinygroup.tinyscript.collection.function.list;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;

public class CopyFunction extends AbstractScriptFunction {

	public String getNames() {
		return "copy";
	}
	
	public String getBindingTypes() {
		return "java.util.List";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException("copy函数的参数为空!");
			} else if (checkParameters(parameters, 1)) {
                List list = (List) parameters[0];
                List newList = new ArrayList();
                newList.addAll(list);
                return newList;
			} else {
				throw new ScriptException("copy函数的参数格式不正确!");
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException("copy函数的参数格式不正确!", e);
		}

	}

}
