package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.expression.TypeConvertUtil;
import org.tinygroup.tinyscript.function.DynamicNameScriptFunction;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;

public class DataSetConvertFunction extends DynamicNameScriptFunction {

	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		String functionName = ScriptContextUtil.getDynamicFunctionName(context);
		try{
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(String.format("%转换函数的参数为空!", functionName));
			}
			AbstractDataSet dataSet = (AbstractDataSet) getValue(parameters[0]);
			int noFieldStart = -1;
			List<Integer> cols = new ArrayList<Integer>();
			for(int i=1;i<parameters.length;i++){
				if(parameters[i] instanceof String){
					int col = DataSetUtil.getFieldIndex(dataSet, (String)parameters[i]);
					if(col<0){
						noFieldStart = i;
						break;	
					}else{
						cols.add(col);
					}
				}else{
					noFieldStart = i;
					break;
				}
			}
			
			if(noFieldStart<0){
			   //执行无辅助参数的转换
			   return convert(dataSet,functionName,cols);
			}else{
			   //执行带辅助参数的转换
			   Object[] rules = new Object[parameters.length-noFieldStart];
			   System.arraycopy(parameters, noFieldStart, rules, 0, rules.length);
			   return convert(dataSet,functionName,cols,rules);
			}
			
		}catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(String.format("%函数的参数格式不正确!", functionName),e);
		}
	}
	

	//本参数为实际下标
	private Object convert(AbstractDataSet dataSet,String type,List<Integer> cols,Object... rules) throws Exception{
		for(int i=0;i<dataSet.getRows();i++){
			for(int col:cols){
				Object v = dataSet.getData(dataSet.getShowIndex(i), dataSet.getShowIndex(col));
				if(v!=null){
				   if(rules!=null){
					   dataSet.setData(dataSet.getShowIndex(i), dataSet.getShowIndex(col), TypeConvertUtil.convert(type, v,rules));
				   }else{
					   dataSet.setData(dataSet.getShowIndex(i), dataSet.getShowIndex(col), TypeConvertUtil.convert(type, v)); 
				   }
				   
				}
			}
			
		}
		return dataSet;
	}

	public boolean exsitFunctionName(String name) {
		return TypeConvertUtil.exsitType(name);
	}

}
