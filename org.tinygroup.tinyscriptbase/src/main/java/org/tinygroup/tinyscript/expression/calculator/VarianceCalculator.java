package org.tinygroup.tinyscript.expression.calculator;

import java.util.List;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * 方差计算器
 * 
 * @author yancheng11334
 * 
 */
public class VarianceCalculator extends CollectionNumberCalculator {

	public String getName() {
		return "varp";
	}

	public Object computeItem(List<Object> numbers) throws ScriptException {
		if (numbers.size() == 1) {
			return 0d;
		} else {
			double total = 0.0d;
			for (Object obj : numbers) {
				total += ExpressionUtil.convertDouble(obj);
			}
			double avg = total / numbers.size();
			total = 0.0d;
			for (Object obj : numbers) {
				double temp = ExpressionUtil.convertDouble(obj) - avg;
				total += temp * temp;
			}
			return total / numbers.size();
		}
	}

}
