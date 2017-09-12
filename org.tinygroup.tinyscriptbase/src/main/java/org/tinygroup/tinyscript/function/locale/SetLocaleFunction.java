package org.tinygroup.tinyscript.function.locale;

import java.util.Locale;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;

/**
 * 设置默认Locale
 * @author yancheng11334
 *
 */
public class SetLocaleFunction extends AbstractScriptFunction{

	public String getNames() {
		return "setLocale";
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(String.format("%s函数的参数为空!", getNames()));
			} else if (checkParameters(parameters, 1)) {
				 String language = (String) parameters[0];
				 Locale locale = new Locale(language);
				 Locale.setDefault(locale);
				 return locale;
			} else if (checkParameters(parameters, 2)) {
				 String language = (String) parameters[0];
				 String country = (String) parameters[1];
				 Locale locale = new Locale(language,country);
				 Locale.setDefault(locale);
				 return locale;
			} else if (checkParameters(parameters, 3)) {
				 String language = (String) parameters[0];
				 String country = (String) parameters[1];
				 String variant = (String) parameters[2];
				 Locale locale = new Locale(language,country,variant);
				 Locale.setDefault(locale);
				 return locale;
			} else {
				throw new ScriptException(String.format("%函数的参数格式不正确!", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(String.format("%s函数执行发生异常:", getNames()), e);
		}
	}

}
