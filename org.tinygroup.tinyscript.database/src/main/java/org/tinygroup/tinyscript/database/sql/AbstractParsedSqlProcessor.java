package org.tinygroup.tinyscript.database.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.parsedsql.JDBCNamedSqlExecutor;
import org.tinygroup.parsedsql.SQLParser;
import org.tinygroup.parsedsql.base.DatabaseType;
import org.tinygroup.parsedsql.impl.DefaultSQLParser;
import org.tinygroup.parsedsql.impl.SimpleJDBCNamedSqlExecutor;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.database.SqlRowSetDataSet;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;
import org.tinygroup.tinyscript.interpret.custom.CustomProcessor;

public abstract class AbstractParsedSqlProcessor implements CustomProcessor {

	private static final String DB_TYPE = "defaultDB";
	private static final String SQL_ALLOW_NULL = "sqlAllowNull";

	protected JDBCNamedSqlExecutor sqlExecutor;
	protected SQLParser sqlParser;

	/**
	 * 提供构造方法,支持new方式创建SqlProcessor
	 */
	public AbstractParsedSqlProcessor() {
		sqlParser = new DefaultSQLParser();
		sqlExecutor = new SimpleJDBCNamedSqlExecutor();
		sqlExecutor.setSqlParser(sqlParser);
	}

	public JDBCNamedSqlExecutor getSqlExecutor() {
		return sqlExecutor;
	}

	public void setSqlExecutor(JDBCNamedSqlExecutor sqlExecutor) {
		this.sqlExecutor = sqlExecutor;
	}

	public SQLParser getSqlParser() {
		return sqlParser;
	}

	public void setSqlParser(SQLParser sqlParser) {
		this.sqlParser = sqlParser;
	}

	protected boolean isSelect(String sql) {
		String s = sql.trim().toLowerCase();
		return s.startsWith("select");
	}

	protected String parseSql(String sql, ScriptContext context) throws ScriptException {
		// 先从上下文查询数据库信息
		String dbType = context.get(DB_TYPE);
		if (dbType == null) {
			// 再从全局配置查询数据库信息
			dbType = ConfigurationUtil.getConfigurationManager().getConfiguration(DB_TYPE);
		}
		if (dbType == null) {
			throw new ScriptException(
					ResourceBundleUtil.getResourceMessage("database", "database.type.error", DB_TYPE));
		}
		DatabaseType databaseType = DatabaseType.valueFrom(dbType);
		return sqlParser.parse(databaseType, sql, context);
	}

	protected Object executeByDataSource(DataSource dataSource, String sql, ScriptContext context)
			throws ScriptException {
		// 对sql语句进行预处理
		String newSql = parseSql(sql, context);
		try {
			if (isSelect(newSql)) {
				// 处理查询语句
				SqlRowSet sqlRowSet = sqlExecutor.queryForSqlRowSet(newSql, dataSource, context);
				return new SqlRowSetDataSet(sqlRowSet, ScriptContextUtil.getScriptEngine(context).isIndexFromOne());
			} else {
				// 处理操作语句
				if(buildNewContext(context)){
					//重构上下文，避免清理逻辑影响原有的业务环境
					ScriptContext newContext = new DefaultScriptContext(context.getTotalItemMap());
					//清理值为null的变量，实现null值不覆盖原有字段值
					clearNullValue(newContext);
					return sqlExecutor.execute(newSql, dataSource, newContext);
				}else{
					return sqlExecutor.execute(newSql, dataSource, context);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ScriptException(e);
		}
	}
	
	/**
	 * 是否需要重建上下文
	 * @param context
	 * @return
	 */
	private boolean buildNewContext(ScriptContext context){
		if(context.exist(SQL_ALLOW_NULL) && !ExpressionUtil.getBooleanValue(context.get(SQL_ALLOW_NULL))){
		   return true;
		}
		return false;
	}
	
	private void clearNullValue(ScriptContext context){
		 //仅清理当前上下文
	     List<String> delKeys = new ArrayList<String>();
	     for(Entry<String, Object> entry:context.getItemMap().entrySet()){
	    	 if(entry.getValue()==null){
	    	    delKeys.add(entry.getKey());
	    	 }
	     }
	     for(String key:delKeys){
	    	 context.getItemMap().remove(key);
	     }
	}

}
