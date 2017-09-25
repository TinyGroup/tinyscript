package org.tinygroup.tinyscript.dataset.function;

import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DataSetRow;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

public class DataSetSubtractFunction extends AbstractDataSetOperateFunction {
	@Override
	public String getNames() {
		return "subtract";
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
			} else if (checkParameters(parameters, 3)) {

				AbstractDataSet dataSet1 = (AbstractDataSet) parameters[0];
				AbstractDataSet dataSet2 = (AbstractDataSet) parameters[1];

				if (!checkField(dataSet1, dataSet2)) {
					throw new ScriptException(
							ResourceBundleUtil.getResourceMessage("dataset", "dataset.fields.inconsistent"));
				}

				return operate(dataSet1, dataSet2, parameters[2], context);

			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	protected DataSet operate(AbstractDataSet dataSet1, AbstractDataSet dataSet2, Object pks, ScriptContext context)
			throws Exception {
		Map<String, DataSetRow> map = createMapDataSetRows(dataSet1, pks, context);
		for (int i = 1; i <= dataSet2.getRows(); i++) {
			String key = createRowKey(dataSet2, pks, i, context);
			if (map.containsKey(key)) {
				map.remove(key);
			}
		}
		return DataSetUtil.createDynamicDataSet(map);
	}

}
