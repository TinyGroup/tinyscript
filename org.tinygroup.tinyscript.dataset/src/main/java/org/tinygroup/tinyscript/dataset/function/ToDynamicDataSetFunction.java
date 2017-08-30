package org.tinygroup.tinyscript.dataset.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.impl.VariableDataSet;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;

/**
 * 转换为动态序表
 * @author yancheng11334
 *
 */
public class ToDynamicDataSetFunction extends AbstractScriptFunction {

	public String getBindingTypes() {
		return DataSet.class.getName();
	}
	
	public String getNames() {
		return "toDynamic";
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(String.format("%s函数的参数为空!",getNames()));
			}else if(checkParameters(parameters, 1)){
				DataSet dataSet = (DataSet) parameters[0];
				if(dataSet instanceof DynamicDataSet){
				   return (DynamicDataSet) dataSet;
				}
				return new VariableDataSet(dataSet);
			}else{
				throw new ScriptException(String.format("%s函数的参数格式不正确!",getNames()));
			}
		}catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(String.format("%s函数的参数格式不正确!",getNames()), e);
		}
		
	}

}
