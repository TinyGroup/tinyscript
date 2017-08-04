package org.tinygroup.tinyscript.collection.expression;

import java.util.*;

import org.tinygroup.tinyscript.expression.operator.ExtendTwoOperator;

/**
 * &操作符(集合的交运算)
 * @author yancheng11334
 *
 */
public class IntersectionOperator extends ExtendTwoOperator{

	public String getOperation() {
		return "&";
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
		    return intersect((List)left,(List)right);
		}else if(isType(left, Set.class) && isType(right, Set.class)){
			return intersect((Set)left,(Set)right);
		}else if(isType(left, Map.class) && isType(right, Map.class)){
			return intersect((Map)left,(Map)right);
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List intersect(List t1, List t2) {
		List list = new ArrayList();
		for(Object element:t1){
		    if(t2.contains(element)){
		       list.add(element);
		    }
		}
		return list;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Set intersect(Set t1, Set t2) {
		Set set = new HashSet();
		Iterator it= t1.iterator();
		while(it.hasNext()){
		   Object element = it.next();
		   if(t2.contains(element)){	   
			  set.add(element);
		   }
		}
		return set;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Map intersect(Map t1, Map t2) {
		Map map = new HashMap();
		for(Object key:t1.keySet()){
		    if(t2.containsKey(key)){
		       map.put(key, t1.get(key));
		    }
		}
		return map;
	}


}
