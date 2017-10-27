package org.tinygroup.tinyscript.dataset.function;

import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.impl.MatchDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
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
	
	@SuppressWarnings("unchecked")
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 3)) {
				AbstractDataSet left = (AbstractDataSet) getValue(parameters[0]);
				AbstractDataSet right = (AbstractDataSet) getValue(parameters[1]);
				String expression = getExpression(parameters[2]);
				return executeJoin(left, right, expression, context);
			} else if (checkParameters(parameters, 4)) {
				AbstractDataSet left = (AbstractDataSet) getValue(parameters[0]);
				AbstractDataSet right = (AbstractDataSet) getValue(parameters[1]);
				Object v1 = getValue(parameters[2]);
				Object v2 = getValue(parameters[3]);
				if(v1!=null && v2!=null){
				   if(v1 instanceof List && v2 instanceof List){
					  List<String> l1 = (List<String>) v1;
					  List<String> l2 = (List<String>) v2;
					  String[] s1 = new String[l1.size()];
					  String[] s2 = new String[l2.size()];
					  return executeJoin(left, right, l1.toArray(s1), l2.toArray(s2));
				   }else if(v1 instanceof LambdaFunction && v2 instanceof LambdaFunction){
					  return executeJoin(left, right,(LambdaFunction)v1,(LambdaFunction)v2,context);
				   }
				}
			} 
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
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
	protected DataSet executeJoin(AbstractDataSet left, AbstractDataSet right, String expression, ScriptContext context)
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

			int[] leftColumns = new int[1];
			int[] rightColumns = new int[1];
			leftColumns[0] = left.getShowIndex(leftColumn);
			rightColumns[0] = right.getShowIndex(rightColumn);
			
			// 记录关联行信息
			List<int[]> joinList = joinTwoDataSet(left,right,leftColumns,rightColumns);

			return new MatchDataSet(left, right, joinList, getScriptEngine().isIndexFromOne());
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}

	}
	
	protected DataSet executeJoin(AbstractDataSet left, AbstractDataSet right, String[] leftColNames,String[] rightColNames)
			throws ScriptException {
		try {
			int[] leftColumns = getShowIndexs(left,DataSetUtil.getFieldIndex(left.getFields(), leftColNames));
			int[] rightColumns = getShowIndexs(right,DataSetUtil.getFieldIndex(right.getFields(), rightColNames));
			
			// 记录关联行信息
			List<int[]> joinList = joinTwoDataSet(left,right,leftColumns,rightColumns);
			
			return new MatchDataSet(left, right, joinList, getScriptEngine().isIndexFromOne());
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}
	
	protected DataSet executeJoin(AbstractDataSet left, AbstractDataSet right, LambdaFunction leftFunction,LambdaFunction rightFunction,ScriptContext context)
			throws ScriptException {
		try {
			// 记录关联行信息
			List<int[]> joinList = joinTwoDataSet(left,right,leftFunction,rightFunction ,context);
			
			return new MatchDataSet(left, right, joinList, getScriptEngine().isIndexFromOne());
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}
	
	protected int[] getShowIndexs(AbstractDataSet dataSet,int[] cols){
		int[] newcols = new int[cols.length];
		for(int i=0;i<cols.length;i++){
			newcols[i] = dataSet.getShowIndex(cols[i]);
		}
		return newcols;
	}
	
	protected ScriptContext createRowContext(AbstractDataSet dataSet,ScriptContext context,int row) throws Exception {
		for(int i=0;i<dataSet.getFields().size();i++){
			Field f = dataSet.getFields().get(i);
			context.put(f.getName(), dataSet.getData(row, dataSet.getShowIndex(i)));
		}
		return context;
	}
	
	/**
	 * 获取两表的关联
	 * @param leftDs
	 * @param rightDs
	 * @param leftColumns
	 * @param rightColumns
	 * @return
	 * @throws Exception
	 */
	protected abstract List<int[]> joinTwoDataSet(AbstractDataSet leftDs,AbstractDataSet rightDs,int[] leftColumns,int[] rightColumns) throws Exception;
	
	/**
	 * 获取两表的关联
	 * @param leftDs
	 * @param rightDs
	 * @param leftFunction
	 * @param rightFunction
	 * @param context
	 * @return
	 * @throws Exception
	 */
	protected abstract List<int[]> joinTwoDataSet(AbstractDataSet leftDs,AbstractDataSet rightDs,LambdaFunction leftFunction,LambdaFunction rightFunction,ScriptContext context) throws Exception;
	
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
