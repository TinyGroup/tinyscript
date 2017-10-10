package org.tinygroup.tinyscript.dataset.function;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 合并拆分记录(变更字段)
 * @author yancheng11334
 *
 */
public class DataSetUpdateFieldFunction extends AbstractScriptFunction {

	public String getNames() {
		return "updateField";
	}
	
	public String getBindingTypes() {
		return DynamicDataSet.class.getName();
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
		   if (parameters == null || parameters.length == 0) {
			   throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
		   } else if(parameters!=null && parameters.length==4 && parameters[0]!=null && parameters[1]!=null){
			   DynamicDataSet dynamicDataSet = (DynamicDataSet) parameters[0];
			   LambdaFunction lambdaFunction = (LambdaFunction) parameters[1];
			   List<Field> insertFields = convertFields(parameters[2]);
			   List<Field> deleteFields = convertFields(parameters[3]);
			   
			   if(!CollectionUtil.isEmpty(insertFields)){
				  //执行插入字段逻辑
				  for(Field field:insertFields){
				      dynamicDataSet.insertColumn(dynamicDataSet.getFields().size(), field);
				  }
			   }
			   
			   //逐行遍历
			   for (int i = 0; i < dynamicDataSet.getRows(); i++) {
				   ScriptContext subContext = new DefaultScriptContext();
				   subContext.setParent(context);
				   
				   //加载行记录信息到上下文环境
				   if(lambdaFunction.getParamterNames()!=null && lambdaFunction.getParamterNames().length>0){
					  Object[] readParamters = new Object[lambdaFunction.getParamterNames().length];
					  //只加载用户指定参数 
					  for(int j=0;j<lambdaFunction.getParamterNames().length;j++){
						  int col = DataSetUtil.getFieldIndex(dynamicDataSet, lambdaFunction.getParamterNames()[j]);
						  readParamters[j] = dynamicDataSet.getData(dynamicDataSet.getShowIndex(i), dynamicDataSet.getShowIndex(col));
					  }
					  //动态执行lambda表达式
					  lambdaFunction.execute(subContext,readParamters);
				   }else{
					  //加载全部参数
					  for(int j=0;j<dynamicDataSet.getFields().size();j++){
						  Field f = dynamicDataSet.getFields().get(j);
						  subContext.put(f.getName(), dynamicDataSet.getData(dynamicDataSet.getShowIndex(i), dynamicDataSet.getShowIndex(j)));
					  }
					  //动态执行lambda表达式
					  lambdaFunction.execute(subContext);
				   }
				   
				   //根据插入字段更新列
				   for(int j=0;j<insertFields.size();j++){
					   int col = DataSetUtil.getFieldIndex(dynamicDataSet, insertFields.get(j).getName());
					   dynamicDataSet.setData(dynamicDataSet.getShowIndex(i), dynamicDataSet.getShowIndex(col),subContext.getItemMap().get(insertFields.get(j).getName()));
				   }
			   }
			   
			   if(!CollectionUtil.isEmpty(deleteFields)){
				   //执行删除字段逻辑
				   for(Field field:deleteFields){
					  dynamicDataSet.deleteColumn(field.getName());
				   }
			   }
			   return dynamicDataSet;
		   } else {
			   throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
		   }
		}catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}
	
	/**
	 * 转换字段信息
	 * @param obj
	 * @return
	 * @throws ScriptException
	 */
	@SuppressWarnings("rawtypes")
	private List<Field> convertFields(Object obj) throws ScriptException{
		List<Field> fields = new ArrayList<Field>();
		if(obj!=null){
		   if(obj.getClass().isArray()){
			  int length = Array.getLength(obj);
			  for(int i=0;i<length;i++){
				  addField(fields,Array.get(obj, i));
			  }
		   }else if(obj instanceof Collection){
			  Collection c = (Collection) obj;
			  Iterator it = c.iterator();
			  while(it.hasNext()){
				  addField(fields,it.next());
			  }
		   }else{
			  throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
		   }
		}
		return fields;
	}
	
	private void addField(List<Field> fields,Object o) throws ScriptException{
		if(o instanceof String){
			String name = (String) o;
			fields.add(new Field(name,name,"Object"));
		}else if(o instanceof Field){
			Field f = (Field) o;
			fields.add(f);
		}else{
		    throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
		}
	}
    
}
