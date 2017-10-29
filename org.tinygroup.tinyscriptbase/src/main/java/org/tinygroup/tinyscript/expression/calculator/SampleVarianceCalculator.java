package org.tinygroup.tinyscript.expression.calculator;

import java.util.List;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

public class SampleVarianceCalculator extends CollectionNumberCalculator {
	public String getName() {
		return "sampVarp";
	}

	public Object computeItem(List<Object> numbers, Object... params) throws ScriptException {
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
			return total / (numbers.size() - 1);
		}
	}

}
