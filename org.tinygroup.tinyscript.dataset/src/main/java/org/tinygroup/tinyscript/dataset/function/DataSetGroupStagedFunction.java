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
 * 数据集按条件分组
 * @author yancheng11334
 *
 */
public class DataSetGroupStagedFunction extends AbstractGroupFunction {

	public String getNames() {
		return "groupStaged";
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
			if(parameters == null || parameters.length == 0){
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			}else if(parameters.length>1){
				DynamicDataSet dataSet = (DynamicDataSet) getValue(parameters[0]);
				String[] expressions = new String[parameters.length-1];
				for(int i=0;i<expressions.length;i++){
					expressions[i] = ScriptContextUtil.convertExpression(getExpression(parameters[i+1]));
				}
				return groupStaged(dataSet,expressions,context);
			}else{
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		}catch(ScriptException e){
			throw e;
		}catch(Exception e){
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}
	
	private GroupDataSet groupStaged(DynamicDataSet dataSet,String[] expressions,ScriptContext context)  throws Exception{
		try{
			if(dataSet instanceof MultiLevelGroupDataSet){
				 //某一级序表进行分组
				MultiLevelGroupDataSet multiLevelGroupDataSet = (MultiLevelGroupDataSet) dataSet;
				List<MultiLevelGroupDataSet> subDataSetList = multiLevelGroupDataSet.getUnGroups();
				for(MultiLevelGroupDataSet subDataSet:subDataSetList){
					List<DynamicDataSet> list = group(subDataSet.getSource(),expressions,context);
					subDataSet.setGroups(list);
				}
				return multiLevelGroupDataSet;
			}else{
				//首次分组
				List<DynamicDataSet> list = group(dataSet,expressions,context);
				MultiLevelGroupDataSet multiLevelGroupDataSet = new MultiLevelGroupDataSet(dataSet,list);
				return multiLevelGroupDataSet;
			}
		}catch (ScriptException e) {
			throw e;
		}catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}
	
	private List<DynamicDataSet> group(AbstractDataSet dataSet,String[] expressions,ScriptContext context)  throws Exception{
		Map<String,DynamicDataSet> result = new HashMap<String,DynamicDataSet>();
		try{
			int rowNum = dataSet.getRows();
			
			//逐条遍历记录
			for(int i=0;i<rowNum;i++){
				int showRow = dataSet.getShowIndex(i);
				String key = "";
				for(String expression:expressions){
				   if(executeDynamicBoolean(expression, updateScriptContext(dataSet,i,context))){
					   key = expression;
					   break;
				   }
				}
				DynamicDataSet groupDataSet = result.get(key);
				if(groupDataSet==null){
				   // 新建分组结果的数据集
				   groupDataSet = DataSetUtil.createDynamicDataSet(dataSet, i);
				   result.put(key, groupDataSet);
				}else{
					// 更新分组结果的数据集
				   int groupRowNum = groupDataSet.getRows();
				   int groupShowRow = groupDataSet.getShowIndex(groupRowNum);
				   groupDataSet.insertRow(groupShowRow);
				   for (int j = 0; j < groupDataSet.getColumns(); j++) {
					   int showColumn = groupDataSet.getShowIndex(j);
					   groupDataSet.setData(groupShowRow, showColumn, dataSet.getData(showRow, showColumn));
			       }
				}
			}
			
			//处理分组结果
			List<DynamicDataSet> groups = new ArrayList<DynamicDataSet>();
			for(String expression:expressions){
				DynamicDataSet groupDataSet = result.get(expression);
				if(groupDataSet!=null){
				   groups.add(groupDataSet);
				}
			}
			DynamicDataSet otherDataSet = result.get("");
			if(otherDataSet!=null){
			   groups.add(otherDataSet);
			}
			
			return groups;
		}catch (ScriptException e) {
			throw e;
		}catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

}
