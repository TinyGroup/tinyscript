package org.tinygroup.tinyscript.dataset.function;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.dataset.DataSetRow;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;

/**
 * preview函数
 * @author yancheng11334
 *
 */
public class PreviewRowFunction extends AbstractVisitRowFunction{

	public String getNames() {
		return "previewRow";
	}

	protected DataSetRow visit(DynamicDataSet dataSet, int index)
			throws ScriptException {
		try {
			return DataSetUtil.createDataSetRow(dataSet, dataSet.getCurrentRow()-index);
		} catch (Exception e) {
			throw new ScriptException(String.format("%s函数操作失败", getNames()),e);
		}
	}
	
	protected void checkIndex(int index) throws Exception{
		if(index<1){
		   throw new ScriptException(String.format("%s函数操作下标是正整数", getNames()));
		}
	}

	protected int getDefaultIndex() {
		return 1;
	}

}
