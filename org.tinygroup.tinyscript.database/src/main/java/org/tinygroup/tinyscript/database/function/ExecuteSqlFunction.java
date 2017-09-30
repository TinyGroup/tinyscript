package org.tinygroup.tinyscript.database.function;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 扩展数据源操作sql，自动释放Connection
 * @author yancheng11334
 *
 */
public class ExecuteSqlFunction extends AbstractScriptFunction {

	public String getNames() {
		return "execute";
	}
	
	public String getBindingTypes() {
		return "javax.sql.DataSource,java.sql.Connection";
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			}else if(parameters.length == 2 && parameters[0] != null && parameters[1] != null){
				String sql = (String) parameters[1];
				if(parameters[0] instanceof DataSource){
					return executeDataSource((DataSource)parameters[0],sql);
				}else if(parameters[0] instanceof Connection){
					return executeConnection((Connection)parameters[0],sql);
				}else{
					throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
				}
			}else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		}catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}
	
	private int executeDataSource(DataSource dataSource,String sql) throws Exception{
		Connection conn = null;
		Statement statement=null;
		try{
			conn = dataSource.getConnection();
			statement = conn.createStatement();
			return statement.executeUpdate(sql);
		}finally{
			if(statement!=null){
			   statement.close();
			}
			if(conn!=null){
			   conn.close();
			}
		}
	}
	
	private int executeConnection(Connection conn,String sql) throws Exception{
		Statement statement=null;
		try{
			statement = conn.createStatement();
			return statement.executeUpdate(sql);
		}finally{
			if(statement!=null){
			   statement.close();
			}
		}
	}

}
