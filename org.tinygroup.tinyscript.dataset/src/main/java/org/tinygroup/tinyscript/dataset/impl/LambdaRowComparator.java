package org.tinygroup.tinyscript.dataset.impl;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.dataset.DataSetRow;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.RowComparator;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;

public class LambdaRowComparator implements RowComparator {

	private LambdaFunction compareFunction;
	private ScriptContext context;

	public LambdaRowComparator(LambdaFunction compareFunction, ScriptContext context) {
		this.compareFunction = compareFunction;
		this.context = context;
	}

	@Override
	public boolean isEqual(DataSetRow o1, DataSetRow o2) {
		try {
			String result1 = compareFunction.execute(createContext(o1)).getResult().toString();
			String result2 = compareFunction.execute(createContext(o2)).getResult().toString();
			return result1.equals(result2);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public int countHash(DataSetRow row) {
		try {
			String result = compareFunction.execute(createContext(row)).getResult().toString();
			return result.hashCode();
		} catch (Exception e) {
			return 0;
		}

	}

	private ScriptContext createContext(DataSetRow row) {
		ScriptContext sub = new DefaultScriptContext();
		sub.setParent(context);
		for (Field field : row.getFields()) {
			try {
				sub.put(field.getName(), row.getData(field.getName()));
			} catch (Exception e) {
			}
		}
		return sub;
	}

}
