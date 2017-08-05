package org.tinygroup.tinyscript.datasetwithtree.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.tree.DataNode;
import org.tinygroup.tinyscript.tree.impl.AbstractDataNode;
import org.tinygroup.tinyscript.tree.impl.DefaultDataNode;

/**
 * 将DataSet转换未tree
 * @author yancheng11334
 *
 */
public class DataSetToTreeFunction extends AbstractScriptFunction {

	public String getNames() {
		return "toTree";
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
			}else if(checkParameters(parameters, 3)){
				AbstractDataSet dataSet = (AbstractDataSet) getValue(parameters[0]);
				String  id =  (String) getValue(parameters[1]);
				String  parentId = (String) getValue(parameters[2]);
				return toTree(dataSet,id,parentId);
			}else{
				throw new ScriptException(String.format("%s函数的参数格式不正确!", getNames()));
			}
		}catch(ScriptException e){
			throw e;
		}catch(Exception e){
			throw new ScriptException(String.format("%s函数执行发生异常:", getNames()),e);
		}
	}
	
	private DataNode toTree(AbstractDataSet dataSet,String id,String parentId) throws Exception{
		
		int idCol = DataSetUtil.getFieldIndex(dataSet, id);
		int parentIdCol = DataSetUtil.getFieldIndex(dataSet, parentId);
		
		Map<String,NodeInfo> maps = new HashMap<String,NodeInfo>();
		Map<String,Map> resultMaps = new HashMap<String,Map>();
		
		String rootId = null;
		//遍历行处理数据
		for(int i=0;i<dataSet.getRows();i++){
			Object idV = dataSet.getData(dataSet.getShowIndex(i), dataSet.getShowIndex(idCol));
			Object parentIdV = dataSet.getData(dataSet.getShowIndex(i), dataSet.getShowIndex(parentIdCol));
			if(parentIdV==null){
				parentIdV ="";
				rootId = idV.toString();
			}
			Map rowMap = createMap(dataSet,i);
			resultMaps.put(idV.toString(), rowMap);
			
			NodeInfo info = maps.get(parentIdV.toString());
			if(info==null){
				info = new NodeInfo();
				info.pid = parentIdV.toString();
				info.ids.add(idV.toString());
				maps.put(parentIdV.toString(), info);
			}else{
				info.ids.add(idV.toString());
			}
		}
		
		//创建树
		NodeInfo rootInfo = maps.get("");
		if(rootInfo==null || rootId == null){
		   throw new ScriptException("数据集转换树型结构失败:没有找到根节点信息");	
		}
		
		DataNode root = new DefaultDataNode();
		dealDataNode(root,maps,resultMaps,rootInfo.ids,rootId);
		return root;
	}
	
	//递归执行树型节点的添加
	private void dealDataNode(DataNode node,Map<String,NodeInfo> maps,Map<String,Map> resultMaps,List<String> ids,String rootId){
		if(node.getName()==null){
		   AbstractDataNode abstractDataNode = (AbstractDataNode) node;
		   abstractDataNode.setName(rootId);
		   abstractDataNode.setValue(resultMaps.get(rootId));
		   NodeInfo info = maps.get(rootId); 
		   if(info!=null){
			  dealDataNode(node,maps,resultMaps,info.ids,rootId);  
		   }
		   
		}else{
			if(ids!=null && ids.size()>0){
			    for(String id:ids){
					Map rowMap = resultMaps.get(id);
					DataNode subNode = new DefaultDataNode(id,rowMap);
					node.addNode(subNode); 
					if(maps.containsKey(id)){
					   NodeInfo info = maps.get(id); 
					   dealDataNode(subNode,maps,resultMaps,info.ids,rootId);
					}
				}
			}
		}
		
	}
	
	@SuppressWarnings({"rawtypes" })
	private Map createMap(AbstractDataSet dataSet,int row) throws Exception {
		Map<String,Object> maps = new HashMap<String,Object>();
		for(int i=0;i<dataSet.getColumns();i++){
			maps.put(dataSet.getFields().get(i).getName(), dataSet.getData(dataSet.getShowIndex(row), dataSet.getShowIndex(i)));
		}
		return maps;
	}
	
	class NodeInfo {
		String pid;
		List<String> ids = new ArrayList<String>();
		
		public String toString() {
			return "NodeInfo [pid=" + pid + ", ids=" + ids + "]";
		}
		
	}

}
