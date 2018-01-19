package org.tinygroup.tinyscript.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.tinyscript.ScriptContext;

public class DefaultScriptContext extends ContextImpl implements ScriptContext{

	/**
	 * 
	 */
	private static final long serialVersionUID = 465771309967928810L;

	public DefaultScriptContext() {
		super();
	}

	@SuppressWarnings("rawtypes")
	public DefaultScriptContext(Map map) {
		super(map);
	}

	public boolean exist(String name) {
        boolean exist = super.exist(name);
        if (exist) {
            return true;
        }
        Context parentContext = getParent();
        if (parentContext != null) {
            return parentContext.exist(name);
        }
        return false;
    }

    public <T> T get(String name) {
        T result = super.get(name);
        if (result != null) {
            return (T) result;
        }
        Context parentContext = getParent();
        if (parentContext != null) {
            return parentContext.get(name);
        }
        return null;
    }
    
    public Map<String, Object> getTotalItemMap() {
    	List<Context> contextList = new ArrayList<Context>();
    	
    	//获取完整的上下文链
    	Context parentContext = getParent();
    	while(parentContext!=null){
    		contextList.add(0,parentContext);
    		parentContext = parentContext.getParent();
    	}
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	//合并上下文，优先级：儿子高于父亲
    	for(Context context:contextList){
    		map.putAll(context.getItemMap());
    	}
    	map.putAll(getItemMap());
    	return map;
    }
  
}
