package org.tinygroup.tinyscript.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.TypeConvertUtil;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;

/**
 * 无绑定类型的转换函数
 * @author yancheng11334
 *
 */
public class TypeConvertFunction extends DynamicNameScriptFunction{

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		String functionName = ScriptContextUtil.getDynamicFunctionName(context);
		try{
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(String.format("%转换函数的参数为空!", functionName));
			}
			return TypeConvertUtil.convert(functionName, parameters);
		}catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(String.format("%s转换函数的参数格式不正确!", functionName),e);
		}
	}

	public boolean exsitFunctionName(String name) {
		return TypeConvertUtil.exsitType(name);
	}

}
