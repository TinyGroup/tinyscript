package org.tinygroup.tinyscript.collection;

import java.lang.reflect.Array;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * 数组集合模型
 * @author yancheng11334
 *
 */
public class ArrayModel extends AbstractScriptCollectionModel{

	public boolean isCollection(Object object) {
		return object.getClass().isArray();
	}

	public Object executeOperator(String op, Object... parameters)
			throws ScriptException {
		//复制除了第一个元素（集合）以外的其他参数
		Object[] newParameters = new Object[parameters.length];
		System.arraycopy(parameters, 1, newParameters, 1, newParameters.length-1);
		
		//遍历数组
		Object[] result = new Object[Array.getLength(parameters[0])];
		for(int i=0;i<result.length;i++){
			newParameters[0] = Array.get(parameters[0], i);
			result[i] = ExpressionUtil.executeOperation(op, newParameters);
		}
		return result;
	}

	public Object executeFunction(ScriptSegment segment, ScriptContext context,
			Object object, String methodName, Object... parameters)
			throws ScriptException {
		Object[] result = new Object[Array.getLength(object)];
		for(int i=0;i<result.length;i++){
			Object item = Array.get(object, i);
			result[i] = operate(segment, context, item, methodName, parameters);
		}
		return result;
	}

	public Object getAttribute(Object object, Object name)
			throws ScriptException {
		Object[] result = new Object[Array.getLength(object)];
		for(int i=0;i<result.length;i++){
			Object item = Array.get(object, i);
			result[i] = findAttribute(item,name);
		}
		return result;
	}

}
