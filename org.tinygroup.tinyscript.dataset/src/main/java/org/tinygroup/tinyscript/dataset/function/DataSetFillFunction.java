package org.tinygroup.tinyscript.dataset.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DataSetColumn;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;

/**
 * 序表相关的填充函数
 * @author yancheng11334
 *
 */
public class DataSetFillFunction extends AbstractScriptFunction {

	public String getNames() {
		return "fill";
	}
	
	public String getBindingTypes() {
		return DataSet.class.getName()+","+DataSetColumn.class.getName();
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
        try{
        	if (parameters == null || parameters.length == 0) {
				throw new ScriptException("fill函数的参数为空!");
			} else if(parameters.length==2 && parameters[0] instanceof DataSetColumn){
				fillDataSetColumn((DataSetColumn)parameters[0],parameters[1]);
			} else if(parameters.length==3 && parameters[0] instanceof DataSet){
				fillDataSet((AbstractDataSet)parameters[0],parameters[1],parameters[2]);
			} else {
				throw new ScriptException("fill函数的参数格式不正确");
			}
				
        }catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException("fill函数的参数格式不正确!", e);
		}
		return null;
	}
	
	private void fillDataSet(AbstractDataSet dataSet,Object item,Object value) throws ScriptException{
		try{
			int col =   ExpressionUtil.convertInteger(item);
			int rowNum = dataSet.getRows();
			for(int i=0;i<rowNum;i++){
				dataSet.setData(dataSet.getShowIndex(i), col, value);
			}
		}catch(Exception e){
			throw new ScriptException("DataSet对象的fill操作发生异常:",e);
		}
	}
	
    private void fillDataSetColumn(DataSetColumn dataSetColumn,Object value) throws ScriptException{
        try{
        	int rowNum = dataSetColumn.size();
        	for(int i=0;i<rowNum;i++){
        		if(dataSetColumn.isIndexFromOne()){
        			dataSetColumn.setData(i+1, value);	
        		}else{
        			dataSetColumn.setData(i, value);
        		}
        		
        	}
		}catch(Exception e){
			throw new ScriptException("DataSetColumn对象的fill操作发生异常:",e);
		}
	}

}
