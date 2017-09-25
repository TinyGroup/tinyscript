package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

public class DataSetCopyFunction extends AbstractScriptFunction {

	public String getNames() {
		return "copy";
	}

	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 1)) {
				DataSet dataSet = (DataSet) parameters[0];
				return copy(dataSet);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}

	}

	private DataSet copy(DataSet dataSet) throws Exception {
		if (dataSet instanceof AbstractDataSet) {
			AbstractDataSet abstractDataSet = (AbstractDataSet) dataSet;
			try {
				return abstractDataSet.cloneDataSet();
			} catch (CloneNotSupportedException e) {
				// 某些实现不支持cloneDataSet方法
			}
		}
		// 走循环遍历复制
		List<Field> newFields = new ArrayList<Field>();
		for (Field field : dataSet.getFields()) {
			newFields.add(field);
		}
		Object[][] dataArray = new Object[dataSet.getRows()][];
		for (int i = 0; i < dataSet.getRows(); i++) {
			dataArray[i] = new Object[newFields.size()];
			for (int j = 0; j < newFields.size(); j++) {
				if (dataSet.isIndexFromOne()) {
					dataArray[i][j] = dataSet.getData(i + 1, j + 1);
				} else {
					dataArray[i][j] = dataSet.getData(i, j);
				}
			}
		}
		return DataSetUtil.createDynamicDataSet(newFields, dataArray, dataSet.isIndexFromOne());
	}

}
