package org.tinygroup.tinyscript.dataset.attribute;

import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.interpret.AttributeProcessor;

public class ExcelAttributeProcessor implements AttributeProcessor{

	public boolean isMatch(Object object, Object name) {
		return object instanceof DataSet && DataSetUtil.isExcelCell((String)name);
	}

	public Object getAttribute(Object object, Object name) throws Exception {
		AbstractDataSet dataSet = (AbstractDataSet) object;
		int[] cell = DataSetUtil.getExcelCell((String)name);
		return dataSet.getData(dataSet.getShowIndex(cell[1]), dataSet.getShowIndex(cell[0]));
	}

}
