package org.tinygroup.tinyscript.tree;

import java.util.List;

/**
 * 数据节点
 * @author yancheng11334
 *
 */
public interface DataNode {

	/**
	 * 获取父亲节点
	 * @return
	 */
	DataNode getParent();
	
	/**
	 * 获取节点值
	 * @return
	 */
	Object getValue();
	
	/**
	 * 设置节点值
	 * @param value
	 */
	void setValue(Object value);
	
	/**
	 * 获取原始类型
	 * @return
	 */
	<T> T getSource();
	
	/**
	 * 获取节点名
	 * @return
	 */
	String getName();
	
	/**
	 * 是否叶子节点
	 * @return
	 */
	boolean isLeaf();
	
	/**
	 * 是否根节点
	 * @return
	 */
	boolean isRoot();
	
	/**
	 * 获取儿子节点列表
	 * @return
	 */
	List<DataNode>  getChildren();
	
	/**
	 * 获取某个儿子节点
	 * @param i
	 * @return
	 */
	DataNode  getChild(int i);
	
	/**
	 * 获取某个儿子节点
	 * @param name
	 * @return
	 */
	DataNode  getChild(String name);
	
	/**
	 * 查询某个节点
	 * @param name
	 * @return
	 */
	DataNode  findNode(String name);
	
	/**
	 * 添加某个子节点
	 * @param node
	 */
	DataNode addNode(DataNode node);
	
	/**
	 * 添加某个子节点
	 * @param name
	 * @param value
	 * @return
	 */
	DataNode addNode(String name,Object value);
	
	/**
	 * 删除某个子节点
	 * @param node
	 */
	DataNode removeNode(DataNode node);
	
	/**
	 * 删除某个子节点
	 * @param name
	 */
	DataNode removeNode(String name);
}
