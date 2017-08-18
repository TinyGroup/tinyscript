package org.tinygroup.tinyscript.expression.typeconvert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.TypeConvertProcessor;

/**
 * 转换日期类型
 * @author yancheng11334
 *
 */
public class DateTypeConvertProcessor implements TypeConvertProcessor {

	public String getName() {
		return "date";
	}

	public Object convert(Object... parameters) throws Exception {
		if (parameters != null) {
			if (parameters.length == 1) {
				return convertDate(parameters[0], null);
			} else if (parameters.length == 2) {
				return convertDate(parameters[0], (String)parameters[1]);
			}
		}
		return null;
	}

	protected Date convertDate(Object obj, String rule) throws Exception {
		if (obj instanceof String) {
			return convertDateByString((String) obj, rule);
		} else if (obj instanceof Long) {
			return new Date((Long) obj);
		} else if (obj instanceof Date) {
			return (Date) obj;
		} else if (obj instanceof Calendar) {
			return ((Calendar) obj).getTime();
		} else {
			throw new ScriptException(String.format("类型%s的对象不支持转换为Date型", obj
					.getClass().getName()));
		}
	}

	private Date convertDateByString(String s, String rule) throws Exception {
		DateFormat format;
		if (rule != null) {
			format = new SimpleDateFormat(rule);
		} else {
			format = new SimpleDateFormat();
		}
		return format.parse(s);
	}
}
