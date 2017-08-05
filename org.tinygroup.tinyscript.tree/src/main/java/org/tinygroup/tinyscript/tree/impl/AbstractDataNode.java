package org.tinygroup.tinyscript.tree.impl;

import org.tinygroup.tinyscript.tree.DataNode;

/**
 * 抽象Tree实现
 * @author yancheng11334
 *
 */
public abstract class AbstractDataNode implements DataNode{

	private DataNode parent;
	private String name;
	private Object value;
	
	public DataNode getParent() {
		return parent;
	}
	public void setParent(DataNode parent) {
		this.parent = parent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	public boolean isRoot() {
		return parent==null;
	}
	
	public DataNode removeNode(String name) {
		return removeNode(getChild(name));
	}
}
