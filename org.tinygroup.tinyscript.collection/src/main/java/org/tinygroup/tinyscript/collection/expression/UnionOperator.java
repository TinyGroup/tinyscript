package org.tinygroup.tinyscript.collection.expression;

import java.util.*;

import org.tinygroup.tinyscript.expression.operator.ExtendTwoOperator;

/**
 * +操作符(集合的并运算)
 * @author yancheng11334
 *
 */
public class UnionOperator extends ExtendTwoOperator{

	public String getOperation() {
		return "+";
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
		    return unite((List)left,(List)right);
		}else if(isType(left, Set.class) && isType(right, Set.class)){
			return unite((Set)left,(Set)right);
		}else if(isType(left, Map.class) && isType(right, Map.class)){
			return unite((Map)left,(Map)right);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List unite(List t1, List t2) {
      List list = new ArrayList(t1);
      for (Object element : t2) {
          if (!list.contains(element)) {
              list.add(element);
          }
      }
      return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Set unite(Set t1, Set t2) {
		Set set = new HashSet(t1);
		set.addAll(t2);
		return set;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Map unite(Map t1, Map t2) {
		Map map = new HashMap(t1);
		map.putAll(t2);
		return map;
	}
}
