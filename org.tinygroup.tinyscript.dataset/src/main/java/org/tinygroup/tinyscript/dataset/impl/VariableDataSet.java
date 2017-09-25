package org.tinygroup.tinyscript.dataset.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 可变的数据集，用来解决只读型的数据集无法转换的问题。
 * 
 * @author yancheng11334
 *
 */
public class VariableDataSet extends DynamicDataSet implements Cloneable {

	private DynamicDataSet variableDataSet;

	public VariableDataSet(DataSet dataSet) throws Exception {
		if (dataSet == null) {
			throw new ScriptException(
					ResourceBundleUtil.getDefaultMessage("function.parameter.empty", "VariableDataSet"));
		}
		if (dataSet instanceof DynamicDataSet) {
			variableDataSet = (DynamicDataSet) dataSet;
		} else {
			variableDataSet = buildSimpleDataSet(dataSet);
		}
		this.setName(dataSet.getName());
	}

	public DynamicDataSet getVariableDataSet() {
		return variableDataSet;
	}

	// 只读型的DataSet用SimpleDataSet重新包装一遍。
	private SimpleDataSet buildSimpleDataSet(DataSet dataSet) throws Exception {
		int col = dataSet.getColumns();
		ArrayList<Object[]> list = new ArrayList<Object[]>();
		AbstractDataSet abstractDataSet = (AbstractDataSet) dataSet;
		while (dataSet.next()) {
			Object[] record = new Object[col];
			for (int i = 0; i < col; i++) {
				record[i] = dataSet.getData(abstractDataSet.getShowIndex(i));
			}
			list.add(record);
		}
		Object[][] dataArray = new Object[list.size()][col];
		for (int i = 0; i < list.size(); i++) {
			dataArray[i] = list.get(i);
		}
		return new SimpleDataSet(dataSet.getFields(), dataArray, dataSet.isIndexFromOne());
	}

	public boolean isReadOnly() {
		return false;
	}

	public void first() throws Exception {
		variableDataSet.first();
	}

	public boolean previous() throws Exception {
		return variableDataSet.previous();
	}

	public void beforeFirst() throws Exception {
		variableDataSet.beforeFirst();
	}

	public void afterLast() throws Exception {
		variableDataSet.afterLast();
	}

	public boolean next() throws Exception {
		return variableDataSet.next();
	}

	public boolean absolute(int row) throws Exception {
		return variableDataSet.absolute(row);
	}

	public List<Field> getFields() {
		return variableDataSet.getFields();
	}

	public int getRows() throws Exception {
		return variableDataSet.getRows();
	}

	public int getColumns() throws Exception {
		return variableDataSet.getColumns();
	}

	public <T> T getData(String filedName) throws Exception {
		return variableDataSet.getData(filedName);
	}

	public <T> T getData(int row, int col) throws Exception {
		return variableDataSet.getData(row, col);
	}

	public <T> void setData(int row, int col, T data) throws Exception {
		variableDataSet.setData(row, col, data);
	}

	public <T> T getData(int col) throws Exception {
		return variableDataSet.getData(col);
	}

	public <T> void setData(int col, T data) throws Exception {
		variableDataSet.setData(col, data);
	}

	public <T> void setData(String filedName, T data) throws Exception {
		variableDataSet.setData(filedName, data);
	}

	public DynamicDataSet deleteColumn(int col) throws Exception {
		return variableDataSet.deleteColumn(col);
	}

	public DynamicDataSet deleteColumn(String colName) throws Exception {
		return variableDataSet.deleteColumn(colName);
	}

	public DynamicDataSet deleteRow(int row) throws Exception {
		return variableDataSet.deleteRow(row);
	}

	public DynamicDataSet insertColumn(int col, Field field) throws Exception {
		return variableDataSet.insertColumn(col, field);
	}

	public DynamicDataSet insertRow(int row) throws Exception {
		return variableDataSet.insertRow(row);
	}

	@SuppressWarnings("rawtypes")
	public DynamicDataSet sort(Comparator c) throws Exception {
		return variableDataSet.sort(c);
	}

	public int getCurrentRow() throws Exception {
		return variableDataSet.getCurrentRow();
	}

	public boolean isIndexFromOne() {
		return variableDataSet.isIndexFromOne();
	}

	public void setIndexFromOne(boolean tag) {
		variableDataSet.setIndexFromOne(tag);
	}

	public Object clone() throws CloneNotSupportedException {
		VariableDataSet dataSet = (VariableDataSet) super.clone();
		dataSet.variableDataSet = (DynamicDataSet) variableDataSet.cloneDataSet();
		return dataSet;
	}
}
