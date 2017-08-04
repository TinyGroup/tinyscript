package org.tinygroup.tinyscript.expression.calculator;

import java.util.List;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * 间距(极差)计算器
 * 
 * @author yancheng11334
 * 
 */
public class RangeCalculator extends CollectionNumberCalculator {

	public String getName() {
		return "range";
	}

	@SuppressWarnings("rawtypes")
	public Object computeItem(List<Object> numbers) throws ScriptException {
		if (numbers.size() == 1) {
			return ExpressionUtil.executeOperation("-", numbers.get(0),
					numbers.get(0));
		} else {
			Comparable max = (Comparable) numbers.get(0);
			Comparable min = (Comparable) numbers.get(0);
			for (int i = 1; i < numbers.size(); i++) {
				Comparable item = (Comparable) numbers.get(i);
				if (ExpressionUtil.compareNumber(item, max) > 0) {
					max = item;
				}
				if (ExpressionUtil.compareNumber(item, min) < 0) {
					min = item;
				}
			}
			return ExpressionUtil.executeOperation("-", max, min);
		}
	}

}
