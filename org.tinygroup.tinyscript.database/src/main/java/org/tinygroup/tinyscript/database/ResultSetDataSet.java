package org.tinygroup.tinyscript.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.Field;

/**
 * Created by luoguo on 2014/7/4.
 */
public class ResultSetDataSet extends AbstractDataSet {
	private final ResultSet resultSet;
	private int columnCount;

	public ResultSetDataSet(ResultSet resultSet) {
		this.resultSet = resultSet;
		try {
			ResultSetMetaData metaData=resultSet.getMetaData();
			columnCount=metaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				Field field=new Field();
				field.setName(metaData.getColumnName(i));
				field.setTitle(metaData.getColumnLabel(i));
				field.setType(metaData.getColumnClassName(i));
				fields.add(field);
			}
			setFields(fields);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public ResultSetDataSet(ResultSet resultSet,boolean tag) {
		this(resultSet);
		this.setIndexFromOne(tag);
	}
	
	public boolean isReadOnly() {
		try {
			return resultSet.getConcurrency() == ResultSet.CONCUR_READ_ONLY;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void checkNoForwardType() throws Exception {
		try {
			if (resultSet.getType() == ResultSet.TYPE_FORWARD_ONLY) {
				throw new Exception("结果集只支持向前滚动");
			}
		} catch (SQLException e) {
			throw new Exception(e);
		}
	}

	public void first() throws Exception {
		checkNoForwardType();
		try {
			resultSet.first();
		} catch (SQLException e) {
			throw new Exception(e);
		}
	}

	public boolean previous() throws Exception {
		checkNoForwardType();
		try {
			return resultSet.previous();
		} catch (SQLException e) {
			throw new Exception(e);
		}
	}

	public void beforeFirst() throws Exception {
		checkNoForwardType();
		try {
			resultSet.beforeFirst();
		} catch (SQLException e) {
			throw new Exception(e);
		}

	}

	public void afterLast() throws Exception {
		checkNoForwardType();
		try {
			resultSet.afterLast();
		} catch (SQLException e) {
			throw new Exception(e);
		}
	}

	public boolean next() throws Exception {
		try {
			return resultSet.next();
		} catch (SQLException e) {
			throw new Exception(e);
		}

	}

	public boolean absolute(int row) throws Exception {
		checkNoForwardType();
		try {
			if(isIndexFromOne()){
				return resultSet.absolute(row);
			}else{
				return resultSet.absolute(row+1);
			}
			
		} catch (SQLException e) {
			throw new Exception(e);
		}
	}

	public int getRows() throws Exception {
		throw new Exception("不支持的方法！");
	}

	public int getColumns() throws Exception {
		return columnCount;
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
		try {
			if(isIndexFromOne()){
				return (T) resultSet.getObject(col);
			}else{
				return (T) resultSet.getObject(col + 1);
			}
			
		} catch (SQLException e) {
			throw new Exception(e);
		}
	}

	public <T> void setData(int col, T data) throws Exception {
		throwNotSupportMethod();
	}

	public <T> void setData(String filedName, T data) throws Exception {
		throwNotSupportMethod();
	}

	public void clean() {
		super.clean();
		try {
			resultSet.close();
		} catch (SQLException e) {
			// TODO doNothing
		}
	}
}
