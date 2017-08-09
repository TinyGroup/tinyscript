package org.tinygroup.tinyscript.database;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;

/**
 * 包装Spring记录集原生类型
 * @author yancheng11334
 *
 */
public class SqlRowSetDataSet extends AbstractDataSet{
    private SqlRowSet sqlRowSet;
    
	public SqlRowSetDataSet(SqlRowSet sqlRowSet){
		this.sqlRowSet = sqlRowSet;
	}
	
	public SqlRowSetDataSet(SqlRowSet sqlRowSet,boolean tag){
		this(sqlRowSet);
		this.setIndexFromOne(tag);
	}
	
	/**
	 * 返回Spring记录集原生类型
	 * @return
	 */
	public SqlRowSet getSqlRowSet() {
		return sqlRowSet;
	}

	public boolean isReadOnly() {
		return true;
	}

	public void first() throws Exception {
		sqlRowSet.first();
	}

	public boolean previous() throws Exception {
		return sqlRowSet.previous();
	}

	public void beforeFirst() throws Exception {
		sqlRowSet.beforeFirst();
	}

	public void afterLast() throws Exception {
		sqlRowSet.afterLast();
	}

	public boolean next() throws Exception {
		return sqlRowSet.next();
	}

	public boolean absolute(int row) throws Exception {
		return sqlRowSet.absolute(row);
	}

	public int getRows() throws Exception {
		throwNotSupportMethod();
		return 0;
	}

	public int getColumns() throws Exception {
		return sqlRowSet.getMetaData().getColumnCount();
	}

	public <T> T getData(int row, int col) throws Exception {
		if (absolute(row)) {
			return  getData(col);
		}
		throw new Exception("不存在" + row + "行的数据！");
	}

	public <T> void setData(int row, int col, T data) throws Exception {
		throwNotSupportMethod();
	}

	@SuppressWarnings("unchecked")
	public <T> T getData(int col) throws Exception {
		if(isIndexFromOne()){
			return (T) sqlRowSet.getObject(col);
		}else{
			return (T) sqlRowSet.getObject(col+1);
		}
	}

	public <T> void setData(int col, T data) throws Exception {
		throwNotSupportMethod();
	}

}
