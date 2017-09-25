package org.tinygroup.tinyscript.dataset.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 提供字段名和下标相互转换
 * 
 * @author yancheng11334
 *
 */
public class DataSetFieldFunction extends AbstractScriptFunction {

	public String getNames() {
		return "field";
	}

	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 2) && parameters[1] instanceof Integer) {
				DataSet dataSet = (DataSet) parameters[0];
				return getFieldName(dataSet, (Integer) parameters[1]);
			} else if (checkParameters(parameters, 2) && parameters[1] instanceof String) {
				DataSet dataSet = (DataSet) parameters[0];
				return getFieldIndex(dataSet, (String) parameters[1]);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	private String getFieldName(DataSet dataSet, int col) throws Exception {
		if (getScriptEngine().isIndexFromOne()) {
			return dataSet.getFields().get(col - 1).getName();
		} else {
			return dataSet.getFields().get(col).getName();
		}

	}

	private int getFieldIndex(DataSet dataSet, String name) throws Exception {
		return DataSetUtil.getFieldIndex(dataSet, name);
	}

}
