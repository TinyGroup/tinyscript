package org.tinygroup.tinyscript.collection.function.set;

import java.util.HashSet;
import java.util.Set;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.collection.function.AbstractXorFunction;

@SuppressWarnings("rawtypes")
public class SetXorFunction extends AbstractXorFunction<Set>{

	public String getBindingTypes() {
		return "java.util.Set";
	}
	
	@SuppressWarnings("unchecked")
	protected Set xor(Set t1, Set t2) throws ScriptException {
		Set set = new HashSet(t1);
		set.addAll(t2);
		Set all = new HashSet();
		for(Object obj:set){
		   if(t1.contains(obj) && t2.contains(obj)){
			  all.add(obj);
		   }
		}
		set.removeAll(all);
		return set;
	}

}
