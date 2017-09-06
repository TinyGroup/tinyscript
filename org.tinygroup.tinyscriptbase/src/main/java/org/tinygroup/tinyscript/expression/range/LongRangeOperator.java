package org.tinygroup.tinyscript.expression.range;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.expression.RangeOperator;

public class LongRangeOperator implements RangeOperator{

	public boolean isMatch(Object start, Object end) {
		return start instanceof Long && end instanceof Long;
	}

	public List<Object> createRange(Object start, Object end) {
		long s = (Long) start;
		long e = (Long) end;
		List<Object> list = new ArrayList<Object>();
		int num = (int)(s-e);
		if(num<0){
			num = -num;
			//从小到大
			for(int i=0;i<=num;i++){
				list.add(s+i);
			}
		}else if(num>0){
			//从大到小
			for(int i=num;i>=0;i--){
				list.add(e+i);
			}
		}else {
			//相同元素
			list.add(start);
		}
		return list;
	}

}
