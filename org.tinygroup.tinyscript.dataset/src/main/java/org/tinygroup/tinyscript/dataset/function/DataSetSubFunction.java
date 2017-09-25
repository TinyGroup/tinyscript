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

/**
 * 拆分数据集
 * 
 * @author yancheng11334
 *
 */
public class DataSetSubFunction extends AbstractScriptFunction {

	public String getNames() {
		return "sub";
	}

	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 2) && parameters[1] instanceof Integer) {
				AbstractDataSet dataSet = (AbstractDataSet) parameters[0];
				int beginIndex = (Integer) parameters[1];
				int endIndex = dataSet.getRows() - 1;
				if (getScriptEngine().isIndexFromOne()) {
					return sub(dataSet, beginIndex - 1, endIndex);
				} else {
					return sub(dataSet, beginIndex, endIndex);
				}

			} else if (checkParameters(parameters, 3) && parameters[1] instanceof Integer
					&& parameters[2] instanceof Integer) {
				AbstractDataSet dataSet = (AbstractDataSet) parameters[0];
				int beginIndex = (Integer) parameters[1];
				int endIndex = (Integer) parameters[2];
				if (getScriptEngine().isIndexFromOne()) {
					return sub(dataSet, beginIndex - 1, endIndex - 1);
				} else {
					return sub(dataSet, beginIndex, endIndex);
				}

			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	// 本参数是实际下标
	private DataSet sub(AbstractDataSet dataSet, int beginIndex, int endIndex) throws Exception {
		if (beginIndex < 0 || endIndex > dataSet.getRows() - 1 || beginIndex > endIndex) {
			throw new ScriptException(ResourceBundleUtil.getResourceMessage("dataset", "dataset.row2.outofindex",
					getNames(), beginIndex, endIndex));
		}
		List<Field> newFields = new ArrayList<Field>();
		newFields.addAll(dataSet.getFields());
		Object[][] dataArray = new Object[endIndex - beginIndex + 1][];
		for (int i = beginIndex; i <= endIndex; i++) {
			dataArray[i - beginIndex] = new Object[newFields.size()];
			for (int j = 0; j < newFields.size(); j++) {
				dataArray[i - beginIndex][j] = dataSet.getData(dataSet.getShowIndex(i), dataSet.getShowIndex(j));
			}
		}
		return DataSetUtil.createDynamicDataSet(newFields, dataArray, dataSet.isIndexFromOne());
	}

}
