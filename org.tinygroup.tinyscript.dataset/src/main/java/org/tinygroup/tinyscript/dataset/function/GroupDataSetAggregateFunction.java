package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.GroupDataSet;
import org.tinygroup.tinyscript.dataset.impl.AggregateResult;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.function.DynamicNameScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;

/**
 * 数据集分组聚合函数
 * 
 * @author yancheng11334
 *
 */
public class GroupDataSetAggregateFunction extends DynamicNameScriptFunction {

	public String getBindingTypes() {
		return GroupDataSet.class.getName();
	}

	public boolean enableExpressionParameter() {
		return true;
	}

	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		String functionName = ScriptContextUtil.getDynamicFunctionName(context);
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(
						ResourceBundleUtil.getDefaultMessage("function.parameter.empty", functionName));
			} else if (checkParameters(parameters, 2)) {
				GroupDataSet dataSet = (GroupDataSet) getValue(parameters[0]);
				String fieldName = (String) getValue(parameters[1]);
				return executeGroupDataSet(dataSet, fieldName, functionName);

			} else {
				throw new ScriptException(
						ResourceBundleUtil.getDefaultMessage("function.parameter.error", functionName));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", functionName), e);
		}
	}

	protected Object executeGroupDataSet(GroupDataSet groupDataSet, String fieldName, String functionName)
			throws Exception {
		String aggregateName = createAggregateName(functionName, fieldName);
		groupDataSet.createAggregateResult(aggregateName);
		int col = getColumn(groupDataSet, fieldName);
		for (int i = 0; i < groupDataSet.getRows(); i++) {
			DynamicDataSet subDataSet = groupDataSet.getGroups().get(i);
			Object value = executeAggregate(subDataSet, col, functionName);
			groupDataSet.setData(groupDataSet.getShowIndex(i), aggregateName, value);
		}
		return groupDataSet;
	}

	protected Object executeAggregate(AbstractDataSet dataSet, int col, String functionName) throws Exception {
		int rowNum = dataSet.getRows();
		List<Object> parameterList = new ArrayList<Object>();
		for (int i = 0; i < rowNum; i++) {
			Object v = dataSet.getData(dataSet.getShowIndex(i), dataSet.getShowIndex(col));
			parameterList.add(v);
		}
		return ExpressionUtil.compute(getCalculatorName(functionName), parameterList);
	}

	protected int getColumn(DataSet dataSet, Object obj) throws Exception {
		if (obj instanceof Integer) {
			Integer index = (Integer) obj;
			return index;
		} else if (obj instanceof String) {
			String name = (String) obj;
			int index = DataSetUtil.getFieldIndex(dataSet, name);
			if (index >= 0) {
				return index;
			} else {
				if (dataSet instanceof GroupDataSet) {
					GroupDataSet groupDataSet = (GroupDataSet) dataSet;
					List<AggregateResult> result = groupDataSet.getAggregateResultList();
					for (int i = 0; i < result.size(); i++) {
						if (name.equals(result.get(i).getName())) {
							return groupDataSet.getFields().size() + i;
						}
					}
				}
			}
		}
		return -1;
	}

	protected String createAggregateName(String functionName, Object obj) {
		StringBuilder sb = new StringBuilder();
		sb.append(functionName).append("_");
		if (obj instanceof Integer) {
			Integer index = (Integer) obj;
			sb.append(index.intValue());
		} else if (obj instanceof String) {
			String name = (String) obj;
			sb.append(name);
		}
		return sb.toString();
	}

	public boolean exsitFunctionName(String name) {
		return ExpressionUtil.getNumberCalculator(getCalculatorName(name)) != null;
	}

	protected String getCalculatorName(String name) {
		if (name.endsWith("Group")) {
			return name.substring(0, name.length() - 5);
		}
		return null;
	}

	public List<String> getFunctionNames() {
		List<String> names = new ArrayList<String>();
		List<String> list = ExpressionUtil.getCalculatorNames();
		for(String name:list){
			names.add(name+"Group");
		}
		return names;
	}

}
