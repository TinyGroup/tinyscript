package org.tinygroup.tinyscript.collection;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * Map集合模型
 * @author yancheng11334
 *
 */
public class MapModel extends AbstractScriptCollectionModel{

	public boolean isCollection(Object object) {
		return object instanceof Map;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object executeOperator(String op, Object... parameters)
			throws ScriptException {
		//复制除了第一个元素（集合）以外的其他参数
		Object[] newParameters = new Object[parameters.length];
		System.arraycopy(parameters, 1, newParameters, 1, newParameters.length-1);
		
		//遍历集合
		Map map= (Map) parameters[0];
		Map result = new HashMap();
		for(Object key:map.keySet()){
		    newParameters[0] = map.get(key); //集合元素作为第一个参数
		    result.put(key, ExpressionUtil.executeOperation(op, newParameters));
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object executeFunction(ScriptSegment segment, ScriptContext context,
			Object object, String methodName, Object... parameters)
			throws ScriptException {
		//遍历集合
		Map map= (Map) object;
		Map result = new HashMap();
		for(Object key:map.keySet()){
		    Object item = map.get(key); //集合元素作为第一个参数
		    result.put(key, operate(segment, context, item, methodName, parameters));
		}
		return result;
	}


}
