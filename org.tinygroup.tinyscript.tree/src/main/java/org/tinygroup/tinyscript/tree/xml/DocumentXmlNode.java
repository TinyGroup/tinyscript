package org.tinygroup.tinyscript.tree.xml;

import java.util.List;

import org.tinygroup.tinyscript.tree.DataNode;
import org.tinygroup.xmlparser.XmlDocument;
import org.tinygroup.xmlparser.parser.XmlStringParser;

/**
 * XmlDocument节点的包装
 * @author yancheng11334
 *
 */
public class DocumentXmlNode extends AbstractXmlNode{

	private XmlDocument document;
	private DataNode root;
	
	public DocumentXmlNode(String xml){
		document = new XmlStringParser().parse(xml);
		root = new SubXmlNode(document.getRoot());
	}
	
	public DocumentXmlNode(XmlDocument xmlDocument){
		document = xmlDocument;
		root = new SubXmlNode(document.getRoot());
	}

	public DataNode getParent() {
		return null;
	}

	public Object getValue() {
		return root.toString();
	}

	@SuppressWarnings("unchecked")
	public <T> T getSource() {
		return (T) document;
	}

	public void setValue(Object value) {
		//ignore
	}
	
	public String getName() {
		return root.getName();
	}

	public boolean isLeaf() {
		return false;
	}

	public boolean isRoot() {
		return true;
	}

	public List<DataNode> getChildren() {
		return root.getChildren();
	}

	public DataNode getChild(int i) {
		return root.getChild(i);
	}

	public DataNode getChild(String name) {
		return root.getChild(name);
	}

	public DataNode addNode(DataNode node) {
		return root.addNode(node);
	}

	public DataNode addNode(String name, Object value) {
		return root.addNode(name, value);
	}

	public DataNode removeNode(DataNode node) {
		return root.removeNode(node);
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((document == null) ? 0 : document.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentXmlNode other = (DocumentXmlNode) obj;
		if (document == null) {
			if (other.document != null)
				return false;
		} else if (!document.equals(other.document))
			return false;
		return true;
	}

	public String toString() {
		return document.toString();
	}

	
	
}
