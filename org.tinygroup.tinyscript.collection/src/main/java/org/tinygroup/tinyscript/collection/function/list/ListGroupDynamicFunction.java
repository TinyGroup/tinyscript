package org.tinygroup.tinyscript.collection.function.list;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;

public class ListGroupDynamicFunction extends AbstractScriptFunction {
	
	@Override
	public String getNames() {
		return "groupDynamic";
	}
	
	@Override
	public boolean enableExpressionParameter() {
		return true;
	}
	
	@Override
	public String getBindingTypes() {
		return List.class.getName();
	}

	@Override
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 2)) {
				List<?> dataArray = (List<?>) getValue(parameters[0]);
				String expression = ScriptContextUtil.convertExpression(getExpression(parameters[1]));
				return groupDynamic(dataArray, expression, context);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	private Object groupDynamic(List<?> dataArray, String expression, ScriptContext context) throws ScriptException {
		Map<Object, List<Object>> result = new LinkedHashMap<Object, List<Object>>();
		for (int i = 0; i < dataArray.size(); i++) {
			ScriptContext subContext = new DefaultScriptContext();
			subContext.setParent(context);
			subContext.put("it", dataArray.get(i));
			ScriptContextUtil.setCurData(subContext, dataArray.get(i));
			Object key = executeDynamicObject(expression, subContext);
			List<Object> group = result.get(key);
			if (group == null) {
				group = new ArrayList<Object>();
				result.put(key, group);
			}
			group.add(dataArray.get(i));
		}
		return result.values();
	}

}
