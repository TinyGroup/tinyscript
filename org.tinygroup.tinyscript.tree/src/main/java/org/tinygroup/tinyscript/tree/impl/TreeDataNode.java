package org.tinygroup.tinyscript.tree.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.tree.DataNode;

/**
 * 树型存储节点,支持属性/子节点/子节点列表
 * @author yancheng11334
 *
 */
public class TreeDataNode implements DataNode{

	private DataNode parent;
	private Map<String,Object> maps = new LinkedHashMap<String,Object>();
	private String name;
	
	public TreeDataNode(){
		
	}
	
    public TreeDataNode(Map<String,Object> maps){
		this.maps = maps;
	}
	
	public DataNode getParent() {
		return parent;
	}
	
	public void setParent(DataNode parent) {
		this.parent = parent;
	}

	public Object getValue() {
		return maps;
	}

	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		if(value instanceof Map){
		   maps = (Map<String,Object>) value;
		}
	}
	
	public Object get(String name) {
		return maps.get(name);
	}
	
	public TreeDataNode put(String name,Object value) {
		maps.put(name, value);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T> T getSource() {
		return (T) this;
	}

	public String getName() {
		return name;
	}

	public boolean isLeaf() {
		for(Object value:maps.values()){
		   if(value instanceof DataNodeArray){
			  return false;
		   }
		}
		return true;
	}

	public boolean isRoot() {
		return parent==null;
	}

	public List<DataNode> getChildren() {
		List<DataNode> children = new ArrayList<DataNode>();
		for(Object value:maps.values()){
		   if(value instanceof DataNodeArray){
			   DataNodeArray array = (DataNodeArray) value;	  
			   children.addAll(array.getChildren());
		   }
		}
		return children;
	}

	public DataNode getChild(int i) {
		List<DataNode> children = getChildren();
		return children.get(i);
	}

	public DataNode getChild(String name) {
		List<DataNode> children = getChildren();
		for(DataNode node:children){
		    if(node.getName().equals(name)){
		       return node;
		    }
		}
		return null;
	}

	public DataNode findNode(String name) {
		return findNode(this,name);
	}
	
	private DataNode findNode(DataNode node,String name){
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

	public DataNode addNode(DataNode node) {
		return addNode(node.getName(),node);
	}

	public DataNode addNode(String name, Object value) {
		if(value instanceof DataNode){
			DataNode node = (DataNode) value;
			DataNodeArray array = (DataNodeArray) maps.get(name);
			if(array==null){
			   array = new DataNodeArray();
			   maps.put(name, array);
			}
			array.addDataNode(node);
			node.setParent(this);
		}else{
		   maps.put(name, value);
		}
		return this;
	}

	public DataNode removeNode(DataNode node) {
		DataNodeArray array = (DataNodeArray) maps.get(node.getName());
		if(array!=null){
		   array.removeDataNode(node);
		   if(array.getLength()==0){
			  maps.remove(node.getName());
		   }
		   node.setParent(null);
		}
		return this;
	}

	public DataNode removeNode(String name) {
	    maps.remove(name);
		return this;
	}
	
	/**
	 * 子节点结构
	 * @author yancheng11334
	 *
	 */
	static class DataNodeArray{
		private List<DataNode> children = new ArrayList<DataNode>();
		
		public DataNodeArray addDataNode(DataNode node){
			children.add(node);
			return this;
		}
		
        public DataNodeArray removeDataNode(DataNode node){
        	children.remove(node);
        	return this;
		}
        
        public int getLength(){
        	return children.size();
        }
        
        public List<DataNode> getChildren(){
        	return children;
        }
	}

}
