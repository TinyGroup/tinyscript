package org.tinygroup.tinyscript.expression.calculator;

import java.util.List;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * 最小值计算器
 * @author yancheng11334
 *
 */
public class MinCalculator extends CollectionNumberCalculator {

	public String getName() {
		return "min";
	}

	@SuppressWarnings("rawtypes")
	public Object computeItem(List<Object> numbers) throws ScriptException {
		if (numbers.size() == 1) {
			return numbers.get(0);
		} else {
			Comparable min = (Comparable) numbers.get(0);
			for (int i = 1; i < numbers.size(); i++) {
				Comparable item = (Comparable) numbers.get(i);
				if (ExpressionUtil.compareNumber(item, min) < 0) {
					min = item;
				}
			}
			return min;
		}
	}

}
