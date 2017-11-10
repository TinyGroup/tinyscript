package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.GroupDataSet;
import org.tinygroup.tinyscript.dataset.impl.MultiLevelGroupDataSet;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

public class DataSetLimitFunction extends AbstractScriptFunction {

	@Override
	public String getNames() {
		return "limit";
	}

	@Override
	public String getBindingTypes() {
		return GroupDataSet.class.getName();
	}

	@Override
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 3)) {
				GroupDataSet dataSet = (GroupDataSet) parameters[0];
				int begin = (Integer) parameters[1];
				int end = (Integer) parameters[2];
				return limit(dataSet, begin - 1, end - 1);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	public GroupDataSet limit(GroupDataSet dataSet, int begin, int end) throws CloneNotSupportedException {
		List<DynamicDataSet> newSubDataSetList = new ArrayList<DynamicDataSet>();
		for (int i = begin; i <= end; i++) {
			DynamicDataSet subDataSet = dataSet.getGroups().get(i);
			if(subDataSet instanceof MultiLevelGroupDataSet){
				MultiLevelGroupDataSet multiLevelGroupDataSet = (MultiLevelGroupDataSet) subDataSet;
				newSubDataSetList.add(multiLevelGroupDataSet.getSource());
			}else{
				newSubDataSetList.add((DynamicDataSet)subDataSet.cloneDataSet());
			}
		}
		DynamicDataSet source = null;
		if(dataSet instanceof MultiLevelGroupDataSet){
		   MultiLevelGroupDataSet multiLevelGroupDataSet = (MultiLevelGroupDataSet) dataSet;
		   source = multiLevelGroupDataSet.getSource();
		}else{
		   source = dataSet;
		}
		return new MultiLevelGroupDataSet(source,newSubDataSetList);
	}

}
