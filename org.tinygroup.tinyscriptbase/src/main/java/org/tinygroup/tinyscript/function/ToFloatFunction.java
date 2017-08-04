package org.tinygroup.tinyscript.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

public class ToFloatFunction extends AbstractScriptFunction{

	public String getNames() {
		return "float";
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		if(parameters==null || parameters.length==0){
		   throw new ScriptException("float函数的参数格式不正确");
		}
		return ExpressionUtil.convertFloat(parameters[0]);
	}


}
