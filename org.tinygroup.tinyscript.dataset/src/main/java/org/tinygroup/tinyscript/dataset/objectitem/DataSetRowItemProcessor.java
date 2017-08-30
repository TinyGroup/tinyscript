package org.tinygroup.tinyscript.dataset.objectitem;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.dataset.DataSetRow;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.objectitem.ObjectSingleItemProcessor;

/**
 * 下标方式访问行对象
 * @author yancheng11334
 *
 */
public class DataSetRowItemProcessor extends ObjectSingleItemProcessor{

	protected boolean isMatch(Object obj, Object item) {
		return obj instanceof DataSetRow;
	}

	protected Object process(ScriptContext context, Object obj, Object item)
			throws Exception {
		DataSetRow dataSetRow = (DataSetRow) obj;
		if(DataSetUtil.isField(item,context)){
			if(item instanceof String && DataSetUtil.getFieldIndex(dataSetRow.getFields(), (String)item)>-1){
				String colName = (String)item;
				return dataSetRow.getData(colName);
			}else{
				String colName = (String)DataSetUtil.getValue(item,context);
				return dataSetRow.getData(colName);
			}
		}else{
			int col = (Integer)DataSetUtil.getValue(item,context);
			return dataSetRow.getData(col);
		}
	}

	protected void assignValue(ScriptContext context, Object value, Object obj,
			Object item) throws Exception {
		DataSetRow dataSetRow = (DataSetRow) obj;
		if(DataSetUtil.isField(item,context)){
			String colName = (String)DataSetUtil.getValue(item,context);
		    dataSetRow.setData(colName, value);
		}else{
			int col = (Integer)DataSetUtil.getValue(item,context);
			dataSetRow.setData(col,value);
		}
	}

}
