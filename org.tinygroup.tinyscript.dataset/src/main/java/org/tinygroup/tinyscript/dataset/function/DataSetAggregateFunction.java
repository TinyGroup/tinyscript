package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.function.DynamicNameScriptFunction;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;

/**
 * 数据集聚合函数
 * @author yancheng11334
 *
 */
public class DataSetAggregateFunction extends DynamicNameScriptFunction{

	public String getBindingTypes() {
		return DataSet.class.getName();
	}
	
	public boolean  enableExpressionParameter(){
		return true;
	}
	
	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		String functionName = ScriptContextUtil.getDynamicFunctionName(context);
		try{
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(String.format("%s聚合函数的参数为空!", functionName));
			}else if(checkParameters(parameters, 2)){
				AbstractDataSet dataSet = (AbstractDataSet) getValue(parameters[0]);
				String fieldName = (String) getValue(parameters[1]);
				return executeDataSet(dataSet,fieldName,functionName);
			}else{
				throw new ScriptException(String.format("%s聚合函数的参数格式不正确!", functionName));
			}
		}catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(String.format("%s聚合函数的参数格式不正确!", functionName),e);
		}
	}

	public boolean exsitFunctionName(String name) {
		return ExpressionUtil.getNumberCalculator(name)!=null;
	}
	
	protected Object executeDataSet(AbstractDataSet dataSet,String fieldName,String functionName) throws Exception{
		int col = getColumn(dataSet,fieldName);
		int rowNum = dataSet.getRows();
		List<Object> parameterList = new ArrayList<Object>();
		for(int i=0;i<rowNum;i++){
			Object v = dataSet.getData(dataSet.getShowIndex(i), dataSet.getShowIndex(col));
			parameterList.add(v);
		}
		return ExpressionUtil.compute(functionName, parameterList);
	}
	
	protected int getColumn(DataSet dataSet,Object obj) throws Exception{
		if(obj instanceof Integer){
			Integer index = (Integer) obj;
			return index;
		}else if(obj instanceof String){
			 String name = (String) obj;
			 int index = DataSetUtil.getFieldIndex(dataSet, name);
			 return index;
		}
		return -1;
	}

}
