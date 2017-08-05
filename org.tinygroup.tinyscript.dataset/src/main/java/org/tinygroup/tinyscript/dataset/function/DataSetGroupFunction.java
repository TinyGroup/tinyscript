package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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

/**
 * 数据集分组聚合的算法
 * @author yancheng11334
 *
 */
public class DataSetGroupFunction extends AbstractScriptFunction {
	
	public String getNames() {
		return "group";
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
				throw new ScriptException("group函数的参数为空!");
			}else if(parameters.length >= 2){
				DataSet dataSet = (DataSet) getValue(parameters[0]);
				String[] fields = new String[parameters.length-1];
				for(int i=0;i<fields.length;i++){
					String expression = convertExpression(getExpression(parameters[i+1]));
					fields[i] = (String)getScriptEngine().execute(expression, context);
				}
				return group(dataSet,fields);
			}else{
				throw new ScriptException("group函数的参数格式不正确!");
			}
		}catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException("group函数的参数格式不正确!", e);
		}
	}
	
	/**
	 * 分组聚合
	 * @param dataSet
	 * @param fields
	 * @return
	 * @throws Exception
	 */
	private GroupDataSet group(DataSet dataSet,String[] fields)  throws Exception{
		Map<FieldKey,DynamicDataSet> result = new LinkedHashMap<FieldKey,DynamicDataSet>();
		try{
			int[] columns = getFieldIndexs(dataSet,fields);
            int rowNum = dataSet.getRows();
			//记录数不足的数据集直接返回
			if(rowNum<=1){
			   return new DefaultGroupDataSet(dataSet.getFields(),dataSet.isIndexFromOne());
			}
			
			AbstractDataSet abstractDataSet = (AbstractDataSet) dataSet;
			//逐条遍历记录
			for(int i=0;i<rowNum;i++){
				FieldKey key = createKey(i,columns,abstractDataSet);
				DynamicDataSet groupDataSet = result.get(key);
				if(groupDataSet==null){
					//新建分组结果的数据集
					groupDataSet = DataSetUtil.createDynamicDataSet(dataSet, i);
					result.put(key, groupDataSet);
				}else{
					//更新分组结果的数据集
					int row = groupDataSet.getRows();
					groupDataSet.insertRow(abstractDataSet.getShowIndex(row));
					for(int j=0;j<groupDataSet.getColumns();j++){
						groupDataSet.setData(abstractDataSet.getShowIndex(row), abstractDataSet.getShowIndex(j), dataSet.getData(abstractDataSet.getShowIndex(i), abstractDataSet.getShowIndex(j)));
					}
				}
			}
			List<Field> newFields = new ArrayList<Field>();
			for(Field field:dataSet.getFields()){
				newFields.add(field);
			}
			return new DefaultGroupDataSet(newFields,new ArrayList<DynamicDataSet>(result.values()),dataSet.isIndexFromOne());
			
		}catch(Exception e){
			throw new ScriptException("执行group函数发生异常",e);
		}
	}
	
	private int[] getFieldIndexs(DataSet dataSet,String[] fields) throws Exception{
		int[] indexs = new int[fields.length];
		for(int i=0;i<fields.length;i++){
			indexs[i] = DataSetUtil.getFieldIndex(dataSet, fields[i]);
			if(indexs[i]<0){
			   throw new ScriptException(String.format("当前数据集不存在%s的字段列", fields[i]));
			}
		}
		return indexs;
	}
	
	//本参数为实际下标,注意转换
	private FieldKey createKey(int row,int[] columns,AbstractDataSet abstractDataSet) throws Exception{
		FieldKey key = new FieldKey();
		for(int col:columns){
		    Object value = abstractDataSet.getData(abstractDataSet.getShowIndex(row), abstractDataSet.getShowIndex(col));
		    key.values.add(value);
		}
		return key;
	}
	
	/**
	 * 分组键值
	 * @author yancheng11334
	 *
	 */
	class FieldKey {
		private List<Object> values = new ArrayList<Object>();

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((values == null) ? 0 : values.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			FieldKey other = (FieldKey) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (values == null) {
				if (other.values != null)
					return false;
			} else if (!values.equals(other.values))
				return false;
			return true;
		}

		private DataSetGroupFunction getOuterType() {
			return DataSetGroupFunction.this;
		}
		
	}

}
