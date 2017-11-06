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

public class DateNameFunction extends AbstractScriptFunction {

	@Override
	public String getNames() {
		return "dateName";
	}

	@Override
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			DateEnum datePart = null;
			Calendar cal = Calendar.getInstance();
			String startWeekDay = null;

			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 2)) {
				datePart = DateEnum.valueOf((String) parameters[0]);
				Date date = ExpressionUtil.convertDate(parameters[1]);
				cal.setTime(date);
			} else if (checkParameters(parameters, 3)) {
				datePart = DateEnum.valueOf((String) parameters[0]);
				Date date = ExpressionUtil.convertDate(parameters[1]);
				startWeekDay = (String) parameters[2];
				cal.setTime(date);
			} else {
				throw new NotMatchException(
						ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}

			switch (datePart) {
			case YEAR:
			case DAY:
			case HOUR:
			case MINUTE:
			case SECOND:
				return cal.get(datePart.getCalendarId()) + "";
			case WEEK:
				Calendar start = Calendar.getInstance();
				start.setTime(new Date());
				start.set(Calendar.MONTH, 0);
				start.set(Calendar.DATE, 1);
				return DateUtil.countWeekDiff(start, cal, startWeekDay);
			case WEEKDAY:
				return DateUtil.getWeekday(cal.get(datePart.getCalendarId()) - 1);
			case MONTH:
				return DateUtil.dateToString(cal.getTime(), "MMM");
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
