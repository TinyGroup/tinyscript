package org.tinygroup.tinyscript.collection.function.list;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.collection.function.AbstractXorFunction;

@SuppressWarnings("rawtypes")
public class XorFunction extends AbstractXorFunction<List>{

	public String getBindingTypes() {
		return "java.util.List";
	}

	@SuppressWarnings("unchecked")
	protected List xor(List t1, List t2) throws ScriptException {
		List list = new ArrayList(t1);
		list.addAll(t2);
		List all = new ArrayList();
		for(Object obj:list){
		   if(t1.contains(obj) && t2.contains(obj)){
			  all.add(obj);
		   }
		}
		list.removeAll(all);
		return list;
	}

}
