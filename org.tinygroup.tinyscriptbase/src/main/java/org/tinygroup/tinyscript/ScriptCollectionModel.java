package org.tinygroup.tinyscript;

/**
 * 脚本集合模型
 * @author yancheng11334
 *
 */
public interface ScriptCollectionModel {

	 /**
	  * 判断是否集合
	  * @param t
	  * @return
	  */
	 boolean  isCollection(Object object);
	 
	 
	 /**
	  * 执行操作符
	  * @param op
	  * @param parameters
	  * @return
	  * @throws ScriptException
	  */
	 Object executeOperator(String op,Object...parameters) throws ScriptException;
	 
	 /**
	  * 执行函数
	  * @param segment
	  * @param context
	  * @param object
	  * @param methodName
	  * @param parameters
	  * @return
	  * @throws ScriptException
	  */
	 Object executeFunction(ScriptSegment segment,
				ScriptContext context, Object object, String methodName,
				Object... parameters) throws ScriptException;
}
