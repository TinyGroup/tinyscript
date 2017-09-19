package org.tinygroup.tinyscript.tree.xml;

import org.tinygroup.tinyscript.tree.DataNode;

/**
 * xml抽象节点
 * @author yancheng11334
 *
 */
public abstract class AbstractXmlNode implements DataNode{

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
