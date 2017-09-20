package org.tinygroup.tinyscript.tree.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.tinygroup.tinyscript.tree.DataNode;

import com.alibaba.fastjson.JSONObject;

public class JsonObjectNode extends AbstractJsonNode{

	private String name;
	private JSONObject object;
	
	public JsonObjectNode(String name,JSONObject object){
		this.name = name;
		this.object = object;
	}
	
	public JsonObjectNode(String name,JSONObject object,DataNode parent){
		this.name = name;
		this.object = object;
		setParent(parent);
	}

	@SuppressWarnings("unchecked")
	public <T> T getSource() {
		return (T)object;
	}

	public String getName() {
		return name;
	}

	public List<DataNode> getChildren() {
		List<DataNode> children = new ArrayList<DataNode>();
		for(Entry<String, Object> entry:object.entrySet()){
			children.add(createDataNode(entry));
		}
		return children;
	}

	public DataNode getChild(int i) {
		for(Entry<String, Object> entry:object.entrySet()){
		    if(i==0){
		       return createDataNode(entry);
		    }
		    i--;
		}
		return null;
	}

	public DataNode getChild(String name) {
		Object value = object.get(name);
		if(value!=null){
		   return createDataNode(name,value);
		}
		return null;
	}

	public DataNode addNode(DataNode node) {
		if(node instanceof JsonObjectNode){
			object.put(node.getName(), node.getSource());
		}else if(node instanceof JsonArrayNode){
			object.put(node.getName(), node.getSource());
		}else {
			object.put(node.getName(), node.getValue());
		}
		return this;
	}

	public DataNode addNode(String name, Object value) {
		object.put(name, value);
		return this;
	}

	public DataNode removeNode(DataNode node) {
		object.remove(node.getName());
		return this;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JsonObjectNode other = (JsonObjectNode) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		return true;
	}
	
	public String toString() {
		return object.toString();
	}

}
