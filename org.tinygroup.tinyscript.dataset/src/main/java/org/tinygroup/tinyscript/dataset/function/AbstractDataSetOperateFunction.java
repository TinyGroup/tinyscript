package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.impl.SimpleDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.LambdaFunction;

public abstract class AbstractDataSetOperateFunction extends AbstractScriptFunction {

	@Override
	public String getBindingTypes() {
		return DataSet.class.getName();
	}

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

	protected int checkRowData(Object[] rowData, Object[][] dataArray, List<Integer> pks) {
		for (int i = 0; i < dataArray.length; i++) {
			boolean flag = true;
			for (int pk : pks) {
				if (!rowData[pk].equals(dataArray[i][pk])) {
					flag = false;
					break;
				}
			}
			if (flag) {
				return i;
			}
		}
		return -1;
	}

	protected int checkRowData(SimpleDataSet dataSet1, SimpleDataSet dataSet2, int row, LambdaFunction pks,
			ScriptContext context) throws Exception {
		int flag = -1;
		for (int j = 1; j <= dataSet2.getRows(); j++) {
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			HashMap<String, Object> map2 = new HashMap<String, Object>();
			for (Field field : dataSet1.getFields()) {
				int col = DataSetUtil.getFieldIndex(dataSet1, field.getName());
				map1.put(field.getName(), dataSet1.getData(row, dataSet1.getShowIndex(col)));
				map2.put(field.getName(), dataSet2.getData(j, dataSet2.getShowIndex(col)));
			}
			if ((Boolean) (((LambdaFunction) pks).execute(context, map1, map2).getResult())) {
				flag = j;
				break;
			}
		}
		return flag;
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

	protected abstract DataSet operate(SimpleDataSet dataArray1, SimpleDataSet dataArray2, Object pks,
			ScriptContext context) throws Exception;

}
