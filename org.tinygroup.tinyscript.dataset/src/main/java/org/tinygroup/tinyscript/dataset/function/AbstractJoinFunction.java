package org.tinygroup.tinyscript.dataset.function;

import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.impl.MatchDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 抽象的join函数基类
 * @author yancheng11334
 *
 */
public abstract class AbstractJoinFunction extends AbstractScriptFunction{

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
				return executeJoin(left, right, expression, context);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}
	
	/**
	 * 执行join逻辑
	 * 
	 * @param left
	 * @param right
	 * @param fields
	 * @param expression
	 * @return
	 * @throws ScriptException
	 */
	protected DataSet executeJoin(DataSet left, DataSet right, String expression, ScriptContext context)
			throws ScriptException {
		try {
			String newExpression = (String) getScriptEngine().execute(convertExpression(expression), context);
			if (newExpression == null) {
				newExpression = expression;
			}
			// 解析关联条件
			String[] fields = getJoinField(newExpression);

			int leftColumn = DataSetUtil.getFieldIndex(left, fields[0]);
			if (leftColumn < 0) {
				throw new ScriptException(
						ResourceBundleUtil.getResourceMessage("dataset", "dataset.fields.leftnotfound", fields[0]));
			}

			int rightColumn = DataSetUtil.getFieldIndex(right, fields[1]);
			if (rightColumn < 0) {
				throw new ScriptException(
						ResourceBundleUtil.getResourceMessage("dataset", "dataset.fields.rightnotfound", fields[1]));
			}

			// 记录关联行信息
			List<int[]> joinList = joinTwoDataSet((AbstractDataSet)left,(AbstractDataSet)right,leftColumn,rightColumn);

			return new MatchDataSet(left, right, joinList, getScriptEngine().isIndexFromOne());
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}

	}
	
	/**
	 * 获取两表的关联
	 * @param leftDs
	 * @param rightDs
	 * @param leftColumn
	 * @param rightColumn
	 * @return
	 * @throws Exception
	 */
	protected abstract List<int[]> joinTwoDataSet(AbstractDataSet leftDs,AbstractDataSet rightDs,int leftColumn,int rightColumn) throws Exception;
	
	private String[] getJoinField(String rule) throws Exception {
		String[] ss = rule.split("=");
		try {
			String[] fields = new String[2];
			fields[0] = ss[0].trim();
			fields[1] = ss[1].trim();
			return fields;
		} catch (Exception e) {
			throw new ScriptException(
					ResourceBundleUtil.getResourceMessage("dataset", "dataset.join.condition.error", rule), e);
		}
	}

}
