package org.tinygroup.tinyscript.dataset.objectitem;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DataSetRow;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.objectitem.ObjectItemProcessor;

/**
 * 下标方式访问数据集
 * @author yancheng11334
 *
 */
public class DataSetItemProcessor implements ObjectItemProcessor{

	public boolean isMatch(Object obj, Object... items) {
		return obj instanceof DataSet;
	}

	public Object process(ScriptContext context, Object obj, Object... items)
			throws Exception {
		AbstractDataSet abstractDataSet = (AbstractDataSet) obj;
		if(DataSetUtil.isField(items[0],context)){
			String str = (String)DataSetUtil.getValue(items[0],context);
			if(DataSetUtil.isExcelCell(str)){
				int[] cell = DataSetUtil.getExcelCell(str);
				return abstractDataSet.getData(abstractDataSet.getShowIndex(cell[1]), abstractDataSet.getShowIndex(cell[0]));
			}else{
				return DataSetUtil.createDataSetColumn(abstractDataSet, str);
			}
			
		}else{
			int row = (Integer)DataSetUtil.getValue(items[0],context);
			return DataSetUtil.createDataSetRow(abstractDataSet, row);
		}
	}

	public void assignValue(ScriptContext context, Object value, Object obj,
			Object... items) throws Exception {
		AbstractDataSet abstractDataSet = (AbstractDataSet) obj;
		if(value instanceof DataSetRow){
			//行赋值,针对一行记录进行赋值
			assignRowValue(context,(DataSetRow) value,abstractDataSet,items);
		}else{
			
			if(items.length==1){
				//按excel单元格下标方式赋值
				assignExcelCellValue(context,value,abstractDataSet,(String)items[0]);
			}else{
				//行列赋值,针对一行记录的某个字段进行赋值
				assignCellValue(context,value,abstractDataSet,items);
			}
			
		}
		
	}
	
	private void assignRowValue(ScriptContext context,DataSetRow dataSetRow, AbstractDataSet dataSet,
			Object... items) throws Exception {
		if(DataSetUtil.isField(items[0],context)){
			throw new ScriptException("DataSet对象下标格式错误:行数据不能给列对象赋值");
		}else{
			int row = (Integer)DataSetUtil.getValue(items[0],context);
			for(int i=0;i<dataSetRow.getFields().size();i++){
				int col = dataSet.getShowIndex(i);
				dataSet.setData(row, col, dataSetRow.getData(col));
			}
		}
	}
	
	private void assignExcelCellValue(ScriptContext context, Object value, AbstractDataSet dataSet,
			String item) throws Exception {
		String v = (String) DataSetUtil.getValue(item, context);
		if(!DataSetUtil.isExcelCell(v)){
			throw new ScriptException(String.format("不支持的DataSet对象下标格式:[%s]", v));
		}
		int[] cell = DataSetUtil.getExcelCell(v);
		int row = dataSet.getShowIndex(cell[1]);
		int col = dataSet.getShowIndex(cell[0]);
		dataSet.setData(row, col, value);
	}
	
	private void assignCellValue(ScriptContext context, Object value, AbstractDataSet dataSet,
			Object... items) throws Exception {
		boolean t1 = DataSetUtil.isField(items[0],context);
		boolean t2 = DataSetUtil.isField(items[1],context);
		
		if(!t1 && t2){
		   //dataSet[row][colName] 先行后列
			String colName = (String)DataSetUtil.getValue(items[1],context);
			int row = (Integer)DataSetUtil.getValue(items[0],context);
			dataSet.absolute(row);
			dataSet.setData(colName, value);
		}else if(!t1 && !t2){
		   //dataSet[row][col] 先行后列
		    int row = (Integer)DataSetUtil.getValue(items[0],context);
			int col = (Integer)DataSetUtil.getValue(items[1],context);
			dataSet.setData(row, col, value);
		}else{
			throw new ScriptException("DataSet对象下标格式错误");
		}
	}
	
	
}
