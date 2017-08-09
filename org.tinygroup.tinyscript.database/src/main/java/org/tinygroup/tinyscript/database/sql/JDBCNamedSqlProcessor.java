package org.tinygroup.tinyscript.database.sql;

import javax.sql.DataSource;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;

public class JDBCNamedSqlProcessor extends AbstractParsedSqlProcessor{

	public boolean isMatch(Object sqlObj) {
		return sqlObj instanceof DataSource;
	}

	public Object executeSql(Object sqlObj, String sql, ScriptContext context)
			throws ScriptException {
		DataSource dataSource = (DataSource) sqlObj;
		return executeByDataSource(dataSource, sql, context);
	}

}
