package org.tinygroup.tinyscript.expression.calculator;

import java.util.Collections;
import java.util.List;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

public class PercentileCalculator extends CollectionNumberCalculator {

	@Override
	public String getName() {
		return "percentile";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object computeItem(List<Object> numbers, Object... params) throws ScriptException {
		List list = (List) numbers;
		Collections.sort(list);
		double percent = Double.parseDouble(params[0] + "") * (numbers.size() - 1);
		int pos = (int) Math.floor(percent);
		if (pos - percent == 0) {
			return numbers.get(pos);
		}
		return (1 - (percent - pos)) * ExpressionUtil.convertDouble(numbers.get(pos))
				+ (percent - pos) * ExpressionUtil.convertDouble(numbers.get(pos + 1));
	}

}
