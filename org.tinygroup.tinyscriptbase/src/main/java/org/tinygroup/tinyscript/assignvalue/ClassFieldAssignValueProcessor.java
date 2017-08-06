package org.tinygroup.tinyscript.assignvalue;

import org.tinygroup.tinyscript.ScriptClassInstance;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;

/**
 * 类属性赋值操作处理器
 * @author yancheng11334
 *
 */
public class ClassFieldAssignValueProcessor implements AssignValueProcessor{

	
	public boolean isMatch(String name, ScriptContext context) {
		String className=null;
		String fieldName=null;
		String[] infos = dealName(name);
		boolean thisTag = false;
		if(infos!=null && infos.length>1){
			if("this".equals(infos[0])){
				className = null;
				thisTag = true;
			}else{
				className = infos[0];
			}
			fieldName = infos[1];
		}else{
			fieldName = name;
		}
		
		ScriptClassInstance instance = null;
		if(className!=null){
			Object obj = context.get(className);
			if(obj instanceof ScriptClassInstance){
				instance = (ScriptClassInstance) obj;
			}
		}else{
			instance = ScriptContextUtil.getScriptClassInstance(context);
		}
		
		if(instance!=null){
		   
		   if(thisTag){
			   //存在this关键字表示不是同名变量
			   return instance.existField(fieldName);
		   }else{
			 //类实例存在该字段，并且当前环境不存在同名变量
			   return instance.existField(fieldName) && !context.getItemMap().containsKey(fieldName);
		   }
		}
		return false;
	}

	public void process(String name, Object value, ScriptContext context)
			throws Exception {
		String className=null;
		String fieldName=null;
		String[] infos = dealName(name);
		if(infos!=null && infos.length>1){
			if("this".equals(infos[0])){
				className = null;
			}else{
				className = infos[0];
			}
			fieldName = infos[1];
		}else{
			fieldName = name;
		}
		
		ScriptClassInstance instance = null;
		if(className!=null){
			instance = context.get(className);
		}else{
			instance = ScriptContextUtil.getScriptClassInstance(context);
		}
		if(instance!=null){
		   instance.setField(fieldName, value);
		   instance.setScriptContext(context);  //更新上下文
		}
	}
	
	
	private String[] dealName(String name){
		return name.split("\\.");
	}

}
