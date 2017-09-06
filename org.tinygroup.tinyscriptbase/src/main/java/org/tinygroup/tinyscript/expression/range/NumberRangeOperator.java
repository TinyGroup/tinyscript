package org.tinygroup.tinyscript.expression.range;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.expression.RangeOperator;

/**
 * 匹配number类型，强制转换int
 * @author yancheng11334
 *
 */
public class NumberRangeOperator implements RangeOperator{

	public boolean isMatch(Object start, Object end) {
		return start instanceof Number && end instanceof Number;
	}


	public List<Object> createRange(Object start, Object end) {
		try{
			int s = ExpressionUtil.convertInteger(start);  //强制转换int
			int e = ExpressionUtil.convertInteger(end);    //强制转换int
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
				list.add(s);
			}
			return list;
		}catch(ScriptException e){
			return null;
		}	
	}

}
