package org.tinygroup.tinyscript.function.date;

import java.text.ParseException;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.exception.NotMatchException;

public class IsDateFunction extends AbstractScriptFunction {

	@Override
	public String getNames() {
		return "isDate";
	}

	@Override
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 1)) {
				DateUtil.convertDateByString((String) parameters[0], null);
				return true;
			} else if (checkParameters(parameters, 2)) {
				DateUtil.convertDateByString((String) parameters[0], (String) parameters[1]);
				return true;
			} else {
				throw new NotMatchException(
						ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (ParseException e) {
			return false;
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

}
