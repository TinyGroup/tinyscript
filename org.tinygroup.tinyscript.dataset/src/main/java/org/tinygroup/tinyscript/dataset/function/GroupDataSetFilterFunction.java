package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.GroupDataSet;
import org.tinygroup.tinyscript.dataset.impl.AggregateResult;
import org.tinygroup.tinyscript.dataset.impl.DefaultGroupDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
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
				throw new ScriptException("filterGroup函数的参数为空!");
			} else if (checkParameters(parameters, 2)) {
				GroupDataSet groupDataSet = (GroupDataSet) getValue(parameters[0]);
				LambdaFunction expression = (LambdaFunction) (((FunctionCallExpressionParameter) parameters[1]).eval());
				return filterGroup(context, groupDataSet, expression);
			} else {
				throw new ScriptException("filterGroup函数的参数格式不正确");
			}

		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException("filterGroup函数的参数格式不正确!", e);
		}
	}

	private Object filterGroup(ScriptContext context, GroupDataSet groupDataSet, LambdaFunction expression)
			throws Exception {
		List<DynamicDataSet> newList = new ArrayList<DynamicDataSet>();

		Set<Integer> columns = new HashSet<Integer>();
		for (int j = 0; j < groupDataSet.getFields().size(); j++) {
			columns.add(j + 1);
		}

		for (int dsNum = 0; dsNum < groupDataSet.getGroups().size(); dsNum++) {
			List<Integer> matchRows = new ArrayList<Integer>();
			DynamicDataSet subDs = groupDataSet.getGroups().get(dsNum);
			for (int i = 0; i < subDs.getRows(); i++) {
				ScriptContext subContext = new DefaultScriptContext();
				subContext.setParent(context);
				subContext.put("$currentRow", groupDataSet.getShowIndex(i));
				setRowValue(subContext, subDs, columns, i);
				setAggregateValue(subContext, subDs, groupDataSet.getAggregateResultList(), dsNum);
				if ((Boolean) expression.execute(subContext).getResult()) {
					matchRows.add(i);
				}
			}
			if (!matchRows.isEmpty()) {
				newList.add(DataSetUtil.createDynamicDataSet(subDs, matchRows));
			}
		}
		return new DefaultGroupDataSet(groupDataSet.getFields(), newList, groupDataSet.getAggregateResultList(),
				groupDataSet.isIndexFromOne());
	}

	private void setAggregateValue(ScriptContext context, AbstractDataSet dataSet,
			List<AggregateResult> aggregateResultList, int row) throws Exception {
		for (AggregateResult result : aggregateResultList) {
			context.put(result.getName(), result.getData(row));
		}
	}

	private void setRowValue(ScriptContext context, AbstractDataSet dataSet, Set<Integer> columns, int row)
			throws Exception {
		for (int j = 0; j < dataSet.getColumns(); j++) {
			Field field = dataSet.getFields().get(j);
			if (columns != null && columns.contains(j)) {
				// 存在字段数组形式
				context.put(field.getName(), DataSetUtil.createDataSetColumn(dataSet, dataSet.getShowIndex(j)));
			} else {
				// 直接赋值
				context.put(field.getName(), dataSet.getData(dataSet.getShowIndex(row), dataSet.getShowIndex(j))); // 设置行参数上下文
			}

		}
	}

}
