package org.tinygroup.tinyscript.assignvalue;

import org.apache.commons.beanutils.BeanUtils;
import org.tinygroup.tinyscript.ScriptContext;

public class FieldAssignValueProcessor implements AssignValueProcessor{

	public boolean isMatch(String name, ScriptContext context) {
		String className=null;
		String fieldName=null;
		String[] infos = dealName(name);
		if(infos!=null && infos.length>1){
			className = infos[0];
			fieldName = infos[1];
		}
		if(className!=null && fieldName!=null){
			return context.exist(className);
		}
		
		return false;
	}

	public void process(String name, Object value, ScriptContext context)
			throws Exception {
		String className=null;
		String fieldName=null;
		String[] infos = dealName(name);
		if(infos!=null && infos.length>1){
			className = infos[0];
			fieldName = infos[1];
		}
		Object bean = context.get(className);
		BeanUtils.setProperty(bean, fieldName,value);
	}
	
	private String[] dealName(String name){
		return name.split("\\.");
	}

}
