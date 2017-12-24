package org.tinygroup.tinyscript.dataset.function;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.tinygroup.commons.tools.CollectionUtil;
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
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;
import org.tinygroup.tinyscript.interpret.ScriptResult;

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
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 3)) {
				AbstractDataSet dataSet = (AbstractDataSet) getValue(parameters[0]);
				String colName = (String) getValue(parameters[1]);
				String expression = getExpression(parameters[2]);

				// 获取更新的列下标
				int col = DataSetUtil.getFieldIndex(dataSet, colName);
				if (col < 0) {
					throw new ScriptException(
							ResourceBundleUtil.getResourceMessage("dataset", "dataset.fields.notfound", colName));
				}

				// 获取包含数组的列下标
				Set<Integer> columns = DataSetUtil.getFieldArray(dataSet, expression);

				// 获取lambda表达式
				LambdaFunction lambdaFunction = getLambdaFunction(parameters[2]);

				if (lambdaFunction != null) {
					return updateByLambda(dataSet, col, columns, lambdaFunction, context);
				} else {
					return updateByExpression(dataSet, col, columns, expression, context);
				}

			} else if (parameters != null && parameters.length == 4 && parameters[0] != null && parameters[1] != null) {
				// 重构结构
				DynamicDataSet dynamicDataSet = (DynamicDataSet) getValue(parameters[0]);
				LambdaFunction lambdaFunction = (LambdaFunction) getValue(parameters[1]);
				List<Field> insertFields = convertFields(getValue(parameters[2]));
				List<Field> deleteFields = convertFields(getValue(parameters[3]));

				return updateField(dynamicDataSet, lambdaFunction, insertFields, deleteFields, context);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	private DynamicDataSet updateField(DynamicDataSet dynamicDataSet, LambdaFunction lambdaFunction,
			List<Field> insertFields, List<Field> deleteFields, ScriptContext context) throws Exception {
		if (!CollectionUtil.isEmpty(insertFields)) {
			// 执行插入字段逻辑
			for (Field field : insertFields) {
				dynamicDataSet.insertColumn(dynamicDataSet.isIndexFromOne() ? dynamicDataSet.getFields().size() + 1
						: dynamicDataSet.getFields().size(), field);
			}
		}

		// 逐行遍历
		for (int i = 0; i < dynamicDataSet.getRows(); i++) {
			ScriptContext subContext = new DefaultScriptContext();
			subContext.setParent(context);

			// 加载行记录信息到上下文环境
			if (lambdaFunction.getParamterNames() != null && lambdaFunction.getParamterNames().length > 0) {
				Object[] readParamters = new Object[lambdaFunction.getParamterNames().length];
				// 只加载用户指定参数
				for (int j = 0; j < lambdaFunction.getParamterNames().length; j++) {
					int col = DataSetUtil.getFieldIndex(dynamicDataSet, lambdaFunction.getParamterNames()[j]);
					readParamters[j] = dynamicDataSet.getData(dynamicDataSet.getShowIndex(i),
							dynamicDataSet.getShowIndex(col));
				}
				// 动态执行lambda表达式
				lambdaFunction.execute(subContext, readParamters);
			} else {
				// 加载全部参数
				for (int j = 0; j < dynamicDataSet.getFields().size(); j++) {
					Field f = dynamicDataSet.getFields().get(j);
					subContext.put(f.getName(),
							dynamicDataSet.getData(dynamicDataSet.getShowIndex(i), dynamicDataSet.getShowIndex(j)));
				}
				// 动态执行lambda表达式
				lambdaFunction.execute(subContext);
			}

			// 根据插入字段更新列
			for (int j = 0; j < insertFields.size(); j++) {
				int col = DataSetUtil.getFieldIndex(dynamicDataSet, insertFields.get(j).getName());
				dynamicDataSet.setData(dynamicDataSet.getShowIndex(i), dynamicDataSet.getShowIndex(col),
						subContext.getItemMap().get(insertFields.get(j).getName()));
			}
		}

		if (!CollectionUtil.isEmpty(deleteFields)) {
			// 执行删除字段逻辑
			for (Field field : deleteFields) {
				dynamicDataSet.deleteColumn(field.getName());
			}
		}
		return dynamicDataSet;
	}

	/**
	 * 转换字段信息
	 * 
	 * @param obj
	 * @return
	 * @throws ScriptException
	 */
	@SuppressWarnings("rawtypes")
	private List<Field> convertFields(Object obj) throws ScriptException {
		List<Field> fields = new ArrayList<Field>();
		if (obj != null) {
			if (obj.getClass().isArray()) {
				int length = Array.getLength(obj);
				for (int i = 0; i < length; i++) {
					addField(fields, Array.get(obj, i));
				}
			} else if (obj instanceof Collection) {
				Collection c = (Collection) obj;
				Iterator it = c.iterator();
				while (it.hasNext()) {
					addField(fields, it.next());
				}
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		}
		return fields;
	}

	private void addField(List<Field> fields, Object o) throws ScriptException {
		if (o instanceof String) {
			String name = (String) o;
			fields.add(new Field(name, name, "Object"));
		} else if (o instanceof Field) {
			Field f = (Field) o;
			fields.add(f);
		} else {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
		}
	}

	private LambdaFunction getLambdaFunction(Object obj) {
		try {
			return (LambdaFunction) getValue(obj);
		} catch (Exception e) {
			// 忽略异常
			return null;
		}
	}

	private DataSet updateByExpression(AbstractDataSet dataSet, int col, Set<Integer> columns, String expression,
			ScriptContext context) throws ScriptException {
		try {
			String runExpression = ScriptContextUtil.convertExpression(expression);
			if (dataSet instanceof GroupDataSet) {
				GroupDataSet groupDataSet = (GroupDataSet) dataSet;
				for (DynamicDataSet subDs : groupDataSet.getGroups()) {
					updateRowWithExpression(subDs, col, runExpression, columns, context);
				}
			} else {
				updateRowWithExpression(dataSet, col, runExpression, columns, context);
			}
			return dataSet;
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	private DataSet updateByLambda(AbstractDataSet dataSet, int col, Set<Integer> columns, LambdaFunction function,
			ScriptContext context) throws ScriptException {
		try {
			if (dataSet instanceof GroupDataSet) {
				GroupDataSet groupDataSet = (GroupDataSet) dataSet;
				for (DynamicDataSet subDs : groupDataSet.getGroups()) {
					updateRowWithLambda(subDs, col, function, columns, context);
				}
			} else {
				updateRowWithLambda(dataSet, col, function, columns, context);
			}
			return dataSet;
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	// 参数为实际下标
	private void updateRowWithExpression(AbstractDataSet dataSet, int col, String expression, Set<Integer> columns,
			ScriptContext context) throws Exception {
		int showCol = dataSet.getShowIndex(col);
		for (int i = 0; i < dataSet.getRows(); i++) {
			int showRow = dataSet.getShowIndex(i);
			ScriptContext subContext = new DefaultScriptContext();
			subContext.setParent(context);
			subContext.put("$currentRow", showRow);
			setRowValue(subContext, dataSet, columns, i);
			ScriptContextUtil.setCurData(subContext, DataSetUtil.createDataSetRow(dataSet, dataSet.getShowIndex(i)));
			try {
				Object value = executeDynamicObject(expression, subContext);
				dataSet.setData(showRow, showCol, value);
			} catch (ScriptException e) {
				// 忽略脚本异常
				continue;
			}

		}
	}

	private void updateRowWithLambda(AbstractDataSet dataSet, int col, LambdaFunction function, Set<Integer> columns,
			ScriptContext context) throws Exception {
		int showCol = dataSet.getShowIndex(col);
		for (int i = 0; i < dataSet.getRows(); i++) {
			int showRow = dataSet.getShowIndex(i);
			ScriptContext subContext = new DefaultScriptContext();
			subContext.setParent(context);
			subContext.put("$currentRow", showRow);
			setRowValue(subContext, dataSet, columns, i);
			ScriptContextUtil.setCurData(subContext, DataSetUtil.createDataSetRow(dataSet, dataSet.getShowIndex(i)));
			try {
				ScriptResult value = function.execute(subContext, showRow);
				dataSet.setData(showRow, showCol, value.getResult());
			} catch (ScriptException e) {
				// 忽略脚本异常
				continue;
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
