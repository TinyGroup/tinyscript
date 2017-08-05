package org.tinygroup.tinyscript.dataset.impl;

import java.util.List;

import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DataSetColumn;
import org.tinygroup.tinyscript.dataset.Field;

/**
 * 默认的数据集列对象实现
 * @author yancheng11334
 *
 */
public class DefaultDataSetColumn implements DataSetColumn{

	private DataSet  dataSet;
	private int col;  //显示值
	
	public DefaultDataSetColumn(DataSet dataSet,int col){
		this.dataSet = dataSet;
		this.col = col;
	}
	
	public DefaultDataSetColumn(DataSet dataSet,String colName) throws Exception{
		this.dataSet = dataSet;
		List<Field> fields = dataSet.getFields();
		boolean tag = true;
		for(int i=0;i<fields.size();i++){
			Field field = fields.get(i);
			if(field.getName().equalsIgnoreCase(colName)){
			   if(dataSet.isIndexFromOne()){
				  col = i+1; 
			   }else{
				  col = i; 
			   }
			   tag = false;
			   break;
			}
		}
		if(tag){
		   throw new Exception(String.format("数据集不包含字段%s", colName));
		}
	}
	
	public Field getField() {
		return dataSet.isIndexFromOne()?dataSet.getFields().get(col-1):dataSet.getFields().get(col);
	}

	public int getColumnNo() {
		return col;
	}

	public int size() throws Exception {
		return dataSet.getRows();
	}

	public <T> T getData(int row) throws Exception {
		return dataSet.getData(row, col);
	}

	public <T> void setData(int row, T data) throws Exception {
		dataSet.setData(row, col, data);
	}

	public int getRows() throws Exception {
		return dataSet.getRows();
	}

	public boolean isIndexFromOne() {
		return dataSet.isIndexFromOne();
	}

}
