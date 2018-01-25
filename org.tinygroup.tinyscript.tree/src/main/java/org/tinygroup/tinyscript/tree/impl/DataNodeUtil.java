package org.tinygroup.tinyscript.tree.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.tinygroup.tinyscript.tree.DataNode;
import org.tinygroup.tinyscript.tree.impl.TreeDataNode.DataNodeArray;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 树型节点工具类
 * @author yancheng11334
 *
 */
public final class DataNodeUtil {

	private DataNodeUtil(){
		
	}
	
	/**
	 * 将TreeDataNode转换JSON字符串
	 * @param tree
	 * @return
	 */
	public static String toJson(TreeDataNode tree){
		JSONObject json = new JSONObject();
		convertJson(tree,json);
		return json.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	private static void convertJson(TreeDataNode tree,JSONObject json){
		if(tree==null || json==null){
		   return;
		}
		Map<String,Object> maps = (Map<String,Object>) tree.getValue();
		for(Entry<String, Object> entry:maps.entrySet()){
			if(entry.getValue()!=null && entry.getValue() instanceof DataNodeArray){
				//设置子节点
				DataNodeArray array = (DataNodeArray) entry.getValue();
				List<DataNode> children = array.getChildren();
				if(array.getLength()>1){
				   //节点列表
				   List<Object> list = new ArrayList<Object>();
				   for(DataNode node:children){
					   JSONObject child = new JSONObject();
					   convertJson((TreeDataNode)node,child); 
					   list.add(child);
				   }
				   json.put(entry.getKey(), new JSONArray(list));
				}else{
				   //单个节点
				   JSONObject child = new JSONObject();
				   convertJson((TreeDataNode)children.get(0),child);
				   json.put(entry.getKey(), child);
				}
			}else{
				//设置属性
				json.put(entry.getKey(), entry.getValue());
			}
		}
	}
}
