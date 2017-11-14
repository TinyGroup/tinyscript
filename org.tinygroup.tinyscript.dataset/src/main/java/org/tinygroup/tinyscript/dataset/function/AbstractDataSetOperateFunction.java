package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
					pksIndex.add(pkIndex);
				}
			}
		} else if (pks instanceof String) {
			int pkIndex = DataSetUtil.getFieldIndex(dataSet, (String) pks);
			if (pkIndex != -1) {
				pksIndex.add(pkIndex);
			}
		}
		return pksIndex;
	}

	protected Map<String, DataSetRow> createMapDataSetRows(AbstractDataSet dataSet, Object pks, ScriptContext context)
			throws Exception {
		Map<String, DataSetRow> result = new LinkedHashMap<String, DataSetRow>();
		for (int i = 0; i < dataSet.getRows(); i++) {
			String key = createRowKey(dataSet, pks, i, context);
			result.put(key, new DefaultDataSetRow(dataSet, dataSet.getShowIndex(i)));
		}
		return result;
	}

	protected String createRowKey(AbstractDataSet dataSet, Object pks, int row, ScriptContext context)
			throws Exception {
		if (pks instanceof List || pks instanceof String) {
			List<Integer> pksIndex = showPkIndex(dataSet, pks);
			StringBuilder builder = new StringBuilder();
			for (int col : pksIndex) {
				builder.append(dataSet.getData(dataSet.getShowIndex(row), dataSet.getShowIndex(col)));
			}
			return builder.toString();
		} else if (pks instanceof LambdaFunction) {
			LambdaFunction keyFunction = (LambdaFunction) pks;
			ScriptContext subContext = new DefaultScriptContext();
			subContext.setParent(context);
			for (int i = 0; i < dataSet.getFields().size(); i++) {
				subContext.put(dataSet.getFields().get(i).getName(),
						dataSet.getData(dataSet.getShowIndex(row), dataSet.getShowIndex(i)));
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
		Set<Field> fields = new HashSet<Field>();
		for (int i = 0; i < dataSet2.getFields().size(); i++) {
			fields.add(dataSet2.getFields().get(i));
		}
		for (int i = 0; i < dataSet1.getFields().size(); i++) {
			if (!fields.contains(dataSet1.getFields().get(i))) {
				return false;
			}
		}

		return true;
	}

	protected abstract DataSet operate(AbstractDataSet dataArray1, AbstractDataSet dataArray2, Object pks,
			ScriptContext context) throws Exception;

}
