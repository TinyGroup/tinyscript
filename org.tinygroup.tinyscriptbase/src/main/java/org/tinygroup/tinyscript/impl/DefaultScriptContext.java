package org.tinygroup.tinyscript.impl;

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
  
}
