package org.tinygroup.tinyscript.function.date;

import java.util.Date;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.exception.NotMatchException;

/**
 * 清除时间部分
 * @author yancheng11334
 *
 */
public class ClearTimeFunction extends AbstractScriptFunction{

	public String getNames() {
		return "clearTime";
	}
	
	public String getBindingTypes() {
		return Date.class.getName();
	}

	@SuppressWarnings("deprecation")
	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(String.format("%s函数的参数为空!",getNames()));
			}else if(checkParameters(parameters, 1) && parameters[0] instanceof Date){
				Date d1 = (Date) parameters[0];
				d1.setHours(0);
                d1.setMinutes(0);
                d1.setSeconds(0);
				return d1;
			}else{
				throw new NotMatchException(String.format("%s函数的参数格式不正确!",getNames()));
			}
		}catch(ScriptException e){
			throw e;
		}catch(Exception e){
			throw new ScriptException(String.format("%s函数的参数格式不正确!",getNames()), e);
		}
	}
}
