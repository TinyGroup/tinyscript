package org.tinygroup.tinyscript.function.date;

import java.util.Date;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.exception.NotMatchException;

public class DateToStringFunction extends AbstractScriptFunction {

	@Override
	public String getNames() {
		return "string";
	}

	public String getBindingTypes() {
		return "java.util.Date";
	}

	@Override
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 1)) {
				return DateUtil.dateToString((Date) parameters[0], null);
			} else if (checkParameters(parameters, 2)) {
				return DateUtil.dateToString((Date) parameters[0], (String) parameters[1]);
			} else {
				throw new NotMatchException(
						ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()), e);
		}
	}

}
