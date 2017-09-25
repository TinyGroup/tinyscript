package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DataSetRow;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.impl.DefaultDataSetRow;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;

public abstract class AbstractDataSetOperateFunction extends AbstractScriptFunction {

	@Override
	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	/**
	 * 批量转换主键（string→int）
	 * 
	 * @param dataSet
	 * @param pks
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected List<Integer> showPkIndex(DataSet dataSet, Object pks) throws Exception {
		List<Integer> pksIndex = new ArrayList<Integer>();
		if (pks instanceof List) {
			for (String pk : (List<String>) pks) {
				int pkIndex = DataSetUtil.getFieldIndex(dataSet, pk);
				if (pkIndex != -1) {
					pksIndex.add(dataSet.isIndexFromOne() ? pkIndex + 1 : pkIndex);
				}
			}
		} else if (pks instanceof String) {
			int pkIndex = DataSetUtil.getFieldIndex(dataSet, (String) pks);
			if (pkIndex != -1) {
				pksIndex.add(dataSet.isIndexFromOne() ? pkIndex + 1 : pkIndex);
			}
		}
		return pksIndex;
	}

	

	protected Map<String, DataSetRow> createMapDataSetRows(AbstractDataSet dataSet, Object pks, ScriptContext context)
			throws Exception {
		Map<String, DataSetRow> result = new LinkedHashMap<String, DataSetRow>();
		for (int i = 1; i <= dataSet.getRows(); i++) {
			String key = createRowKey(dataSet, pks, i, context);
			result.put(key, new DefaultDataSetRow(dataSet, i));
		}
		return result;
	}

	protected String createRowKey(AbstractDataSet dataSet, Object pks, int row, ScriptContext context)
			throws Exception {
		if (pks instanceof List || pks instanceof String) {
			List<Integer> pksIndex = showPkIndex(dataSet, pks);
			StringBuilder builder = new StringBuilder();
			for (int col : pksIndex) {
				builder.append(dataSet.getData(row, col));
			}
			return builder.toString();
		} else if (pks instanceof LambdaFunction) {
			LambdaFunction keyFunction = (LambdaFunction) pks;
			ScriptContext subContext = new DefaultScriptContext();
			subContext.setParent(context);
			for (int i = 0; i < dataSet.getFields().size(); i++) {
				subContext.put(dataSet.getFields().get(i).getName(), dataSet.getData(row, i + 1));
			}
			return keyFunction.execute(subContext).getResult().toString();

		}
		return null;
	}

	/**
	 * 检查两个序表的列属性是否完全一致
	 * 
	 * @param dataSet1
	 * @param dataSet2
	 * @return
	 * @throws Exception
	 */
	protected boolean checkField(DataSet dataSet1, DataSet dataSet2) throws Exception {
		if (dataSet1.getColumns() != dataSet2.getColumns()) {
			return false;
		} else {
			for (int i = 0; i < dataSet1.getFields().size(); i++) {
				Field field1 = dataSet1.getFields().get(i);
				Field field2 = dataSet2.getFields().get(i);
				if (!field1.equals(field2)) {
					return false;
				}
			}
		}
		return true;
	}

	protected abstract DataSet operate(AbstractDataSet dataArray1, AbstractDataSet dataArray2, Object pks,
			ScriptContext context) throws Exception;

}
