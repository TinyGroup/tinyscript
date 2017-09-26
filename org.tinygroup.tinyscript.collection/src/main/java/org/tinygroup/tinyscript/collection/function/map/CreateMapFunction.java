package org.tinygroup.tinyscript.collection.function.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

public class CreateMapFunction extends AbstractScriptFunction {

	@Override
	public String getNames() {
		return "map";
	}

	@Override
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 2)) {
				List<?> key = (List<?>) parameters[0];
				List<?> value = (List<?>) parameters[1];
				int size = key.size() <= value.size() ? key.size() : value.size();
				Map<Object, Object> result = new HashMap<Object, Object>();
				for (int i = 0; i < size; i++) {
					result.put(key.get(i), value.get(i));
				}
				return result;
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

}
