package org.tinygroup.tinyscript.function.output;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.FunctionCallUtil;

/**
 * 控制台输出函数基类
 * @author yancheng11334
 *
 */
public abstract class ConsoleOutputFunction extends AbstractScriptFunction {

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try {
			return FunctionCallUtil.operate(segment, context, System.out, getNames(), parameters);
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(String.format("执行控制台输出函数%s发生异常:",  getNames()),e);
		}
	}

}
