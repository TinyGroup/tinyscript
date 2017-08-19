package org.tinygroup.tinyscript.dataset.function;

import java.util.Set;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.GroupDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.call.FunctionCallExpressionParameter;

public class DataSetUpdateFunction extends AbstractScriptFunction {

	public String getNames() {
		return "update";
	}

	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	public boolean enableExpressionParameter() {
		return true;
	}

	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException("update函数的参数为空!");
			} else if (checkParameters(parameters, 3)) {
				AbstractDataSet dataSet = (AbstractDataSet) getValue(parameters[0]);
				String colName = (String) getValue(parameters[1]);

				return updateExpression(dataSet, colName, parameters[2], context);
			} else {
				throw new ScriptException("update函数的参数格式不正确!");
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException("update函数的参数格式不正确!", e);
		}
	}

	private DataSet updateExpression(AbstractDataSet dataSet, String colName, Object parameter, ScriptContext context)
			throws ScriptException {
		String expression = null;

		try {
			expression = getExpression(parameter);
			int col = DataSetUtil.getFieldIndex(dataSet, colName);
			if (col < 0) {
				throw new ScriptException(String.format("根据字段%s没有在数据集找到匹配列", colName));
			}

			Set<Integer> columns = null;

			// 获取需要数组处理的字段
			columns = DataSetUtil.getFieldArray(dataSet, expression);

			// 转换表达式为脚本可以执行的语法片段
			expression = ScriptContextUtil.convertExpression(expression);

			Object function = null;
			if (expression.indexOf("->")>0){
			    function = ((FunctionCallExpressionParameter) (parameter)).eval();
			}
			else{
				function = expression;
			}
				

			if (dataSet instanceof GroupDataSet) {
				GroupDataSet groupDataSet = (GroupDataSet) dataSet;
				for (DynamicDataSet subDs : groupDataSet.getGroups()) {
					updateRowWithExpression(subDs, col, function, columns, context);
				}
			} else {
				updateRowWithExpression(dataSet, col, function, columns, context);
			}

			return dataSet;

		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException("执行update函数发生异常", e);
		}
	}

	// 参数为实际下标
	private void updateRowWithExpression(AbstractDataSet dataSet, int col, Object expression, Set<Integer> columns,
			ScriptContext context) throws Exception {
		for (int i = 0; i < dataSet.getRows(); i++) {
			ScriptContext subContext = new DefaultScriptContext();
			subContext.setParent(context);
			subContext.put("$currentRow", dataSet.getShowIndex(i));
			setRowValue(subContext, dataSet, columns, i);
			Object value;
			try {
				if (expression instanceof LambdaFunction) {
					value = ((LambdaFunction) expression).execute(subContext, i + 1);
					dataSet.setData(dataSet.getShowIndex(i), dataSet.getShowIndex(col),
							((ScriptResult) value).getResult());
				} else {
					value = executeDynamicObject((String) expression, subContext);
					dataSet.setData(dataSet.getShowIndex(i), dataSet.getShowIndex(col), value);
				}

			} catch (ScriptException e) {
				// 忽略脚本异常
			}

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
