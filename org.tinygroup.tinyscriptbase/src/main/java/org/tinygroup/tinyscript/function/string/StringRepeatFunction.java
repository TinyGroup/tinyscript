package org.tinygroup.tinyscript.function.string;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

public class StringRepeatFunction extends AbstractScriptFunction {

	@Override
	public String getNames() {
		return "repeat";
	}

	@Override
	public String getBindingTypes() {
		return String.class.getName();
	}

	@Override
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (this.checkParameters(parameters, 2)) {
				String str = (String) parameters[0];
				int count = (Integer) parameters[1];
				StringBuilder builder = new StringBuilder();
				for (int i = 1; i <= count; i++) {
					builder.append(str);
				}
				return builder.toString();
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

}
