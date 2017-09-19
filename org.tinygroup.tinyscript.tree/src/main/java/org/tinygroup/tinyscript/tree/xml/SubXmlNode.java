package org.tinygroup.tinyscript.tree.xml;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.tree.DataNode;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * XmlNode节点包装
 * @author yancheng11334
 *
 */
public class SubXmlNode extends AbstractXmlNode{

	private XmlNode node;
	
	public SubXmlNode(XmlNode xmlNode){
		node = xmlNode;
	}
	
	public DataNode getParent() {
		return new SubXmlNode(node.getParent());
	}

	public Object getValue() {
		return node.getContent();
	}
	
	public void setValue(Object value) {
		
		node.setContent(value==null?null:value.toString());
	}

	@SuppressWarnings("unchecked")
	public <T> T getSource() {
		return (T)node;
	}

	public String getName() {
		return node.getNodeName();
	}

	public boolean isLeaf() {
		List<XmlNode> nodes = node.getSubNodes();
		return (nodes==null || nodes.isEmpty());
	}

	public boolean isRoot() {
		return false;
	}

	public List<DataNode> getChildren() {
		List<XmlNode> nodes = node.getSubNodes();
		List<DataNode> children = new ArrayList<DataNode>();
		if(nodes!=null){
		   for(XmlNode xmlNode:nodes){
			   children.add(new SubXmlNode(xmlNode));
		   }
		}
		return children;
	}

	public DataNode getChild(int i) {
		List<XmlNode> nodes = node.getSubNodes(); 
		return new SubXmlNode(nodes.get(i));
	}

	public DataNode getChild(String name) {
		return new SubXmlNode(node.getSubNode(name));
	}

	public DataNode addNode(DataNode dataNode) {
		XmlNode xmlNode = dataNode.getSource();
		node.addNode(xmlNode);
		return this;
	}

	public DataNode addNode(String name, Object value) {
		XmlNode xmlNode = new XmlNode(name);
		xmlNode.addContent(value==null?null:value.toString());
		node.addNode(xmlNode);
		return this;
	}

	public DataNode removeNode(DataNode dataNode) {
		node.removeNode(dataNode.getName());
		return this;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubXmlNode other = (SubXmlNode) obj;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		return true;
	}

	public String toString() {
		return node.toString();
	}

	
}
