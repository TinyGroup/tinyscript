package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DataSetRow;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.ExpressionFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;

/**
 * 序表的过滤函数
 * @author yancheng11334
 *
 */
public class DataSetFilterFunction extends ExpressionFunction {

	public String getNames() {
		return "filter";
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
        		throw new ScriptException(String.format("%s函数的参数为空!", getNames()));
			} else if(checkParameters(parameters, 2)){
				AbstractDataSet dataSet = (AbstractDataSet) getValue(parameters[0]);
				 String  expression = getExpression(parameters[1]);
				 return filter(context,dataSet,expression);
			} else {
				throw new ScriptException(String.format("%s函数的参数格式不正确!", getNames()));
			}
				
        }catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(String.format("%s函数执行发生异常:", getNames()),e);
		}
	}
	
	private DataSet filter(ScriptContext context,AbstractDataSet dataSet,String expression) throws Exception{
		ScriptContext subContext = new DefaultScriptContext();
		subContext.setParent(context);
		expression = checkExpression(expression);
		
		List<DataSetRow> result = new ArrayList<DataSetRow>();
		   int rowNum = dataSet.getRows();
		   for(int i=0;i<rowNum;i++){
			   DataSetRow dataSetRow = DataSetUtil.createDataSetRow(dataSet,dataSet.getShowIndex(i)); 
			   for(int j=0;j<dataSet.getFields().size();j++){
				   Field field = dataSet.getFields().get(j);
				   subContext.put(field.getName(), dataSetRow.getData(dataSet.getShowIndex(j))); 
			   }
			   if(executeDynamicBoolean(expression,subContext)){
				  result.add(dataSetRow);
			   }
		   }
		   DataSet newDataSet = DataSetUtil.createDynamicDataSet(dataSet.getFields(), result);
		   newDataSet.setIndexFromOne(dataSet.isIndexFromOne());
		   return newDataSet;
	}

}