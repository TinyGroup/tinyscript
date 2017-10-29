package org.tinygroup.tinyscript.expression.calculator;

import java.util.List;

import org.tinygroup.tinyscript.ScriptException;

public class CountAllCalculator extends CollectionNumberCalculator{

	@Override
	public String getName() {
		return "countAll";
	}

	@Override
	public Object computeItem(List<Object> numbers, Object... param) throws ScriptException {
		return numbers.size();
	}

}
