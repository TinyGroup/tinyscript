package org.tinygroup.tinyscript.expression.calculator;

import java.util.List;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * 最大值计算器
 * @author yancheng11334
 *
 */
public class MaxCalculator extends CollectionNumberCalculator {

	public String getName() {
		return "max";
	}

	@SuppressWarnings("rawtypes")
	public Object computeItem(List<Object> numbers, Object... params) throws ScriptException {
		if (numbers.size() == 1) {
			return numbers.get(0);
		} else {
			Comparable max = (Comparable) numbers.get(0);
			for (int i = 1; i < numbers.size(); i++) {
				Comparable item = (Comparable) numbers.get(i);
				if (ExpressionUtil.compareNumber(item, max) > 0) {
					max = item;
				}
			}
			return max;
		}
	}

}
