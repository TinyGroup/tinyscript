package org.tinygroup.tinyscript.dataset.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DataSetRow;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;

public class DataSetRemoveFunction extends AbstractScriptFunction {

	public String getNames() {
		return "remove";
	}

	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
        	if (parameters == null || parameters.length == 0) {
        		throw new ScriptException(String.format("%s函数的参数为空!", getNames()));
			} else if(checkParameters(parameters, 2)){
				 DataSet dataSet = (DataSet) getValue(parameters[0]);
				 LambdaFunction function = (LambdaFunction) parameters[1];
				 return remove(context,dataSet,function);
			} else {
				throw new ScriptException(String.format("%s函数的参数格式不正确!", getNames()));
			}
				
        }catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(String.format("%s函数执行发生异常:", getNames()),e);
		}
	}
	
	private DataSet remove(ScriptContext context,DataSet dataSet,LambdaFunction function) throws Exception{
		ScriptContext subContext = new DefaultScriptContext();
		subContext.setParent(context);
		
		if(dataSet instanceof DynamicDataSet==false){
			  throw new ScriptException("本数据集不是动态数据集,无法过滤记录!");
		   }
		   DynamicDataSet dynamicDataSet = (DynamicDataSet) dataSet;
		   int rowNum = dataSet.getRows();
		   for(int i=0;i<rowNum;i++){
			   //倒序删除
			   int p = rowNum-1-i;
			   DataSetRow dataSetRow = DataSetUtil.createDataSetRow(dataSet,dynamicDataSet.getShowIndex(p)); 
			   for(int j=0;j<dataSet.getFields().size();j++){
				   Field field = dataSet.getFields().get(j);
				   subContext.put(field.getName(), dataSetRow.getData(dynamicDataSet.getShowIndex(j))); 
			   }
			   if(!(Boolean)function.execute(subContext).getResult()){
				   dynamicDataSet.deleteRow(dynamicDataSet.getShowIndex(p));
			   }
		   }
		   return dynamicDataSet;
	}
}
