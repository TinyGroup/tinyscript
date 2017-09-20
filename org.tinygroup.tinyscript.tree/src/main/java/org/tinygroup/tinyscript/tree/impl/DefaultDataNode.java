package org.tinygroup.tinyscript.tree.impl;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.tree.DataNode;

/**
 * 默认树节点的实现
 * @author yancheng11334
 *
 */
public class DefaultDataNode extends AbstractDataNode{

	private List<DataNode> children = new ArrayList<DataNode>();

	public DefaultDataNode() {
		super();
	}
	
	public DefaultDataNode(String name,Object value) {
		super();
		setName(name);
		setValue(value);
	}
	
	public DefaultDataNode(String name,Object value,DataNode parent) {
		super();
		setName(name);
		setValue(value);
		setParent(parent);
	}

	public boolean isLeaf() {
		return children.isEmpty();
	}

	public List<DataNode> getChildren() {
		return children;
	}

	public DataNode getChild(int i) {
		return children.get(i);
	}

	public DataNode getChild(String name) {
		for(DataNode node:children){
		    if(node.getName().equals(name)){
		       return node;
		    }
		}
		return null;
	}

	public DataNode addNode(String name,Object value) {
		return addNode(new DefaultDataNode(name,value));
	}

	public DataNode addNode(DataNode node) {
		AbstractDataNode abstractDataNode = (AbstractDataNode) node;
		abstractDataNode.setParent(this);
		children.add(node);
		return this;
	}

	public DataNode removeNode(DataNode node) {
		AbstractDataNode abstractDataNode = (AbstractDataNode) node;
		abstractDataNode.setParent(null);
		children.remove(node);
		return this;
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

	@SuppressWarnings("unchecked")
	public <T> T getSource() {
		return (T) this;
	}

}
