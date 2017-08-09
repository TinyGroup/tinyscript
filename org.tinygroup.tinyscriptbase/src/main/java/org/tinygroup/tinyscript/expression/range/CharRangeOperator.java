package org.tinygroup.tinyscript.expression.range;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.expression.RangeOperator;

public class CharRangeOperator implements RangeOperator{

	public boolean isMatch(Object c) {
		return c instanceof Character;
	}

	public List<Object> createRange(Object start, Object end) {
		int s = (int)((Character)start).charValue(); 
		int e = (int)((Character)end).charValue();
		List<Object> list = new ArrayList<Object>();
		int num = s-e;
		if(num<0){
			//从小到大
			for(int i=s;i<=e;i++){
				list.add((char)i);
			}
		}else if(num>0){
			//从大到小
			for(int i=s;i>=e;i--){
				list.add((char)i);
			}
		}else {
			//相同元素
			list.add(start);
		}
		return list;
	}

	

}
