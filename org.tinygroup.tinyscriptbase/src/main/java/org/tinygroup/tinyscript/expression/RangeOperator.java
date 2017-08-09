package org.tinygroup.tinyscript.expression;

import java.util.List;

public interface RangeOperator {
	
	boolean isMatch(Object c);
	
	List<Object> createRange(Object start,Object end);
}
