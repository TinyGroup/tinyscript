package org.tinygroup.tinyscript.dataset.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * match操作后的结果集
 * 
 * @author yancheng11334
 *
 */
public class MatchDataSet extends DynamicDataSet implements Cloneable {

	private DynamicDataSet left;
	private DynamicDataSet right;
	private List<int[]> matchList = new ArrayList<int[]>();
	private int current = 0;

	/**
	 * 默认构造函数
	 * 
	 * @param left
	 *            左数据集
	 * @param right
	 *            右数据集
	 * @param matchList
	 *            左右数据集行映射关系
	 */
	public MatchDataSet(DataSet left, DataSet right, List<int[]> matchList) {
		this.left = (DynamicDataSet) left;
		this.right = (DynamicDataSet) right;
		this.matchList = matchList;
		resetFields();
	}

	/**
	 * 构造函数
	 * 
	 * @param left
	 * @param right
	 * @param matchList
	 * @param tag
	 */
	public MatchDataSet(DataSet left, DataSet right, List<int[]> matchList, boolean tag) {
		this(left, right, matchList);
		setIndexFromOne(tag);
	}

	/**
	 * 重置字段信息
	 */
	protected void resetFields() {
		List<Field> newFields = new ArrayList<Field>();
		newFields.addAll(left.getFields());
		newFields.addAll(right.getFields());
		setFields(newFields);
	}

	public boolean isReadOnly() {
		return false;
	}

	public void first() throws Exception {
		current = 0;
	}

	public boolean previous() throws Exception {
		if (current > 0) {
			current--;
			return true;
		} else {
			return false;
		}
	}

	public void beforeFirst() throws Exception {
		throw new Exception(
				ResourceBundleUtil.getResourceMessage("dataset", "dataset.operate.nosupport", "beforeFirst"));
	}

	public void afterLast() throws Exception {
		throw new Exception(ResourceBundleUtil.getResourceMessage("dataset", "dataset.operate.nosupport", "afterLast"));
	}

	public boolean next() throws Exception {
		if (current < matchList.size() - 1) {
			current++;
			return true;
		} else {
			return false;
		}
	}

	public boolean absolute(int row) throws Exception {
		if (isIndexFromOne()) {
			if (row >= 1 && row <= matchList.size()) {
				current = row - 1;
				return true;
			}
		} else {
			if (row >= 0 && row <= matchList.size() - 1) {
				current = row;
				return true;
			}
		}

		return false;
	}

	public int getRows() throws Exception {
		return matchList.size();
	}

	public int getColumns() throws Exception {
		return left.getColumns() + right.getColumns();
	}

	public <T> T getData(int row, int col) throws Exception {
		int showRow = getActualIndex(row);
		int[] rows = matchList.get(showRow);
		int leftColumn = left.getColumns();
		if (isIndexFromOne()) {
			if (col >= leftColumn + 1) {
				if (rows[1] >= 0) {
					return right.getData(getShowIndex(rows[1]), col - leftColumn);
				}
			} else {
				if (rows[0] >= 0) {
					return left.getData(getShowIndex(rows[0]), col);
				}
			}
		} else {
			if (col >= leftColumn) {
				if (rows[1] >= 0) {
					return right.getData(getShowIndex(rows[1]), col - leftColumn);
				}
			} else {
				if (rows[0] >= 0) {
					return left.getData(getShowIndex(rows[0]), col);
				}
			}
		}
		return null;

	}

	public <T> void setData(int row, int col, T data) throws Exception {
		int showRow = getActualIndex(row);
		int[] rows = matchList.get(showRow);
		int leftColumn = left.getColumns();
		if (isIndexFromOne()) {
			if (col >= leftColumn + 1) {
				if (rows[1] >= 0) {
					right.setData(getShowIndex(rows[1]), col - leftColumn, data);
				}
			} else {
				if (rows[0] >= 0) {
					left.setData(getShowIndex(rows[0]), col, data);
				}
			}
		} else {
			if (col >= leftColumn) {
				if (rows[1] >= 0) {
					right.setData(getShowIndex(rows[1]), col - leftColumn, data);
				}
			} else {
				if (rows[0] >= 0) {
					left.setData(getShowIndex(rows[0]), col, data);
				}
			}
		}

	}

	public <T> T getData(int col) throws Exception {
		return getData(getShowIndex(current), col);
	}

	public <T> void setData(int col, T data) throws Exception {
		setData(getShowIndex(current), col, data);
	}

	public DynamicDataSet deleteColumn(int col) throws Exception {
		int leftColumn = left.getColumns();
		if (isIndexFromOne()) {
			if (col >= leftColumn + 1) {
				right.deleteColumn(col - leftColumn);
			} else {
				left.deleteColumn(col);
			}
		} else {
			if (col >= leftColumn) {
				right.deleteColumn(col - leftColumn);
			} else {
				left.deleteColumn(col);
			}
		}

		resetFields();
		return this;
	}

	public DynamicDataSet deleteColumn(String colName) throws Exception {
		Integer index = getColumn(colName);
		if (index == null) {
			throw new Exception(ResourceBundleUtil.getResourceMessage("dataset", "dataset.fields.notfound", colName));
		}
		deleteColumn(getShowIndex(index));
		return this;
	}

	public DynamicDataSet deleteRow(int row) throws Exception {
		matchList.remove(getActualIndex(row));
		return this;
	}

	public DynamicDataSet insertColumn(int col, Field field) throws Exception {
		throw new Exception(
				ResourceBundleUtil.getResourceMessage("dataset", "dataset.operate.nosupport", "insertColumn"));
	}

	public DynamicDataSet insertRow(int row) throws Exception {
		throw new Exception(ResourceBundleUtil.getResourceMessage("dataset", "dataset.operate.nosupport", "insertRow"));
	}

	@SuppressWarnings("rawtypes")
	public DynamicDataSet sort(Comparator c) throws Exception {
		throw new Exception(ResourceBundleUtil.getResourceMessage("dataset", "dataset.operate.nosupport", "sort"));
	}

	public int getCurrentRow() throws Exception {
		return getShowIndex(current);
	}

	public void setIndexFromOne(boolean tag) {
		super.setIndexFromOne(tag);
		right.setIndexFromOne(tag);
		left.setIndexFromOne(tag);
	}

	public DataSet clone() throws CloneNotSupportedException {
		try {
			Object[][] dataArray = new Object[matchList.size()][];
			for (int row = 0; row < matchList.size(); row++) {
				dataArray[row] = new Object[getColumns()];
				for (int col = 0; col < getColumns(); col++) {
					dataArray[row][col] = getData(getShowIndex(row), getShowIndex(col));
				}
			}
			List<Field> newFields = new ArrayList<Field>();
			for (Field field : getFields()) {
				newFields.add(field);
			}
			return DataSetUtil.createDynamicDataSet(newFields, dataArray, isIndexFromOne());
		} catch (Exception e) {
			throw new CloneNotSupportedException(ResourceBundleUtil.getResourceMessage("dataset", "dataset.clone.error",
					MatchDataSet.class.getSimpleName()));
		}

	}
}
