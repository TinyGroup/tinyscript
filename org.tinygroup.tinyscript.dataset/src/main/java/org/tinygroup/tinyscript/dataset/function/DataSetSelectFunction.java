package org.tinygroup.tinyscript.dataset.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;

/**
 * 序表选择字段函数
 * @author yancheng11334
 *
 */
public class DataSetSelectFunction extends AbstractScriptFunction {

	public String getNames() {
		return "select";
	}
	
	public String getBindingTypes() {
		return DynamicDataSet.class.getName();
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
        	if (parameters == null || parameters.length == 0) {
				throw new ScriptException("select函数的参数为空!");
			} else if(parameters.length >=2 && parameters[0] instanceof DynamicDataSet){
				Object[] args = new Object[parameters.length-1];
				for(int i=0;i<args.length;i++){
				   args[i] = parameters[i+1];
				}
			    return select((DynamicDataSet)parameters[0],args);
			} else {
				throw new ScriptException("select函数的参数格式不正确");
			}
				
        }catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException("select函数的参数格式不正确!", e);
		}
	}
	
	/**
	 * 执行选择字段
	 * @param dataSet
	 * @param objects
	 * @return
	 */
    private DynamicDataSet select(DynamicDataSet dataSet,Object...objects) throws Exception{
    	boolean[] columns = new boolean[dataSet.getColumns()];
    	//判断哪些字段需要保留
    	for(Object obj:objects){
    	   if(obj instanceof Integer){
    		  if(dataSet.isIndexFromOne()){
    			  columns[((Integer)obj)-1] = true;
    		  }else{
    			  columns[(Integer)obj] = true;
    		  }
    		  
    	   }else if(obj instanceof String){
    		  int index = DataSetUtil.getFieldIndex(dataSet, (String)obj);
    		  if(index<0){
    			 throw new ScriptException(String.format("不能匹配的字段名%s", obj));
    		  }
    		  columns[index] = true;
    	   }else{
    		   throw new ScriptException(String.format("不能识别的字段对象%s", obj));
    	   }
    	}
    	//执行删除逻辑,自后往前删除
    	for(int i=columns.length-1;i>=0;i--){
    		if(columns[i]==false){
    		   dataSet.deleteColumn(dataSet.getShowIndex(i));
    		}
    	}
    	return dataSet;
    }

}
