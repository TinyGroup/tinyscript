package org.tinygroup.tinyscript.tree.json;

import java.util.Map.Entry;

import org.tinygroup.tinyscript.tree.DataNode;
import org.tinygroup.tinyscript.tree.impl.DefaultDataNode;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * json抽象节点
 * @author yancheng11334
 *
 */
public abstract class AbstractJsonNode implements DataNode{

	private DataNode parent;
	
	public DataNode getParent() {
		return parent;
	}

	public void setParent(DataNode parent) {
		this.parent = parent;
	}
	
	public boolean isLeaf() {
		return false;
	}

	public boolean isRoot() {
		return false;
	}
	
	public Object getValue() {
		return null;
	}

	public void setValue(Object value) {
		
	}

	protected DataNode createDataNode(String name,Object value){
		 if(value instanceof JSONObject){
			return new JsonObjectNode(name,(JSONObject)value,this);
		 }else if(value instanceof JSONArray){
			return new JsonArrayNode(name,(JSONArray)value,this);
		 }else{
			return new DefaultDataNode(name,value,this);
		 }
	}
	
	protected DataNode createDataNode(Entry<String, Object> entry){
		 return createDataNode(entry.getKey(),entry.getValue());
	}

	public DataNode removeNode(String name) {
		return removeNode(getChild(name));
	}
	
	protected DataNode findNode(DataNode node,String name){
		if(node.getName()!=null && node.getName().equals(name)){
		   return node;
		}
		if(!node.isLeaf()){
		   DataNode result = null;
		   for(DataNode child:node.getChildren()){
			   result = findNode(child,name);
			   if(result!=null){
				  return result;
			   }
		   }
		}
		return null;
	}
	
	public DataNode findNode(String name) {
		return findNode(this,name);
	}

}
