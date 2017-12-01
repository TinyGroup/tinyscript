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

public class ListGroupStagedFunction extends AbstractScriptFunction {

	@Override
	public String getNames() {
		return "groupStaged";
	}

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
			} else if (parameters.length > 1) {
				List<?> dataArray = (List<?>) getValue(parameters[0]);
				String[] expressions = new String[parameters.length - 1];
				for (int i = 0; i < expressions.length; i++) {
					expressions[i] = ScriptContextUtil.convertExpression(getExpression(parameters[i + 1]));
				}
				return groupStaged(dataArray, expressions, context);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	private Object groupStaged(List<?> dataArray, String[] expressions, ScriptContext context) throws ScriptException {
		Map<String, List<Object>> result = new LinkedHashMap<String, List<Object>>();
		for (int i = 0; i < dataArray.size(); i++) {
			String key = null;
			ScriptContext subContext = new DefaultScriptContext();
			subContext.setParent(context);
			subContext.put("it", dataArray.get(i));
			ScriptContextUtil.setCurData(subContext, dataArray.get(i));
			for (String expression : expressions) {
				if (executeDynamicBoolean(expression, subContext)) {
					key = expression;
					break;
				}
			}
			List<Object> list = result.get(key);
			if (list == null) {
				list = new ArrayList<Object>();
				result.put(key, list);
			}
			list.add(dataArray.get(i));
		}
		return result.values();
	}

}
