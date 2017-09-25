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
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 序表的过滤函数
 * @author yancheng11334
 *
 */
public class DataSetFilterFunction extends AbstractScriptFunction {

	public String getNames() {
		return "filter";
	}

	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
        	if (parameters == null || parameters.length == 0) {
        		throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if(checkParameters(parameters, 2)){
				AbstractDataSet dataSet = (AbstractDataSet) getValue(parameters[0]);
				 LambdaFunction function = (LambdaFunction) parameters[1];
				 return filter(context,dataSet,function);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
				
        }catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}
	
	private DataSet filter(ScriptContext context,AbstractDataSet dataSet,LambdaFunction function) throws Exception{
		ScriptContext subContext = new DefaultScriptContext();
		subContext.setParent(context);
		
		List<DataSetRow> result = new ArrayList<DataSetRow>();
		   int rowNum = dataSet.getRows();
		   for(int i=0;i<rowNum;i++){
			   DataSetRow dataSetRow = DataSetUtil.createDataSetRow(dataSet,dataSet.getShowIndex(i)); 
			   for(int j=0;j<dataSet.getFields().size();j++){
				   Field field = dataSet.getFields().get(j);
				   subContext.put(field.getName(), dataSetRow.getData(dataSet.getShowIndex(j))); 
			   }
			   if((Boolean)function.execute(subContext).getResult()){
				  result.add(dataSetRow);
			   }
		   }
		   DataSet newDataSet = DataSetUtil.createDynamicDataSet(dataSet.getFields(), result);
		   newDataSet.setIndexFromOne(dataSet.isIndexFromOne());
		   return newDataSet;
	}

}
