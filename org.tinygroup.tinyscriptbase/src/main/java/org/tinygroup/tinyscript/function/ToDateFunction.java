package org.tinygroup.tinyscript.function;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;

/**
 * 转换日期格式
 * @author yancheng11334
 *
 */
public class ToDateFunction extends AbstractScriptFunction{

	public String getNames() {
		return "date";
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
			if(parameters == null || parameters.length == 0) {
				throw new ScriptException("date函数的参数为空!");
			}else if(checkParameters(parameters, 1)){
				return convertDate(parameters[0],null);
			}else if(checkParameters(parameters, 2)){
				return convertDate(parameters[0],(String)parameters[1]);
			}else{
				throw new ScriptException("date函数的参数格式不正确!");
			}
		}catch(ScriptException e){
			throw e;
		}catch(Exception e){
			throw new ScriptException("date函数的参数格式不正确!", e);
		}
	}
	
	protected Date convertDate(Object obj,String rule) throws Exception{
		if(obj instanceof String){
		    return convertDateByString((String)obj,rule);
		}else if(obj instanceof Long){
			return new Date((Long)obj);
		}else if(obj instanceof Date){
			return (Date) obj;
		}else if(obj instanceof Calendar){
			return ((Calendar) obj).getTime();
		}else{
			throw new ScriptException(String.format("类型%s的对象不支持转换为Date型", obj.getClass().getName()));
		}
	}
	
	private Date convertDateByString(String s,String rule) throws Exception{
		DateFormat format;
		if(rule!=null){
		   format = new SimpleDateFormat(rule);
		}else{
		   format = new SimpleDateFormat();
		}
		return format.parse(s);
	}

}
