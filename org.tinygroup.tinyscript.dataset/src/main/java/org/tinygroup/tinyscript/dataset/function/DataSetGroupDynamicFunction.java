package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.GroupDataSet;
import org.tinygroup.tinyscript.dataset.impl.DefaultGroupDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;

/**
 * 数据集动态分组
 * @author yancheng11334
 *
 */
public class DataSetGroupDynamicFunction extends AbstractScriptFunction {

	public String getNames() {
		return "groupDynamic";
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
			if(parameters == null || parameters.length == 0){
				throw new ScriptException(String.format("%s函数的参数为空!", getNames()));
			}else if(checkParameters(parameters, 2)){
				AbstractDataSet dataSet = (AbstractDataSet) getValue(parameters[0]);
				String expression = DataSetUtil.convertExpression(getExpression(parameters[1]));
				return groupDynamic(dataSet,expression,context);
			}else{
				throw new ScriptException(String.format("%s函数的参数格式不正确!", getNames()));
			}
		}catch(ScriptException e){
			throw e;
		}catch(Exception e){
			throw new ScriptException(String.format("%s函数执行发生异常:", getNames()),e);
		}
	}
	
	private GroupDataSet groupDynamic(AbstractDataSet dataSet,String expression,ScriptContext context)  throws Exception{
		Map<Object,DynamicDataSet> result = new HashMap<Object,DynamicDataSet>();
		try{
			int rowNum = dataSet.getRows();
			//记录数不足的数据集直接返回
			if(rowNum<=1){
			   return new DefaultGroupDataSet(dataSet.getFields(),dataSet.isIndexFromOne());
			}
			
			//逐条遍历记录
			for(int i=0;i<rowNum;i++){
				Object key = executeDynamicObject(expression, updateScriptContext(dataSet,i,context));
				DynamicDataSet groupDataSet = result.get(key);
				if(groupDataSet==null){
					//新建分组结果的数据集
					groupDataSet = DataSetUtil.createDynamicDataSet(dataSet, i);
					result.put(key, groupDataSet);
				}else{
					//更新分组结果的数据集
					int row = groupDataSet.getRows();
					groupDataSet.insertRow(dataSet.getShowIndex(row));
					for(int j=0;j<groupDataSet.getColumns();j++){
						groupDataSet.setData(dataSet.getShowIndex(row), dataSet.getShowIndex(j), dataSet.getData(dataSet.getShowIndex(i), dataSet.getShowIndex(j)));
					}
				}
			}
			
			//处理字段
			List<Field> newFields = new ArrayList<Field>();
			for(Field field:dataSet.getFields()){
				newFields.add(field);
			}
			
			return new DefaultGroupDataSet(newFields,new ArrayList<DynamicDataSet>(result.values()),dataSet.isIndexFromOne());
		}catch(Exception e){
			throw new ScriptException(String.format("%s函数执行发生异常:", getNames()),e);
		}
	}
	
	//更新上下文
	private ScriptContext updateScriptContext(AbstractDataSet dataSet,int row,ScriptContext context) throws Exception{
		ScriptContext subContext = new DefaultScriptContext();
		subContext.setParent(context);
		for(int i=0;i<dataSet.getFields().size();i++){
			Field field = dataSet.getFields().get(i);
			subContext.put(field.getName(), dataSet.getData(dataSet.getShowIndex(row), dataSet.getShowIndex(i)));
		}
		return subContext;
	}

}
