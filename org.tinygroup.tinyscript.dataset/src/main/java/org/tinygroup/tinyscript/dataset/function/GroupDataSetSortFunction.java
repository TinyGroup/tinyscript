package org.tinygroup.tinyscript.dataset.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.GroupDataSet;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 分组排序
 * 
 * @author yancheng11334
 *
 */
public class GroupDataSetSortFunction extends DataSetSortFunction {

	public String getNames() {
		return "sortGroup";
	}

	public String getBindingTypes() {
		return GroupDataSet.class.getName();
	}

	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		if (parameters == null || parameters.length == 0) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
		} else if (checkParameters(parameters, 2)) {
			return groupSort(segment, context, (GroupDataSet) parameters[0], (String) parameters[1]);
		} else {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
		}
	}

	protected Object groupSort(ScriptSegment segment, ScriptContext context, GroupDataSet groupDataSet, String rule)
			throws ScriptException {
		try {
			for (DataSet dataSet : groupDataSet.getGroups()) {
				sort(segment, context, dataSet, rule);
			}
			return groupDataSet;
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getResourceMessage("dataset", "dataset.sort.error", rule), e);
		}
	}

}
