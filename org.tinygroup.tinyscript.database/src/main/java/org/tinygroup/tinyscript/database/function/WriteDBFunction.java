package org.tinygroup.tinyscript.database.function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;
import org.tinygroup.tinyscript.interpret.ScriptUtil;

/**
 * 序表通过sql写入数据库
 * 
 * @author yancheng11334
 *
 */
public class WriteDBFunction extends AbstractScriptFunction {

	private static final int MAX_RECORDS = 200;

	public String getNames() {
		return "writeDB";
	}

	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 2)) {
				AbstractDataSet ds = (AbstractDataSet) parameters[0];
				String table = (String) parameters[1];
				return insertDataSet(ds, table, null, context);
			} else if (checkParameters(parameters, 3)) {
				AbstractDataSet ds = (AbstractDataSet) parameters[0];
				String table = (String) parameters[1];
				DataSource dataSource = (DataSource) parameters[2];
				return insertDataSet(ds, table, dataSource, context);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	/**
	 * 批量插入序表数据
	 * 
	 * @param ds
	 * @param table
	 * @param dataSource
	 * @return
	 * @throws ScriptException
	 */
	private Object insertDataSet(AbstractDataSet ds, String table, DataSource dataSource, ScriptContext context)
			throws Exception {
		DataSource newDataSource = dataSource == null
				? (DataSource) ScriptUtil.getVariableValue(context, ScriptContextUtil.getCustomBeanName(context))
				: dataSource;
		Connection conn = null;
		PreparedStatement ps = null;
		String insertSql = null;
		try {
			conn = newDataSource.getConnection();
			ResultSetMetaData rsmd = getTableMetaData(table, conn);
			Map<Integer, Integer> maps = createMap(ds, rsmd);
			insertSql = createSQL(ds, table);

			conn.setAutoCommit(false);
			ps = conn.prepareStatement(insertSql);

			int count = 0;
			int[] result = new int[ds.getRows()];
			int i = 0, j = 0;
			try {
				for (i = 0; i < ds.getRows(); i++) {
					for (j = 0; j < ds.getColumns(); j++) {
						int targetSqlType = rsmd.getColumnType(maps.get(j));
						ps.setObject(j + 1, ds.getData(ds.getShowIndex(i), ds.getShowIndex(j)), targetSqlType);
					}
					count++;
					ps.addBatch();
					if (count % MAX_RECORDS == 0) {
						int[] temp = ps.executeBatch(); // 批量提交
						System.arraycopy(temp, 0, result, count - MAX_RECORDS, MAX_RECORDS); // 合并结果
					}
				}
				// 提交剩余记录
				int[] temp = ps.executeBatch();
				System.arraycopy(temp, 0, result, result.length - temp.length, temp.length); // 合并结果
				conn.commit();
			} catch (Exception e) {
				throw new ScriptException(ResourceBundleUtil.getResourceMessage("database",
						"database.sql.position.error", getNames(), insertSql, ds.getShowIndex(i), ds.getShowIndex(j)),
						e);
			}

			return result;

		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(
					ResourceBundleUtil.getResourceMessage("database", "database.sql.error", getNames(), insertSql), e);
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	private Map<Integer, Integer> createMap(DataSet ds, ResultSetMetaData rsmd) throws Exception {
		Map<String, Integer> maps = new HashMap<String, Integer>(); // 定义物理表的字段名和列的映射关系
		Map<Integer, Integer> result = new HashMap<Integer, Integer>(); // 定义字段列和物理表列的映射关系
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			maps.put(rsmd.getColumnName(i).toUpperCase(), i); // 转大写存储
		}
		List<Field> fields = ds.getFields();
		for (int i = 0; i < fields.size(); i++) {
			Field f = fields.get(i);
			String name = f.getName().toUpperCase();
			if (!maps.containsKey(name)) {
				throw new ScriptException(
						ResourceBundleUtil.getResourceMessage("database", "database.field.noexists", f.getName()));
			}
			result.put(i, maps.get(name));
		}
		return result;
	}

	private String createSQL(DataSet ds, String table) throws Exception {
		List<Field> fields = ds.getFields();
		StringBuilder sb = new StringBuilder();
		StringBuilder values = new StringBuilder();
		values.append(" values(");
		sb.append("insert into ").append(table);
		sb.append(" (");
		for (int i = 0; i < fields.size(); i++) {
			Field f = fields.get(i);
			if (i == fields.size() - 1) {
				sb.append(f.getName());
				values.append("?");
			} else {
				sb.append(f.getName()).append(",");
				values.append("?,");
			}
		}
		sb.append(")");
		values.append(")");
		sb.append(values);
		return sb.toString();
	}

	/**
	 * 获取表结构信息
	 * 
	 * @param table
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	private ResultSetMetaData getTableMetaData(String table, Connection conn) throws Exception {
		String sql = "select * from " + table + " where 1=0; "; // 不查数据
		Statement sc = null;
		try {
			sc = conn.createStatement();
			ResultSet rs = sc.executeQuery(sql);
			return rs.getMetaData();
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
	}

}
