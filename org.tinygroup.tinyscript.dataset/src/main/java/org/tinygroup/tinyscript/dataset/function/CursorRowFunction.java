package org.tinygroup.tinyscript.dataset.function;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.dataset.DataSetRow;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;

/**
 * cursor函数
 * @author yancheng11334
 *
 */
public class CursorRowFunction extends AbstractVisitRowFunction{

	public String getNames() {
		return "cursorRow";
	}

	protected DataSetRow visit(DynamicDataSet dataSet, int index)
			throws ScriptException {
        try {
			return DataSetUtil.createDataSetRow(dataSet, dataSet.getCurrentRow()+index);
		} catch (Exception e) {
			throw new ScriptException(String.format("%s函数操作失败", getNames()),e);
		}
	}

	protected int getDefaultIndex() {
		return 0;
	}

}
