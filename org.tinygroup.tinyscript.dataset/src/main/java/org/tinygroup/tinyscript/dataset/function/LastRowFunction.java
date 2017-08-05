package org.tinygroup.tinyscript.dataset.function;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.dataset.DataSetRow;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;

/**
 * last函数
 * @author yancheng11334
 *
 */
public class LastRowFunction extends AbstractVisitRowFunction{

	public String getNames() {
		return "lastRow";
	}

	protected DataSetRow visit(DynamicDataSet dataSet, int index)
			throws ScriptException {
		try {
			return DataSetUtil.createDataSetRow(dataSet, dataSet.getShowIndex(dataSet.getRows()-1+index));
		} catch (Exception e) {
			throw new ScriptException(String.format("%s函数操作失败", getNames()),e);
		}
	}
	
	protected void checkIndex(int index) throws Exception{
		if(index>0){
		   throw new ScriptException(String.format("%s函数操作下标不能是正整数", getNames()));
		}
	}

	protected int getDefaultIndex() {
		return 0;
	}

}
