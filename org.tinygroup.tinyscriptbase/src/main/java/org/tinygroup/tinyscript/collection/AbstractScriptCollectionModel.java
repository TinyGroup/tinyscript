package org.tinygroup.tinyscript.collection;

import org.tinygroup.tinyscript.ScriptCollectionModel;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.FunctionCallUtil;

/**
 * 抽象类模型类
 * @author yancheng11334
 *
 */
public abstract class AbstractScriptCollectionModel implements ScriptCollectionModel{
    
	protected Object operate(ScriptSegment segment,
			ScriptContext context, Object object, String methodName,
			Object... parameters) throws ScriptException{
		try {
			return FunctionCallUtil.operate(segment, context, object, methodName, parameters);
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(e);
		}
	}
}
