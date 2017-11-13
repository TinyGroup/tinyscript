package org.tinygroup.tinyscript.dataset.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
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
				AbstractDataSet dataSet1 = (AbstractDataSet) getValue(parameters[0]);
				AbstractDataSet dataSet2 = (AbstractDataSet) getValue(parameters[1]);
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

	private DataSet insert(AbstractDataSet dataSet1, AbstractDataSet dataSet2) throws Exception {
		Object[][] datas = new Object[dataSet1.getRows() + dataSet2.getRows()][dataSet1.getFields().size()];
		for (int i = 0; i < dataSet1.getRows(); i++) {
			for (int j = 0; j < datas[i].length; j++) {
				datas[i][j] = dataSet1.getData(dataSet1.getShowIndex(i), dataSet1.getShowIndex(j));
			}
		}
		for (int i = dataSet1.getRows(); i < dataSet2.getRows() + dataSet1.getRows(); i++) {
			for (int j = 0; j < datas[i].length; j++) {
				int rightFieldIndex = DataSetUtil.getFieldIndex(dataSet2, dataSet1.getFields().get(j).getName());
				if (rightFieldIndex > -1) {
					datas[i][j] = dataSet2.getData(dataSet2.getShowIndex(i - dataSet1.getRows()),
							dataSet2.getShowIndex(rightFieldIndex));
				}
			}
		}
		return DataSetUtil.createDynamicDataSet(dataSet1.getFields(), datas, dataSet1.isIndexFromOne());
	}

}
