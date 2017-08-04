package org.tinygroup.tinyscript.expression.calculator;

import java.util.List;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * 总和计算器
 * @author yancheng11334
 *
 */
public class SumCalculator extends CollectionNumberCalculator {

	public String getName() {
		return "sum";
	}

	public Object computeItem(List<Object> numbers) throws ScriptException {
		if (numbers.size() == 1) {
			return numbers.get(0);
		} else {
			Object total = numbers.get(0);
			for (int i = 1; i < numbers.size(); i++) {
				total = ExpressionUtil.executeOperation("+", total,
						numbers.get(i));
			}
			return total;
		}
	}

}
