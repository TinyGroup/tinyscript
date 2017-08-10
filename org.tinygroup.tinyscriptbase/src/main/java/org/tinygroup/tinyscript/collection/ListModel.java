package org.tinygroup.tinyscript.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
/**
 * List集合模型
 * @author yancheng11334
 *
 */
public class ListModel extends AbstractScriptCollectionModel{

	public boolean isCollection(Object object) {
		return object instanceof List;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object executeOperator(String op, Object... parameters)
			throws ScriptException {
		//复制除了第一个元素（集合）以外的其他参数
		Object[] newParameters = new Object[parameters.length];
		System.arraycopy(parameters, 1, newParameters, 1, newParameters.length-1);
		
		//遍历集合
		List list  = (List) parameters[0];
		List result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			newParameters[0] = it.next(); //集合元素作为第一个参数
			result.add(ExpressionUtil.executeOperation(op, newParameters));
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object executeFunction(ScriptSegment segment, ScriptContext context,
			Object object, String methodName, Object... parameters)
			throws ScriptException {
		List list  = (List) object;
		List result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			Object item = it.next(); //集合元素作为第一个参数
			result.add(operate(segment, context, item, methodName, parameters));
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object getAttribute(Object object, Object name)
			throws ScriptException {
		List list  = (List) object;
		List result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			Object item = it.next(); //集合元素作为第一个参数
			result.add(findAttribute(item,name));
		}
		return result;
	}


}
