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

/**
 * 判断日期之间相差多少秒
 * 
 * @author yancheng11334
 *
 */
public class DateDifferentFunction extends AbstractScriptFunction {

	public String getNames() {
		return "datediff";
	}

	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		DateEnum datePart = null;
		Date d1 = null;
		Date d2 = null;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		String startWeekDay = null;
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 3)) {
				datePart = DateEnum.valueOf((String) parameters[0]);
				d1 = ExpressionUtil.convertDate(parameters[1]);
				d2 = ExpressionUtil.convertDate(parameters[2]);
			} else if (checkParameters(parameters, 4)) {
				datePart = DateEnum.valueOf((String) parameters[0]);
				d1 = ExpressionUtil.convertDate(parameters[1]);
				d2 = ExpressionUtil.convertDate(parameters[2]);
				startWeekDay = (String) parameters[3];
			} else {
				throw new NotMatchException(
						ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
			c1.setTime(d1);
			c2.setTime(d2);
			switch (datePart) {
			case YEAR:
			case MONTH:
			case WEEKDAY:
				return c1.get(datePart.getCalendarId()) - c2.get(datePart.getCalendarId());
			case WEEK:
				return DateUtil.countWeekDiff(c1, c2, startWeekDay);
			case DAY:
				return (d1.getTime() - d2.getTime()) / (1000 * 60 * 60 * 24);
			case HOUR:
				return (d1.getTime() - d2.getTime()) / (1000 * 60 * 60);
			case MINUTE:
				return (d1.getTime() - d2.getTime()) / (1000 * 60);
			case SECOND:
				return (d1.getTime() - d2.getTime()) / 1000;
			}
			return null;
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	

}
