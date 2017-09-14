package org.tinygroup.tinyscript.function.date;

import java.util.Date;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.exception.NotMatchException;

/**
 * 判断日期之间相差多少秒
 * @author yancheng11334
 *
 */
public class DateDifferentFunction extends AbstractScriptFunction{

	public String getNames() {
		return "datediff";
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			}else if(checkParameters(parameters, 2) && parameters[0] instanceof Date && parameters[1] instanceof Date){
				Date d1 = (Date) parameters[0];
				Date d2 = (Date) parameters[1];
				return (d1.getTime()-d2.getTime())/1000;
			}else{
				throw new NotMatchException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames())); 
			}
		}catch(ScriptException e){
			throw e;
		}catch(Exception e){
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()),e); 
		}
	}

}
