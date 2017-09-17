package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DataSetRow;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.RowComparator;
import org.tinygroup.tinyscript.dataset.impl.DefaultDataSetRow;
import org.tinygroup.tinyscript.dataset.impl.LambdaRowComparator;
import org.tinygroup.tinyscript.dataset.impl.ListRowComparator;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.LambdaFunction;

public abstract class AbstractDataSetOperateFunction extends AbstractScriptFunction {

	@Override
	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	/**批量转换主键（string→int）
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

	/**创建行集合
	 * @param dataSet
	 * @param pks
	 * @param context
	 * @return
	 * @throws Exception
	 */
	protected Set<DataSetRow> createDataSetRows(AbstractDataSet dataSet, Object pks, ScriptContext context)
			throws Exception {
		Set<DataSetRow> rows = new HashSet<DataSetRow>();
		for (int i = 1; i <= dataSet.getRows(); i++) {
			rows.add(new DefaultDataSetRow(dataSet, i, createRowComparator(dataSet, pks, context)));
		}
		return rows;
	}

	/**
	 * 创建行比较器
	 * @param dataSet
	 * @param pks
	 * @param context
	 * @return
	 * @throws Exception
	 */
	protected RowComparator createRowComparator(DataSet dataSet, Object pks, ScriptContext context) throws Exception {
		if (pks instanceof List || pks instanceof String) {
			List<Integer> pksIndex = showPkIndex(dataSet, pks);
			return new ListRowComparator(pksIndex);
		} else if (pks instanceof LambdaFunction) {
			return new LambdaRowComparator((LambdaFunction) pks, context);
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
