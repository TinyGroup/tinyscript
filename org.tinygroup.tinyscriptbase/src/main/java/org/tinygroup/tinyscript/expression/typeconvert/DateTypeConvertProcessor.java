package org.tinygroup.tinyscript.expression.typeconvert;

import java.util.Calendar;
import java.util.Date;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.TypeConvertProcessor;
import org.tinygroup.tinyscript.function.date.DateUtil;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 转换日期类型
 * 
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
				if (parameters[1] instanceof String)
					return convertDate(parameters[0], (String) parameters[1]);
				else if (parameters[1] instanceof Object[]) {
					return convertDate(parameters[0], (String) ((Object[]) parameters[1])[0]);
				}
			}
		}
		return null;
	}

	protected Date convertDate(Object obj, String rule) throws Exception {
		if (obj instanceof String) {
			return DateUtil.convertDateByString((String) obj, rule);
		} else if (obj instanceof Long) {
			return new Date((Long) obj);
		} else if (obj instanceof Date) {
			return (Date) obj;
		} else if (obj instanceof Calendar) {
			return ((Calendar) obj).getTime();
		} else {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("number.convert.error",
					obj.getClass().getName(), Date.class.getName()));
		}
	}
}
