package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.impl.MatchDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;

/**
 * join函数
 * @author yancheng11334
 *
 */
public class DataSetJoinFunction extends AbstractScriptFunction {

	public String getNames() {
		return "join";
	}
	
	public String getBindingTypes() {
		return DataSet.class.getName();
	}
	
	public boolean  enableExpressionParameter(){
		return true;
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException("join函数的参数为空!");
			}else if(checkParameters(parameters, 3)){
				DataSet left = (DataSet) getValue(parameters[0]);
				DataSet right = (DataSet) getValue(parameters[1]);
				String expression = getExpression(parameters[2]);
				return executeJoin(left,right,expression,context);
			}else{
				throw new ScriptException("join函数的参数格式不正确!");
			}
		}catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException("join函数的参数格式不正确!", e);
		}
	}
	
	
	
	/**
	 * 执行join逻辑
	 * @param left
	 * @param right
	 * @param fields
	 * @param expression
	 * @return
	 * @throws ScriptException
	 */
	protected DataSet executeJoin(DataSet left,DataSet right,String expression,ScriptContext context) throws ScriptException{
		try{
			String newExpression = (String)getScriptEngine().execute(convertExpression(expression), context);
			if(newExpression==null){
			   newExpression = expression;
			}
			//解析关联条件
			String[] fields = getJoinField(newExpression);
			
			int leftColumn = DataSetUtil.getFieldIndex(left, fields[0]);
			if(leftColumn<0){
			   throw new ScriptException(String.format("左数据集查不到字段%s", fields[0]));	
			}
			
			int rightColumn = DataSetUtil.getFieldIndex(right, fields[1]);
			if(rightColumn<0){
			   throw new ScriptException(String.format("右数据集查不到字段%s", fields[1]));	
			}
			
			//记录关联行信息
			List<int[]> joinList = new ArrayList<int[]>(); 
			
			//遍历右数据集
			Map<Object,Integer> values = new HashMap<Object,Integer>();
			AbstractDataSet rightDs = (AbstractDataSet) right;
			int rrow = right.getRows();
			for(int row=0;row<rrow;row++){
				Object v = right.getData(rightDs.getShowIndex(row), rightDs.getShowIndex(rightColumn));
				if(v!=null){
				   values.put(v, row);
				}
			}
			
			
			//遍历左数据集
			int lrow = left.getRows();
			AbstractDataSet leftDs = (AbstractDataSet) left;
			for(int row=0;row<lrow;row++){
				Object v = left.getData(leftDs.getShowIndex(row),leftDs.getShowIndex(leftColumn) );
				if(v!=null){
					Integer rightRow = values.get(v);
					int[] join = new int[2];
					join[0] = row;
					join[1] = rightRow == null? -1 : rightRow;
					joinList.add(join);
				}
			}
		
			return new MatchDataSet(left,right,joinList,getScriptEngine().isIndexFromOne());
		}catch(Exception e){
			throw new ScriptException("执行join逻辑发生异常",e);
		}
		
	}
	
	private String[] getJoinField(String rule) throws Exception{
		String[] ss = rule.split("=");
		try{
			String[] fields = new String[2];
			fields[0] = ss[0].trim();
			fields[1] = ss[1].trim();
			return fields;
		}catch(Exception e){
			throw new ScriptException(String.format("解析join条件:%s发生异常", rule),e);
		}
	}

}
