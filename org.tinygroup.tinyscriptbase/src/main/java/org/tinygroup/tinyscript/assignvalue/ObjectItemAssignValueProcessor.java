package org.tinygroup.tinyscript.assignvalue;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;
import org.tinygroup.tinyscript.objectitem.ObjectItemUtil;

public class ObjectItemAssignValueProcessor implements AssignValueProcessor{

	public boolean isMatch(String name, ScriptContext context) {
		int s = name.indexOf("[");
		int e = name.indexOf("]");
		return s>0 && e>s;
	}

	public void process(String name, Object value, ScriptContext context)
			throws Exception {	
		
		ArrayStruct array = new ArrayStruct(name);
	    Object obj = ScriptContextUtil.executeExpression(context, array.arrayName);
	    Object[] items = ScriptContextUtil.executeExpression(context, array.arrayItems);
		ObjectItemUtil.assignValue(context, value, obj, items);
	}
	
    class  ArrayStruct {
    	String arrayName;
    	String[] arrayItems;
    	
    	public ArrayStruct(String name){
    		arrayName = name.substring(0, name.indexOf("["));
    		
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
    		   items.add(name.substring(s+1, e));
    		}
    		arrayItems = new String[items.size()];
    		arrayItems = items.toArray(arrayItems);
    	}
    }
}
