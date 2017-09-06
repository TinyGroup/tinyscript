package org.tinygroup.tinyscript.expression;

import java.util.List;

public interface RangeOperator {
	
	boolean isMatch(Object start,Object end);
	
	List<Object> createRange(Object start,Object end);
}
