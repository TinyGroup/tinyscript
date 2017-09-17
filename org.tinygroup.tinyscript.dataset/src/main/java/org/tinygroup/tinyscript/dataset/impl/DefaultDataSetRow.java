package org.tinygroup.tinyscript.dataset.impl;

import java.util.List;

import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DataSetRow;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.RowComparator;

/**
 * 默认的数据集行对象实现
 * 
 * @author yancheng11334
 *
 */
public class DefaultDataSetRow implements DataSetRow {

	private DataSet dataSet;
	private int row; // 显示值
	private RowComparator comparator;

	@Override
	public boolean equals(Object o) {
		if (comparator == null) {
			return super.equals(o);
		}
		return comparator.isEqual(this, (DataSetRow) o);
	}

	@Override
	public int hashCode() {
		if (comparator == null) {
			return super.hashCode();
		}
		return comparator.countHash(this);
	}

	public DefaultDataSetRow(DataSet dataSet, int row) throws Exception {
		this.dataSet = dataSet;
		this.row = row;
	}

	public DefaultDataSetRow(DataSet dataSet, int row, RowComparator comparator) throws Exception {
		this.dataSet = dataSet;
		this.row = row;
		this.comparator = comparator;
	}

	public List<Field> getFields() {
		return dataSet.getFields();
	}

	public <T> T getData(int col) throws Exception {
		dataSet.absolute(row);
		return dataSet.getData(row, col);
	}

	public <T> T getData(String colName) throws Exception {
		dataSet.absolute(row);
		return dataSet.getData(colName);
	}

	public <T> void setData(int col, T data) throws Exception {
		dataSet.absolute(row);
		dataSet.setData(row, col, data);
	}

	public <T> void setData(String colName, T data) throws Exception {
		dataSet.absolute(row);
		dataSet.setData(colName, data);
	}

	public boolean isIndexFromOne() {
		return dataSet.isIndexFromOne();
	}

}
