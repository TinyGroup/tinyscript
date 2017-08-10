package org.tinygroup.tinyscript.collection;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * Set集合模型
 * @author yancheng11334
 *
 */
public class SetModel extends AbstractScriptCollectionModel{

	public boolean isCollection(Object object) {
		return object instanceof Set;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object executeOperator(String op, Object... parameters)
			throws ScriptException {
		//复制除了第一个元素（集合）以外的其他参数
		Object[] newParameters = new Object[parameters.length];
		System.arraycopy(parameters, 1, newParameters, 1, newParameters.length-1);
		
		//遍历集合
		Set set  = (Set) parameters[0];
		Set result = new HashSet();
		Iterator it = set.iterator();
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
		//遍历集合
		Set set  = (Set) object;
		Set result = new HashSet();
		Iterator it = set.iterator();
		while(it.hasNext()){
			Object item = it.next();
			result.add(operate(segment, context, item, methodName, parameters));
		}
		return result;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object getAttribute(Object object, Object name)
			throws ScriptException {
		//遍历集合
		Set set  = (Set) object;
		Set result = new HashSet();
		Iterator it = set.iterator();
		while(it.hasNext()){
			Object item = it.next();
			result.add(findAttribute(item,name));
		}
	    return result;
	}


}
