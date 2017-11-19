package org.tinygroup.tinyscript.dataset.in;

import org.tinygroup.tinyscript.dataset.DataSetColumn;
import org.tinygroup.tinyscript.expression.InExpressionProcessor;

public class DataSetColumnInProcessor implements InExpressionProcessor {

	@Override
	public boolean isMatch(Object collection) throws Exception {
		return collection instanceof DataSetColumn;
	}

	@Override
	public boolean checkIn(Object collection, Object item) throws Exception {
		DataSetColumn colData = (DataSetColumn) collection;
		for (int i = 0; i < colData.getRows(); i++) {
			if (item == colData.getData(colData.isIndexFromOne() ? i + 1 : i)
					|| item.equals(colData.getData(colData.isIndexFromOne() ? i + 1 : i))) {
				return true;
			}
		}
		return false;
	}

}
