package org.tinygroup.tinyscript.assignvalue;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.objectitem.ObjectItemUtil;

public class ObjectItemAssignValueProcessor implements AssignValueProcessor{

	public boolean isMatch(String name, ScriptContext context) {
		int s = name.indexOf("[");
		int e = name.indexOf("]");
		return s>0 && e>s;
	}

	public void process(String name, Object value, ScriptContext context)
			throws Exception {	
		String objName = parseObjectName(name);
		Object[] items = parseItems(name);
		Object obj = context.get(objName);
		ObjectItemUtil.assignValue(context, value, obj, items);
	}
	
	private Object[] parseItems(String name){
		int s = 0,e = 0;
		List<String> items = new ArrayList<String>();
		while(s!=-1 && e!=-1){
		   s = name.indexOf("[", e);
		   if(s<0){
			 break;
		   }
		   e = name.indexOf("]", s);
		   if(e<0){
			  break;
		   }
		   items.add(filter(name.substring(s+1, e)));
		}
		return items.toArray();
	}
	
	private String filter(String s){
		if(s.startsWith("\"") && s.endsWith("\"")){
		   return s.substring(1, s.length()-1);
		}else if(s.startsWith("'") && s.endsWith("'")){
		   return s.substring(1, s.length()-1);
		}
		return s;
	}
	
	private String parseObjectName(String name){
		int s = name.indexOf("[");
		return name.substring(0, s);
	}
     

}
