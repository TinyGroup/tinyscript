package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.impl.MatchDataSet;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 通过条件匹配左右数据集
 * 
 * @author yancheng11334
 *
 */
public class DataSetMatchFunction extends AbstractScriptFunction {

	public String getNames() {
		return "match";
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
				DataSet left = (DataSet) getValue(parameters[0]);
				DataSet right = (DataSet) getValue(parameters[1]);
				String expression = getExpression(parameters[2]);
				return executeMatch(left, right, expression, context);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	protected DataSet executeMatch(DataSet left, DataSet right, String expression, ScriptContext context)
			throws ScriptException {
		String oldExpression = expression;
		try {
			// 转换表达式为脚本可以执行的语法片段
			expression = convertExpression(expression);

			// 合并新的字段
			List<Field> newFields = new ArrayList<Field>();
			newFields.addAll(left.getFields());
			newFields.addAll(right.getFields());

			// 匹配的左表、右表对应的行下标
			List<int[]> matchList = new ArrayList<int[]>();

			int leftRows = left.getRows();
			int rightRows = right.getRows();

			for (int i = 0; i < leftRows; i++) {
				ScriptContext subContext = new DefaultScriptContext();
				subContext.setParent(context);

				// 初始化新结果行
				Object[] rowData = new Object[newFields.size()];
				setRowValue(subContext, rowData, (AbstractDataSet) left, i);

				for (int j = 0; j < rightRows; j++) {
					setRowValue(subContext, null, (AbstractDataSet) right, j);
					if (executeDynamicBoolean(expression, subContext)) {
						// 执行match操作
						setRowValue(rowData, (AbstractDataSet) right, left.getColumns(), j);
						int[] match = new int[2];
						match[0] = i; // 左关联
						match[1] = j; // 右关联
						matchList.add(match);
						break;
					}
				}
			}

			return new MatchDataSet(left, right, matchList, getScriptEngine().isIndexFromOne());
		} catch (Exception e) {
			throw new ScriptException(
					ResourceBundleUtil.getResourceMessage("dataset", "dataset.match.error", oldExpression), e);
		}
	}

	private void setRowValue(Object[] rowData, AbstractDataSet dataSet, int shift, int row) throws Exception {
		for (int j = 0; j < dataSet.getColumns(); j++) {
			rowData[j + shift] = dataSet.getData(dataSet.getShowIndex(row), dataSet.getShowIndex(j));
		}
	}

	private void setRowValue(ScriptContext context, Object[] rowData, AbstractDataSet dataSet, int row)
			throws Exception {
		for (int j = 0; j < dataSet.getColumns(); j++) {
			Field field = dataSet.getFields().get(j);
			Object value = dataSet.getData(dataSet.getShowIndex(row), dataSet.getShowIndex(j));
			context.put(field.getName(), value); // 设置行参数上下文
			if (rowData != null) {
				rowData[j] = value;
			}
		}
	}

}
