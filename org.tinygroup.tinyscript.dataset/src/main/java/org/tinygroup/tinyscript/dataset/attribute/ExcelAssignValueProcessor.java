package org.tinygroup.tinyscript.dataset.attribute;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.assignvalue.AssignValueProcessor;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;

public class ExcelAssignValueProcessor implements AssignValueProcessor{

	public boolean isMatch(String name, ScriptContext context) {
		String[] names = name.split("\\.");
		if(names!=null && names.length>=2){
		   Object d = context.get(names[0]);
		   if(d==null || !(d instanceof DataSet)){
			  return false;
		   }
		   return DataSetUtil.isExcelCell(names[1]);
		}
		return false;
	}


	public void process(String name, Object value, ScriptContext context)
			throws Exception {
		String[] names = name.split("\\.");
		AbstractDataSet dataSet = context.get(names[0]);
		int[] cell = DataSetUtil.getExcelCell(names[1]);
		dataSet.setData(dataSet.getShowIndex(cell[1]), dataSet.getShowIndex(cell[0]), value);
	}

}
