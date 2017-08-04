package org.tinygroup.tinyscript.collection.expression;

import java.util.*;

import org.tinygroup.tinyscript.expression.operator.ExtendTwoOperator;

/**
 * ^操作符(集合的异或运算)
 * @author yancheng11334
 *
 */
public class XorOperator extends ExtendTwoOperator{

	public String getOperation() {
		return "^";
	}

	protected boolean checkParameter(Object left, Object right) {
		if(isType(left, List.class) && isType(right, List.class)){
		   return true;
		}else if(isType(left, Set.class) && isType(right, Set.class)){
		   return true;
		}else if(isType(left, Map.class) && isType(right, Map.class)){
		   return true;
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	protected Object operation(Object left, Object right) {
		if(isType(left, List.class) && isType(right, List.class)){
		    return xor((List)left,(List)right);
		}else if(isType(left, Set.class) && isType(right, Set.class)){
			return xor((Set)left,(Set)right);
		}else if(isType(left, Map.class) && isType(right, Map.class)){
			return xor((Map)left,(Map)right);
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List xor(List t1, List t2) {
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Set xor(Set t1, Set t2) {
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Map xor(Map t1, Map t2) {
		Map map = new HashMap(t1);
		map.putAll(t2);
		Set keys = map.keySet();
		Set all = new HashSet();
		for(Object obj:keys){
		   if(t1.containsKey(obj) && t2.containsKey(obj)){
		     all.add(obj);
		   }
		}
		for(Object obj:all){
			map.remove(obj);
		}
		return map;
	}

}
