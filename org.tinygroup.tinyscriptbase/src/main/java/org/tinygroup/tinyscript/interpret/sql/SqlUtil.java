package org.tinygroup.tinyscript.interpret.sql;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;

/**
 * SQL操作语句
 * @author yancheng11334
 *
 */
public final class SqlUtil {

	private static List<SqlProcessor> sqlProcessors = new ArrayList<SqlProcessor>();
	
	private SqlUtil(){
		
	}
	
	public static void addSqlProcessor(SqlProcessor processor){
		for(SqlProcessor sqlProcessor:sqlProcessors){
		    if(sqlProcessor.equals(processor) || sqlProcessor.getClass().isInstance(processor)){
		       return ;
		    }
		}
		sqlProcessors.add(processor);
	}
	
	public static void removeSqlProcessor(SqlProcessor processor){
		sqlProcessors.remove(processor);
	}
	
	/**
	 * 执行sql语句并返回结果
	 * @param sqlObj
	 * @param sql
	 * @param context
	 * @return
	 * @throws ScriptException
	 */
	public static Object executeSql(Object sqlObj,String sql,ScriptContext context) throws ScriptException{
		for(SqlProcessor sqlProcessor:sqlProcessors){
			if(sqlProcessor.isMatch(sqlObj)){
			   return sqlProcessor.executeSql(sqlObj, sql, context);
			}
		}
		throw new ScriptException(String.format("没有找到匹配类型[%s]的SQL处理器", sqlObj.getClass().getName()));
	}
}
