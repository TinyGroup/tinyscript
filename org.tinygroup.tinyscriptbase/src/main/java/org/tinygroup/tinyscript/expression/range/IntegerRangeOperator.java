package org.tinygroup.tinyscript.expression.range;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.expression.RangeOperator;

public class IntegerRangeOperator implements RangeOperator{

	public boolean isMatch(Object c) {
		return c instanceof Integer;
	}

	public List<Object> createRange(Object start, Object end) {
		int s = (Integer) start;
		int e = (Integer) end;
		List<Object> list = new ArrayList<Object>();
		int order = s-e;
		if(order<0){
			//从小到大
			for(int i=s;i<=e;i++){
				list.add(i);
			}
		}else if(order>0){
			//从大到小
			for(int i=s;i>=e;i--){
				list.add(i);
			}
		}else {
			//相同元素
			list.add(start);
		}
		return list;
	}

}
