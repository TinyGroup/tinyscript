package org.tinygroup.tinyscript.dataset.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

public class DataSetInsertFunction extends AbstractScriptFunction {

	@Override
	public String getNames() {
		return "insert";
	}

	@Override
	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	@Override
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 2)) {
				DynamicDataSet dataSet1 = (DynamicDataSet) getValue(parameters[0]);
				DynamicDataSet dataSet2 = (DynamicDataSet) getValue(parameters[1]);
				return insert(dataSet1, dataSet2);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}

		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	private DataSet insert(DynamicDataSet dataSet1, DynamicDataSet dataSet2) throws Exception {
		for (int i = 0; i < dataSet2.getRows(); i++) {
			dataSet1.insertRow(dataSet1.getShowIndex(dataSet1.getRows()));
			for (int j = 0; j < dataSet1.getFields().size(); j++) {
				int fieldIndex = DataSetUtil.getFieldIndex(dataSet2, dataSet1.getFields().get(j).getName());
				if (fieldIndex > -1) {
					Object data = dataSet2.getData(dataSet2.getShowIndex(i), dataSet2.getShowIndex(fieldIndex));
					dataSet1.setData(dataSet1.getShowIndex(dataSet1.getRows() - 1), dataSet1.getShowIndex(j), data);
				}
			}
		}
		return dataSet1;
	}

}
