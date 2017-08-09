package org.tinygroup.tinyscript.interpret.sql;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;

/**
 * SQL语句执行器
 * @author yancheng11334
 *
 */
public interface SqlProcessor {

	/**
	 * 判断处理器是否匹配当前对象
	 * @param sqlObj
	 * @return
	 */
	boolean isMatch(Object sqlObj);
	
	/**
	 * 执行sql并返回结果
	 * @param sqlObj
	 * @param sql
	 * @param context
	 * @return
	 * @throws ScriptException
	 */
	Object executeSql(Object sqlObj,String sql,ScriptContext context) throws ScriptException;
}
