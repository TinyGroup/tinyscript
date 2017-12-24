package org.tinygroup.tinyscript.dataset.function;

import java.util.HashSet;
import java.util.Set;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DataSetRow;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;

public class DataSetFilterOneFunction extends AbstractScriptFunction {

	@Override
	public String getNames() {
		return "filterOne";
	}

	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	@Override
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 2)) {
				AbstractDataSet dataSet = (AbstractDataSet) getValue(parameters[0]);
				LambdaFunction expression = (LambdaFunction) parameters[1];
				return filterOne(dataSet, expression, context);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}

	}

	private DataSetRow filterOne(AbstractDataSet dataSet, LambdaFunction filterFunction, ScriptContext context)
			throws Exception {
		Set<Integer> columns = new HashSet<Integer>();
		for (int j = 0; j < dataSet.getFields().size(); j++) {
			columns.add(j);
		}

		for (int i = 0; i < dataSet.getRows(); i++) {
			DataSetRow dataSetRow = DataSetUtil.createDataSetRow(dataSet, dataSet.getShowIndex(i));
			ScriptContext subContext = new DefaultScriptContext();
			subContext.setParent(context);
			subContext.put("$currentRow", dataSet.getShowIndex(i));
			ScriptContextUtil.setCurData(subContext, dataSetRow);
			DataSetUtil.setRowValue(subContext, dataSet, columns, i);
			if ((Boolean) filterFunction.execute(subContext).getResult()) {
				return dataSetRow;
			}
		}
		return null;
	}

}
