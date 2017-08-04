package org.tinygroup.tinyscript.expression.calculator;

import java.util.List;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * 标准差(均方差)计算器
 * 
 * @author yancheng11334
 * 
 */
public class StandardDeviationCalculator extends CollectionNumberCalculator {

	public String getName() {
		return "stdevp";
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
			return Math.sqrt(total / numbers.size());
		}
	}

}
