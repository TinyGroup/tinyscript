package org.tinygroup.tinyscript.database.sql;

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
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;
import org.tinygroup.tinyscript.interpret.custom.CustomProcessor;

public abstract class AbstractParsedSqlProcessor implements CustomProcessor {

	private static final String DB_TYPE = "defaultDB";

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
				return sqlExecutor.execute(newSql, dataSource, context);
			}
		} catch (Exception e) {
			throw new ScriptException(e);
		}
	}

}
