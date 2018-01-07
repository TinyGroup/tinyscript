package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.GroupDataSet;
import org.tinygroup.tinyscript.dataset.impl.AggregateResult;
import org.tinygroup.tinyscript.dataset.impl.MultiLevelGroupDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 分组函数抽象基类
 * 
 * @author yancheng11334
 *
 */
public abstract class AbstractGroupFunction extends AbstractScriptFunction {

	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	public boolean enableExpressionParameter() {
		return true;
	}

	/**
	 * 具体分组逻辑
	 * 
	 * @param dataSet
	 * @param fields
	 * @return
	 * @throws Exception
	 */
	protected List<DynamicDataSet> group(DynamicDataSet dataSet, String[] fields, Object... params) throws Exception {
		Map<Object, DynamicDataSet> result = new LinkedHashMap<Object, DynamicDataSet>();
		try {
			int[] showColumns = getShowColumns(dataSet, fields);
			int rowNum = dataSet.getRows();

			// 逐条遍历记录
			for (int i = 0; i < rowNum; i++) {
				int showRow = dataSet.getShowIndex(i);
				Object key = createKey(showRow, showColumns, dataSet, params);
				DynamicDataSet groupDataSet = result.get(key);
				if (groupDataSet == null) {
					// 新建分组结果的数据集
					groupDataSet = DataSetUtil.createDynamicDataSet(dataSet, i);
					result.put(key, groupDataSet);
				} else {
					// 更新分组结果的数据集
					int groupRowNum = groupDataSet.getRows();
					int groupShowRow = groupDataSet.getShowIndex(groupRowNum);
					groupDataSet.insertRow(groupShowRow);
					for (int j = 0; j < groupDataSet.getColumns(); j++) {
						int showColumn = groupDataSet.getShowIndex(j);
						groupDataSet.setData(groupShowRow, showColumn, dataSet.getData(showRow, showColumn));
					}
				}
			}

		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
		return new ArrayList<DynamicDataSet>(result.values());
	}

	protected int[] getShowColumns(DynamicDataSet dataSet, String[] fields) throws Exception {
		int[] columns = DataSetUtil.getFieldIndex(dataSet.getFields(), fields);
		int[] showColumns = new int[columns.length];
		for (int i = 0; i < columns.length; i++) {
			if (columns[i] < 0) {
				throw new ScriptException(
						ResourceBundleUtil.getResourceMessage("dataset", "dataset.fields.notfound", fields[i]));
			}
			showColumns[i] = dataSet.getShowIndex(columns[i]);
		}
		return showColumns;
	}

	protected Object createKey(int showRow, int[] showColumns, AbstractDataSet dataSet, Object... params)
			throws Exception {
		List<Object> keys = new ArrayList<Object>();
		for (int showColumn : showColumns) {
			keys.add(dataSet.getData(showRow, showColumn));
		}
		return keys;
	}

	// 更新上下文
	protected ScriptContext updateScriptContext(AbstractDataSet dataSet, int row, ScriptContext context)
			throws Exception {
		ScriptContext subContext = new DefaultScriptContext();
		subContext.setParent(context);
		for (int i = 0; i < dataSet.getFields().size(); i++) {
			Field field = dataSet.getFields().get(i);
			subContext.put(field.getName(), dataSet.getData(dataSet.getShowIndex(row), dataSet.getShowIndex(i)));
		}
		return subContext;
	}

	protected void updateAggregateResult(List<MultiLevelGroupDataSet> subDataSetList,
			List<AggregateResult> aggregateResultList) throws Exception {
		GroupDataSetAggregateFunction aggregateFunction = BeanContainerFactory
				.getBeanContainer(getClass().getClassLoader()).getBean(GroupDataSetAggregateFunction.class);
		for (AggregateResult result : aggregateResultList) {
			String fieldName = result.getField().getName();
			String functionName = result.getFunctionName();
			Object[] params = result.getParams();
			for (MultiLevelGroupDataSet dataSet : subDataSetList) {
				aggregateFunction.executeGroupDataSet((GroupDataSet) dataSet, fieldName, functionName, params);
			}
		}
	}

}
