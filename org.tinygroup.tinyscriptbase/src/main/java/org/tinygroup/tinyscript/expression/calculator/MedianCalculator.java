package org.tinygroup.tinyscript.expression.calculator;

import java.util.Collections;
import java.util.List;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * 中位数计算器
 * 
 * @author yancheng11334
 * 
 */
public class MedianCalculator extends CollectionNumberCalculator {

	public String getName() {
		return "median";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object computeItem(List<Object> numbers, Object... params) throws ScriptException {
		// 先对参数进行排序
		List list = (List) numbers;
		Collections.sort(list);
		if (list.size() % 2 == 0) {
			// 偶数，返回中间两数的平均值
			int m = list.size() / 2;
			double a1 = ExpressionUtil.convertDouble(list.get(m - 1));
			double a2 = ExpressionUtil.convertDouble(list.get(m));
			return (a1 + a2) / 2;
		} else {
			// 奇数，返回中间值
			int m = list.size() / 2;
			return list.get(m);
		}
	}

}
