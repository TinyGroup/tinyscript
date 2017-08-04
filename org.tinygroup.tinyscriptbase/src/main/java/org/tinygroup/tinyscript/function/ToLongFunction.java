package org.tinygroup.tinyscript.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

public class ToLongFunction extends AbstractScriptFunction{

	public String getNames() {
		return "long";
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		if(parameters==null || parameters.length==0){
		   throw new ScriptException("long函数的参数格式不正确");
		}
		return ExpressionUtil.convertLong(parameters[0]);
	}


}
