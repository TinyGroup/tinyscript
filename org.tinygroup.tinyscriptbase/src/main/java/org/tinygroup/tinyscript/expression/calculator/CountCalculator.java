package org.tinygroup.tinyscript.expression.calculator;

import java.util.List;

import org.tinygroup.tinyscript.ScriptException;

/**
 * 非空个数计算器
 * @author yancheng11334
 *
 */
public class CountCalculator extends CollectionNumberCalculator {

	public String getName() {
		return "count";
	}

	public Object computeItem(List<Object> numbers) throws ScriptException {
		int num=0;
		for(Object object:numbers){
		   if(object!=null){
			  num++;
		   }
		}
		return num;
	}
	
	public Object getEmptyValue(){
		return 0;
	}

}
