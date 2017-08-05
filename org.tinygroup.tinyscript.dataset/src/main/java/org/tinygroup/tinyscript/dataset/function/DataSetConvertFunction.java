package org.tinygroup.tinyscript.dataset.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;

public class DataSetConvertFunction extends AbstractScriptFunction {

	public String getNames() {
		return "convert";
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
				throw new ScriptException("convert函数的参数为空!");
			}else if(checkParameters(parameters, 3)){
				AbstractDataSet dataSet = (AbstractDataSet) getValue(parameters[0]);
				String field =  (String) getValue(parameters[1]);
				String  type= (String) getValue(parameters[2]);
				int col = getColumn(dataSet,field,parameters[1]);
				
				return convert(dataSet,col,type,null);
				
			}else if(checkParameters(parameters, 4)){
				AbstractDataSet dataSet = (AbstractDataSet) getValue(parameters[0]);
				String expression =  getExpression(parameters[1]);
				String  type= (String) getValue(parameters[2]);
				String  rule =(String) getValue(parameters[3]);
                int col = getColumn(dataSet,expression,parameters[1]);
				
				return convert(dataSet,col,type,rule);
			}else{
				throw new ScriptException("convert函数的参数格式不正确!");
			}
		}catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException("convert函数的参数格式不正确!", e);
		}
	}
	
	private int getColumn(DataSet dataSet,String field,Object p1) throws Exception{
		int col = -1;
		try{
			col = DataSetUtil.getFieldIndex(dataSet, field);
			if(col<0){
			   col = ExpressionUtil.convertInteger(getValue(p1));
			   if(getScriptEngine().isIndexFromOne()){
				   col = col -1;
			   }
			}
		}catch(Exception e){
			 throw new ScriptException(String.format("匹配的字段名%s发生异常", field),e);
		}
		
		if(col<0){
		    throw new ScriptException(String.format("不能匹配的字段名%s", field));
		}
		return col;
	}

	//本参数为实际下标
	private Object convert(AbstractDataSet dataSet, int col, String type,String rule) throws Exception{
		for(int i=0;i<dataSet.getRows();i++){
			Object v = dataSet.getData(dataSet.getShowIndex(i), dataSet.getShowIndex(col));
			if(v!=null){
			   if(rule!=null){
				   dataSet.setData(dataSet.getShowIndex(i), dataSet.getShowIndex(col), convertType(v,type,rule));
			   }else{
				   dataSet.setData(dataSet.getShowIndex(i), dataSet.getShowIndex(col), convertType(v,type)); 
			   }
			   
			}
		}
		return dataSet;
	}
	
	//处理简单数值类型转换
	private Object convertType(Object v,String type) throws Exception{
		String script = "return "+type+"(value);"; 
		ScriptContext context = new DefaultScriptContext();
		context.put("value", v);
		return getScriptEngine().execute(script, context);
	}
	
	//处理如Date较复杂对象转换
	private Object convertType(Object v,String type,String rule) throws Exception{
		String script = "return "+type+"(value,rule);"; 
        ScriptContext context = new DefaultScriptContext();
		context.put("value", v);
		context.put("rule", rule);
		return getScriptEngine().execute(script, context);
	}

}
