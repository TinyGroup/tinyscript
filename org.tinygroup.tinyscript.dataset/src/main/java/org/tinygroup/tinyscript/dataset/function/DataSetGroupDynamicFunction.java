package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.GroupDataSet;
import org.tinygroup.tinyscript.dataset.impl.MultiLevelGroupDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;

/**
 * 数据集动态分组
 * @author yancheng11334
 *
 */
public class DataSetGroupDynamicFunction extends AbstractGroupFunction {

	public String getNames() {
		return "groupDynamic";
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
			if(parameters == null || parameters.length == 0){
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			}else if(checkParameters(parameters, 2)){
				DynamicDataSet dataSet = (DynamicDataSet) getValue(parameters[0]);
				String expression = ScriptContextUtil.convertExpression(getExpression(parameters[1]));
				return groupDynamic(dataSet,expression,context);
			}else{
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		}catch(ScriptException e){
			throw e;
		}catch(Exception e){
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}
	private GroupDataSet groupDynamic(DynamicDataSet dataSet,String expression,ScriptContext context)  throws Exception{
		try{
			if(dataSet instanceof MultiLevelGroupDataSet){
				 //某一级序表进行分组
				MultiLevelGroupDataSet multiLevelGroupDataSet = (MultiLevelGroupDataSet) dataSet;
				List<MultiLevelGroupDataSet> subDataSetList = multiLevelGroupDataSet.getUnGroups();
				for(MultiLevelGroupDataSet subDataSet:subDataSetList){
					List<DynamicDataSet> list = group(subDataSet.getSource(),expression,context);
					subDataSet.setGroups(list);
				}
				return multiLevelGroupDataSet;
			}else{
				//首次分组
				List<DynamicDataSet> list = group(dataSet,expression,context);
				MultiLevelGroupDataSet multiLevelGroupDataSet = new MultiLevelGroupDataSet(dataSet,list);
				return multiLevelGroupDataSet;
			}
		}catch (ScriptException e) {
			throw e;
		}catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}
	
	private List<DynamicDataSet> group(AbstractDataSet dataSet,String expression,ScriptContext context)  throws Exception{
		Map<Object,DynamicDataSet> result = new HashMap<Object,DynamicDataSet>();
		try{
			int rowNum = dataSet.getRows();
			
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
			
			return new ArrayList<DynamicDataSet>(result.values());
		}catch(Exception e){
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}
	

}
