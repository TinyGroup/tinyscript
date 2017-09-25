package org.tinygroup.tinyscript.dataset.function;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.dataset.DataSetRow;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * first函数
 * 
 * @author yancheng11334
 *
 */
public class FirstRowFunction extends AbstractVisitRowFunction {

	public String getNames() {
		return "firstRow";
	}

	protected DataSetRow visit(DynamicDataSet dataSet, int index) throws ScriptException {
		try {
			return DataSetUtil.createDataSetRow(dataSet, dataSet.getShowIndex(index));
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	protected void checkIndex(int index) throws Exception {
		if (index < 0) {
			throw new ScriptException(
					ResourceBundleUtil.getResourceMessage("dataset", "dataset.row.outofindex", getNames(), index));
		}
	}

	protected int getDefaultIndex() {
		return 0;
	}

}
