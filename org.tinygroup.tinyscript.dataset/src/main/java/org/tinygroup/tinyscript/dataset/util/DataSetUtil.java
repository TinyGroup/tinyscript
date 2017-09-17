package org.tinygroup.tinyscript.dataset.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DataSetColumn;
import org.tinygroup.tinyscript.dataset.DataSetRow;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.GroupDataSet;
import org.tinygroup.tinyscript.dataset.impl.AggregateResult;
import org.tinygroup.tinyscript.dataset.impl.DefaultDataSetColumn;
import org.tinygroup.tinyscript.dataset.impl.DefaultDataSetRow;
import org.tinygroup.tinyscript.dataset.impl.SimpleDataSet;
import org.tinygroup.tinyscript.dataset.impl.VariableDataSet;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * 结果集工具类
 * 
 * @author yancheng11334
 *
 */
public final class DataSetUtil {

	private static final Pattern EXCEL_ITEM = Pattern.compile("[a-zA-Z]{1,2}[0-9]{1,5}");
	private static final Pattern EXCEL_CHAR = Pattern.compile("[a-zA-Z]{1,2}");

	/**
	 * 构建动态数据集
	 * 
	 * @param fields
	 * @param dataArray
	 * @return
	 * @throws Exception
	 */
	public static DynamicDataSet createDynamicDataSet(List<Field> fields, Object[][] dataArray) throws Exception {
		return new SimpleDataSet(fields, dataArray);
	}

	/**
	 * 构建动态数据集
	 * 
	 * @param set
	 * @return
	 * @throws Exception
	 */
	public static DynamicDataSet createDynamicDataSet(Set<DataSetRow> set) throws Exception {
		List<Field> fields = set.iterator().next().getFields();
		Object[][] data = new Object[set.size()][fields.size()];
		int rowId = 0;
		for (DataSetRow row : set) {
			for (int i = 0; i < fields.size(); i++) {
				data[rowId][i] = row.getData(i + 1);
			}
			rowId++;
		}
		return new SimpleDataSet(fields, data, set.iterator().next().isIndexFromOne());
	}

	/**
	 * 构建动态数据集
	 * 
	 * @param fields
	 * @param dataArray
	 * @param tag
	 * @return
	 * @throws Exception
	 */
	public static DynamicDataSet createDynamicDataSet(List<Field> fields, Object[][] dataArray, boolean tag)
			throws Exception {
		return new SimpleDataSet(fields, dataArray, tag);
	}

	/**
	 * 构建动态数据集
	 * 
	 * @param fields
	 * @param dataSetRows
	 * @return
	 * @throws Exception
	 */
	public static DynamicDataSet createDynamicDataSet(List<Field> fields, List<DataSetRow> dataSetRows)
			throws Exception {
		Object[][] dataArray = new Object[dataSetRows.size()][];
		for (int i = 0; i < dataSetRows.size(); i++) {
			DataSetRow dataSetRow = dataSetRows.get(i);
			dataArray[i] = new Object[fields.size()];
			for (int j = 0; j < fields.size(); j++) {
				Field field = fields.get(j);
				dataArray[i][j] = (Object) dataSetRow.getData(field.getName());
			}
		}
		return new SimpleDataSet(fields, dataArray);
	}

	/**
	 * 构建动态数据集
	 * 
	 * @param dataSet
	 * @param row
	 * @return
	 * @throws Exception
	 */
	public static DynamicDataSet createDynamicDataSet(DataSet dataSet, int row) throws Exception {
		Object[][] dataArray = new Object[1][];
		dataArray[0] = new Object[dataSet.getColumns()];
		AbstractDataSet abstractDataSet = (AbstractDataSet) dataSet;
		for (int i = 0; i < dataArray[0].length; i++) {
			dataArray[0][i] = dataSet.getData(abstractDataSet.getShowIndex(row), abstractDataSet.getShowIndex(i));
		}
		List<Field> fields = new ArrayList<Field>();
		for (Field field : dataSet.getFields()) {
			fields.add(field);
		}
		DynamicDataSet result = new SimpleDataSet(fields, dataArray);
		result.setIndexFromOne(dataSet.isIndexFromOne());
		return result;
	}

	/**
	 * 构建动态数据集(保持父数据集副本,关联修改)
	 * 
	 * @param dataSet
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	public static DynamicDataSet createDynamicDataSet(DataSet dataSet, List<Integer> rows) throws Exception {
		SimpleDataSet localDataSet = getSimpleDataSet(dataSet);

		if (localDataSet == null) {
			throw new ScriptException(
					String.format("%s类型的数据集无法保持副本,建议转换成SimpleDataSet类型", dataSet.getClass().getName()));
		}

		Object[][] fatherArray = localDataSet.getDataArray();

		if (rows == null) {
			return new SimpleDataSet(dataSet.getFields(), fatherArray, dataSet.isIndexFromOne());
		} else {
			Object[][] childArray = new Object[rows.size()][];
			for (int i = 0; i < rows.size(); i++) {
				childArray[i] = fatherArray[rows.get(i)];
			}
			return new SimpleDataSet(dataSet.getFields(), childArray, dataSet.isIndexFromOne());
		}
	}

	/**
	 * 构建动态数据集(保持父数据集副本,关联修改)
	 * 
	 * @param dataSet
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 * @throws Exception
	 */
	public static DynamicDataSet createDynamicDataSet(DataSet dataSet, int beginIndex, int endIndex) throws Exception {
		beginIndex = beginIndex > 0 ? beginIndex : 0;
		endIndex = endIndex < dataSet.getRows() ? endIndex : dataSet.getRows() - 1;

		SimpleDataSet localDataSet = getSimpleDataSet(dataSet);

		if (localDataSet == null) {
			throw new ScriptException(
					String.format("%s类型的数据集无法保持副本,建议转换成SimpleDataSet类型", dataSet.getClass().getName()));
		}

		Object[][] fatherArray = localDataSet.getDataArray();
		Object[][] childArray = new Object[endIndex - beginIndex + 1][];
		for (int i = beginIndex; i <= endIndex; i++) {
			childArray[i - beginIndex] = fatherArray[i];
		}
		return new SimpleDataSet(dataSet.getFields(), childArray, dataSet.isIndexFromOne());
	}

	private static SimpleDataSet getSimpleDataSet(DataSet dataSet) throws Exception {
		SimpleDataSet localDataSet = null;
		if (dataSet instanceof SimpleDataSet) {
			localDataSet = (SimpleDataSet) dataSet;
		} else if (dataSet instanceof VariableDataSet) {
			VariableDataSet variableDataSet = (VariableDataSet) dataSet;
			if (variableDataSet.getVariableDataSet() instanceof SimpleDataSet) {
				localDataSet = (SimpleDataSet) variableDataSet.getVariableDataSet();
			}
		}
		return localDataSet;
	}

	/**
	 * 构建数据集的行对象
	 * 
	 * @param dataSet
	 * @param row
	 * @return
	 * @throws Exception
	 */
	public static DataSetRow createDataSetRow(DataSet dataSet, int row) throws Exception {
		return new DefaultDataSetRow(dataSet, row);
	}

	/**
	 * 构建数据集的列对象
	 * 
	 * @param dataSet
	 * @param col
	 * @return
	 * @throws Exception
	 */
	public static DataSetColumn createDataSetColumn(DataSet dataSet, int col) throws Exception {
		return new DefaultDataSetColumn(dataSet, col);
	}

	/**
	 * 构建数据集的列对象
	 * 
	 * @param dataSet
	 * @param colName
	 * @return
	 * @throws Exception
	 */
	public static DataSetColumn createDataSetColumn(DataSet dataSet, String colName) throws Exception {
		return new DefaultDataSetColumn(dataSet, colName);
	}

	/**
	 * 返回索引字段对应的列
	 * 
	 * @param dataSet
	 * @param colName
	 * @return
	 * @throws Exception
	 */
	public static int getFieldIndex(DataSet dataSet, String colName) throws Exception {
		for (int i = 0; i < dataSet.getFields().size(); i++) {
			Field field = dataSet.getFields().get(i);
			if (field.getName().equalsIgnoreCase(colName)) {
				return i;
			}
		}
		if (dataSet instanceof GroupDataSet) {
			GroupDataSet groupDataSet = (GroupDataSet) dataSet;
			List<AggregateResult> result = groupDataSet.getAggregateResultList();
			for (int i = 0; i < result.size(); i++) {
				if (colName.equalsIgnoreCase(result.get(i).getName())) {
					return groupDataSet.getFields().size() + i;
				}
			}
		}
		return -1;
	}

	/**
	 * 返回索引字段对应的列
	 * 
	 * @param fields
	 * @param colName
	 * @return
	 * @throws Exception
	 */
	public static int getFieldIndex(List<Field> fields, String colName) throws Exception {
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			if (field.getName().equalsIgnoreCase(colName)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 设置行上下文参数
	 * 
	 * @param dataSet
	 * @param context
	 * @param row
	 * @throws Exception
	 */
	public static void setRowContext(AbstractDataSet dataSet, ScriptContext context, int row) throws Exception {
		// 将行记录按名称放入到上下文
		for (int j = 0; j < dataSet.getFields().size(); j++) {
			String name = dataSet.getFields().get(j).getName();
			context.put(name, dataSet.getData(dataSet.getShowIndex(row), dataSet.getShowIndex(j)));
		}
		if (dataSet instanceof GroupDataSet) {
			GroupDataSet groupDataSet = (GroupDataSet) dataSet;
			for (AggregateResult result : groupDataSet.getAggregateResultList()) {
				context.put(result.getName(), result.getData(row));
			}
		}
	}

	/**
	 * 清理行上下文参数(仅清理当前层次的上下文)
	 * 
	 * @param dataSet
	 * @param context
	 * @throws Exception
	 */
	public static void clearRowContext(AbstractDataSet dataSet, ScriptContext context) throws Exception {
		for (int j = 0; j < dataSet.getFields().size(); j++) {
			String name = dataSet.getFields().get(j).getName();
			context.getItemMap().remove(name);
		}
		if (dataSet instanceof GroupDataSet) {
			GroupDataSet groupDataSet = (GroupDataSet) dataSet;
			for (AggregateResult result : groupDataSet.getAggregateResultList()) {
				context.getItemMap().remove(result.getName());
			}
		}
	}

	/**
	 * 判断是否整型
	 * 
	 * @param item
	 * @return
	 */
	public static boolean isNumber(Object item) {
		try {
			return ExpressionUtil.convertInteger(item) != null;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获取值
	 * 
	 * @param item
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static Object getValue(Object item, ScriptContext context) throws Exception {
		if (isNumber(item)) {
			return ExpressionUtil.convertInteger(item);
		}
		Object value = context.get(item.toString());
		if (value != null) {
			return value;
		}
		return item;
	}

	/**
	 * 判断是否字段(注意包含Excel访问方式)
	 * 
	 * @param item
	 * @param context
	 * @return
	 */
	public static boolean isField(Object item, ScriptContext context) {
		if (isNumber(item)) {
			return false;
		}
		Object value = context.get(item.toString());
		if (value != null) {
			return !isNumber(value);
		}
		return true;
	}

	/**
	 * 判断是否Excel单元格下标方式
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isExcelCell(String s) {
		return EXCEL_ITEM.matcher(s).matches();
	}

	/**
	 * 获取Excel单元格下标方式
	 * 
	 * @param s
	 * @return
	 */
	public static int[] getExcelCell(String s) {
		int[] cell = new int[2];
		try {
			s = s.toUpperCase();
			Matcher m = EXCEL_CHAR.matcher(s);
			m.find();
			char[] cs = m.group().toCharArray();
			if (cs.length == 1) {
				cell[0] = (int) cs[0] - 65;
			} else {
				cell[0] = ((int) cs[0] - 64) * 26 + ((int) cs[1] - 65);
			}
			cell[1] = Integer.parseInt(s.substring(cs.length, s.length())) - 1;
		} catch (Exception e) {
			throw new RuntimeException(String.format("转换Excel单元格下标方式失败:坐标[%s]", s));
		}

		return cell;
	}

	/**
	 * 取得需要进行数组处理的字段
	 * 
	 * @param dataSet
	 * @param expression
	 * @return
	 * @throws Exception
	 */
	public static Set<Integer> getFieldArray(DataSet dataSet, String expression) throws Exception {
		Set<Integer> columns = new HashSet<Integer>();
		for (int i = 0; i < dataSet.getFields().size(); i++) {
			Field field = dataSet.getFields().get(i);
			Pattern FIELD_ARRAY_RULE = Pattern.compile(field.getName() + "\\[.*\\]");
			if (FIELD_ARRAY_RULE.matcher(expression).find()) {
				columns.add(i);
			}
		}
		return columns;
	}

	public static Set<DataSetRow> createDataSetRows(DataSet dataSet) {
		Set<DataSetRow> result = new HashSet<DataSetRow>();

		return result;
	}

}
