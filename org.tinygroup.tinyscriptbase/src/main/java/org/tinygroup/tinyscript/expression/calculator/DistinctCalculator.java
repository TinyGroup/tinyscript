package org.tinygroup.tinyscript.expression.calculator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.tinygroup.tinyscript.ScriptException;

/**
 * 统计不重复元素的计算器
 * @author yancheng11334
 *
 */
public class DistinctCalculator extends CollectionNumberCalculator {

	public String getName() {
		return "distinct";
	}

	public Object computeItem(List<Object> numbers) throws ScriptException {
		Set<Object> sets = new HashSet<Object>();
		for(Object object:numbers){
		    if(object!=null){
		       sets.add(object);
		    }
		}
		return new ArrayList<Object>(sets);
	}

}
