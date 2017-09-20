package org.tinygroup.tinyscript.tree.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.tinygroup.tinyscript.tree.DataNode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

/**
 * fastjson的根节点
 * @author yancheng11334
 *
 */
public class FastJsonNode extends AbstractJsonNode{
    private Map<String,Object> map;
    
    @SuppressWarnings("unchecked")
	public FastJsonNode(String json){
    	map = JSON.parseObject(json,LinkedHashMap.class,Feature.OrderedField);
    }
    
    public FastJsonNode(JSONObject json){
    	map = json;
    }
   
	@SuppressWarnings("unchecked")
	public <T> T getSource() {
		return (T) map;
	}

	public String getName() {
		return null;
	}

	public boolean isRoot() {
		return true;
	}

	public List<DataNode> getChildren() {
		List<DataNode> children = new ArrayList<DataNode>();
		for(Entry<String, Object> entry:map.entrySet()){
			children.add(createDataNode(entry));
		}
		return children;
	}

	public DataNode getChild(int i) {
		for(Entry<String, Object> entry:map.entrySet()){
		    if(i==0){
		       return createDataNode(entry);
		    }
		    i--;
		}
		return null;
	}

	public DataNode getChild(String name) {
		Object value = map.get(name);
		if(value!=null){
		   return createDataNode(name,value);
		}
		return null;
	}

	public DataNode addNode(DataNode node) {
		if(node instanceof JsonObjectNode){
		   map.put(node.getName(), node.getSource());
		}else if(node instanceof JsonArrayNode){
		   map.put(node.getName(), node.getSource());
		}else {
		   map.put(node.getName(), node.getValue());
		}
		return this;
	}

	public DataNode addNode(String name, Object value) {
		map.put(name, value);
		return this;
	}

	public DataNode removeNode(DataNode node) {
		map.remove(node.getName());
		return this;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FastJsonNode other = (FastJsonNode) obj;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		return true;
	}

	public String toString() {
		return map.toString();
	}

	
}
