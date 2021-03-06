package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.GroupDataSet;
import org.tinygroup.tinyscript.dataset.impl.AggregateResult;
import org.tinygroup.tinyscript.dataset.impl.MultiLevelGroupDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;
import org.tinygroup.tinyscript.interpret.call.FunctionCallExpressionParameter;

public class GroupDataSetFilterFunction extends AbstractScriptFunction {

	public String getNames() {
		return "filterGroup";
	}

	public String getBindingTypes() {
		return GroupDataSet.class.getName();
	}

	public boolean enableExpressionParameter() {
		return true;
	}

	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 2)) {
				GroupDataSet groupDataSet = (GroupDataSet) getValue(parameters[0]);
				LambdaFunction expression = (LambdaFunction) (((FunctionCallExpressionParameter) parameters[1]).eval());
				return filterGroup(context, groupDataSet, expression);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}

		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	private Object filterGroup(ScriptContext context, GroupDataSet groupDataSet, LambdaFunction expression)
			throws Exception {
		List<DynamicDataSet> newList = new ArrayList<DynamicDataSet>();

		Set<Integer> columns = new HashSet<Integer>();
		for (int j = 0; j < groupDataSet.getFields().size(); j++) {
			columns.add(j);
		}

		for (int dsNum = 0; dsNum < groupDataSet.getGroups().size(); dsNum++) {
			List<Integer> matchRows = new ArrayList<Integer>();
			DynamicDataSet subDs = groupDataSet.getGroups().get(dsNum);
			DynamicDataSet source = getSource(subDs);
			for (int i = 0; i < subDs.getRows(); i++) {
				ScriptContext subContext = new DefaultScriptContext();
				subContext.setParent(context);
				subContext.put("$currentRow", groupDataSet.getShowIndex(i));
				DataSetUtil.setRowValue(subContext, subDs, columns, i);
				setAggregateValue(subContext,groupDataSet,dsNum);
				ScriptContextUtil.setCurData(subContext, DataSetUtil.createDataSetRow(groupDataSet, groupDataSet.getShowIndex(i)));
				if ((Boolean) expression.execute(subContext).getResult()) {
					matchRows.add(i);
				}
			}
			if (!matchRows.isEmpty()) {
				newList.add(DataSetUtil.createDynamicDataSet(source, matchRows));
			}
		}
		DynamicDataSet source = getSource(groupDataSet);	
		return new MultiLevelGroupDataSet(source,newList);
	}
	
	private DynamicDataSet getSource(DynamicDataSet groupDataSet){
		if(groupDataSet instanceof MultiLevelGroupDataSet){
		   MultiLevelGroupDataSet multiLevelGroupDataSet = (MultiLevelGroupDataSet) groupDataSet;
		   return multiLevelGroupDataSet.getSource();
		}else{
		   return groupDataSet;
		}
	}
	
	private void setAggregateValue(ScriptContext context, GroupDataSet groupDataSet, int row) throws Exception{
		List<AggregateResult> aggregateResultList = groupDataSet.getAggregateResultList();
		for (AggregateResult result : aggregateResultList) {
			context.put(result.getName(), result.getData(row));
		}
	}
			

}
