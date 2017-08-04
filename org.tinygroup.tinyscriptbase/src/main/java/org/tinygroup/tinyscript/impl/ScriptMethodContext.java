package org.tinygroup.tinyscript.impl;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.tinyscript.ScriptClassInstance;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;
import org.tinygroup.tinyscript.interpret.ScriptUtil;

/**
 * 运行时方法的上下文(类似栈)
 * @author yancheng11334
 *
 */
public class ScriptMethodContext extends ContextImpl implements ScriptContext{
	
	public ScriptClassInstance getScriptClassInstance() {
		return ScriptContextUtil.getScriptClassInstance(this);
	}

	public void setScriptClassInstance(ScriptClassInstance classInstance) {
		ScriptContextUtil.setScriptClassInstance(this, classInstance);
	}
	
	public boolean exist(String name) {
        boolean exist = super.exist(name);
        if (exist) {
            return true;
        }
        Context parentContext = getParent();
        if (parentContext != null) {
        	exist = parentContext.exist(name);
        	if(exist){
        	   return true;
        	}
        }
        if(!name.startsWith("$")){ //避免无限递归
        	ScriptClassInstance object = getScriptClassInstance();
            if(object!=null){
               return object.existField(name);
            }
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
        	result = (T) parentContext.get(name);
        	if (result != null) {
                return result;
            }
        }
        if(!name.startsWith("$")){ //避免无限递归
        	ScriptClassInstance object = getScriptClassInstance();
            if(object!=null){
               return (T) object.getField(name);
            }
        }
        return null;
    }
    
}
