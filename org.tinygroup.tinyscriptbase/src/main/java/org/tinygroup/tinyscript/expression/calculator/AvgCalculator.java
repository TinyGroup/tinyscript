package org.tinygroup.tinyscript.expression.calculator;

import java.util.List;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * 平均数计算器
 * @author yancheng11334
 *
 */
public class AvgCalculator extends CollectionNumberCalculator{

	public String getName() {
		return "avg";
	}

	public Object computeItem(List<Object> numbers) throws ScriptException {
		double total = 0.0d;
		for(Object obj:numbers){
			total += ExpressionUtil.convertDouble(obj);
		}
		return total/numbers.size();
	}

}
