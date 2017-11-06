package org.tinygroup.tinyscript.function.date;

import java.util.Calendar;
import java.util.Date;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.exception.NotMatchException;

public class DateTruncFunction extends AbstractScriptFunction {

	@Override
	public String getNames() {
		return "dateTrunc";
	}

	@Override
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			DateEnum datePart = null;
			Calendar cal = Calendar.getInstance();
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 2)) {
				datePart = DateEnum.valueOf((String) parameters[0]);
				Date date = ExpressionUtil.convertDate(parameters[1]);
				cal.setTime(date);
			} else {
				throw new NotMatchException(
						ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
			switch (datePart) {
			case WEEK:
				Calendar start = Calendar.getInstance();
				start.setTime(new Date());
				start.set(Calendar.MONTH, 0);
				start.set(Calendar.DATE, 1);
				if (start.get(Calendar.DAY_OF_WEEK) != 1) {
					start.set(Calendar.DATE, 9 - start.get(Calendar.DAY_OF_WEEK));
				}
				return start.getTime();
			case WEEKDAY:
				if (cal.get(Calendar.DAY_OF_WEEK) != 1) {
					cal.set(Calendar.DATE, cal.get(Calendar.DATE) - cal.get(Calendar.DAY_OF_WEEK) + 1);
				}
				return cal.getTime();
			case YEAR:
			case MONTH:
				cal.set(Calendar.MONTH, 0);
			case DAY:
				cal.set(Calendar.DATE, 1);
			case HOUR:
				cal.set(Calendar.HOUR, 0);
			case MINUTE:
				cal.set(Calendar.MINUTE, 0);
			case SECOND:
				cal.set(Calendar.SECOND, 0);
				return cal.getTime();
			default:
				return null;
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

}
