package org.tinygroup.tinyscript.dataset.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.GroupDataSet;
import org.tinygroup.tinyscript.dataset.impl.AggregateResult;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 重命名函数
 * 
 * @author yancheng11334
 *
 */
public class DataSetRenameFunction extends AbstractScriptFunction {

	public String getNames() {
		return "rename";
	}

	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 3)) {
				DataSet dataSet = (DataSet) parameters[0];
				String newName = (String) parameters[2];

				int index = getIndex(dataSet, parameters[1]);

				return rename(dataSet, index, newName);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	private int getIndex(DataSet dataSet, Object obj) throws Exception {
		if (obj instanceof Integer) {
			return (Integer) obj;
		} else if (obj instanceof String) {
			String colName = (String) obj;
			int index = DataSetUtil.getFieldIndex(dataSet, colName);
			if (index < 0) {
				throw new ScriptException(
						ResourceBundleUtil.getResourceMessage("dataset", "dataset.fields.notfound", colName));
			}
			return index;
		} else {
			throw new ScriptException(
					ResourceBundleUtil.getDefaultMessage("function.parameter.unsupport", getNames(), obj.getClass()));
		}
	}

	private DataSet rename(DataSet dataSet, int col, String newName) throws Exception {
		Field oldField = dataSet.getFields().get(col);
		Field newField = new Field();
		newField.setName(newName);
		newField.setTitle(oldField.getTitle());
		newField.setType(oldField.getType());
		dataSet.getFields().set(col, newField);
		if ("aggregate".equals(oldField.getType()) && dataSet instanceof GroupDataSet) {
			for (AggregateResult result : ((GroupDataSet) dataSet).getAggregateResultList()) {
				if (oldField.getName().equals(result.getName())) {
					result.setName(newField.getName());
				}
			}
		}
		return dataSet;
	}

}
