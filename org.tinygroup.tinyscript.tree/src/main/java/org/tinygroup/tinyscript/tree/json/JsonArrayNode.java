package org.tinygroup.tinyscript.tree.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.tree.DataNode;

import com.alibaba.fastjson.JSONArray;

public class JsonArrayNode extends AbstractJsonNode{

	private String name;
	private JSONArray array;
	
	public JsonArrayNode(String name,JSONArray array){
		this.name = name;
		this.array = array;
	}
	
	public JsonArrayNode(String name,JSONArray array,DataNode parent){
		this.name = name;
		this.array = array;
		setParent(parent);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getSource() {
		return (T)array;
	}

	public String getName() {
		return name;
	}

	public List<DataNode> getChildren() {
		List<DataNode> children = new ArrayList<DataNode>();
		Iterator<Object> it = array.iterator();
		int i=0;
		while(it.hasNext()){
			children.add(createDataNode(name+i,it.next()));
			i++;
		}

		return children;
	}

	public DataNode getChild(int i) {
		Object object = array.get(i);
		return createDataNode(name+i,object);
	}

	public DataNode getChild(String name) {
		int i = Integer.parseInt(name.substring(this.name.length()));
		return getChild(i);
	}

	public DataNode addNode(DataNode node) {
		throw new RuntimeException(ResourceBundleUtil.getDefaultMessage("unsupport.info1", "addNode"));
	}

	public DataNode addNode(String name, Object value) {
		throw new RuntimeException(ResourceBundleUtil.getDefaultMessage("unsupport.info1", "addNode"));
	}

	public DataNode removeNode(DataNode node) {
		throw new RuntimeException(ResourceBundleUtil.getDefaultMessage("unsupport.info1", "removeNode"));
	}

}
