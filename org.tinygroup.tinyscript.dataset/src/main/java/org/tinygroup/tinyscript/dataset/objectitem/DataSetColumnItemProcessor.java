package org.tinygroup.tinyscript.dataset.objectitem;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.dataset.DataSetColumn;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;
import org.tinygroup.tinyscript.objectitem.ObjectSingleItemProcessor;

/**
 * 下标方式访问数据集列对象
 * @author yancheng11334
 *
 */
public class DataSetColumnItemProcessor extends ObjectSingleItemProcessor{

	protected boolean isMatch(Object obj, Object item) {
		return obj instanceof DataSetColumn;
	}

	protected Object process(ScriptContext context, Object obj, Object item)
			throws Exception {
		DataSetColumn dataSetColumn = (DataSetColumn) obj;
		int row =   (Integer)DataSetUtil.getValue(item,context);
		Integer currentRow = context.get("$currentRow");
		if(currentRow!=null){
			int newRow = currentRow.intValue()+row;
			ScriptEngine engine = ScriptContextUtil.getScriptEngine(context);
			if(engine.isIndexFromOne()){
				//偏移的情况下，越界返回null
				if(newRow<1 || newRow > dataSetColumn.getRows()){
				   return null;
				}
			}else{
				//偏移的情况下，越界返回null
				if(newRow<0 || newRow > dataSetColumn.getRows()-1){
				   return null;
				}
			}
			return dataSetColumn.getData(newRow); //这里的row就表示偏移
		}else{
			return dataSetColumn.getData(row);
		}
		
	}

	protected void assignValue(ScriptContext context, Object value, Object obj,
			Object item) throws Exception {
		DataSetColumn dataSetColumn = (DataSetColumn) obj;
		int row =   (Integer)DataSetUtil.getValue(item,context);
		dataSetColumn.setData(row, value);
	}

}
