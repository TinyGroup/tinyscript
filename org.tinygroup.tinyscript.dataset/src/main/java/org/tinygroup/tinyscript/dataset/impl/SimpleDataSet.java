package org.tinygroup.tinyscript.dataset.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.Field;


/**
 * Created by luoguo on 2014/7/4.
 */
public class SimpleDataSet extends DynamicDataSet implements Cloneable {
    private Object[][] dataArray;
    private int currentRow = -1;  //对应实际下标，而非显示下标

    public SimpleDataSet(List<Field> fields, Object[][] dataArray) {
        this.dataArray = dataArray;
        super.setFields(fields);
    }
    
    public SimpleDataSet(List<Field> fields, Object[][] dataArray,boolean tag) {
        this.dataArray = dataArray;
        super.setFields(fields);
        this.setIndexFromOne(tag);
    }
    
    public Object[][] getDataArray(){
    	return dataArray;
    }

    public boolean isReadOnly() {
        return false;
    }

    public void first() throws Exception {
        checkValidate();
        currentRow = 0;
    }

    private void checkValidate() throws Exception {
        if (dataArray != null && dataArray.length == 0) {
            throw new Exception("没有数据存在，无法移动到第一行！");
        }
    }

    public boolean previous() throws Exception {
        checkValidate();
        if (currentRow == 0) {
            throw new Exception("已经移动到第一行，无法继续前移！");
        }
        currentRow--;
        return true;
    }

    public void beforeFirst() throws Exception {
        checkValidate();
        currentRow = -1;
    }


    public void afterLast() throws Exception {
        checkValidate();
        currentRow = dataArray.length;
    }

    public boolean next() throws Exception {
        checkValidate();
        if (currentRow == dataArray.length - 1) {
            return false;
        }
        currentRow++;
        return true;
    }

    public boolean absolute(int row) throws Exception {
        checkValidate();
        if(isIndexFromOne()){
        	if (row < 1 || row >= dataArray.length+1) {
        		throw new Exception(String.format("指定的行数%s不合法或越界!", row));
        	}
        }else{
        	 if (row < 0 || row >= dataArray.length) {
        		 throw new Exception(String.format("指定的行数%s不合法或越界!", row));
        	 }
        }
        currentRow = getActualIndex(row);
        return true;
    }

    public int getRows() throws Exception {
        return dataArray==null?0:dataArray.length;
    }

    public int getColumns() {
        return getFields().size();
    }

    @SuppressWarnings("unchecked")
	public <T> T getData(int row, int col) {
    	row = getActualIndex(row);
		col = getActualIndex(col);
        return (T) dataArray[row][col];
    }

    public <T> void setData(int row, int col, T newData) throws Exception {
    	row = getActualIndex(row);
		col = getActualIndex(col);
        dataArray[row][col] = newData;
    }

	public <T> T getData(int col) {
        return getData(getShowIndex(currentRow), col);
    }

    public <T> void setData(int col, T data) throws Exception {
        setData(getShowIndex(currentRow), col, data);
    }

    public void clean() {
        super.clean();
        dataArray = null;
    }

	public DynamicDataSet deleteColumn(int col) throws Exception {
		int colNum = getColumns();
		if(isIndexFromOne()){
		   if(col<1 || col>= colNum+1){
			 throw new 	Exception(String.format("删除第%s列数据越界.", col));
		   }
		}else{
		   if(col<0 || col>= colNum){
			 throw new 	Exception(String.format("删除第%s列数据越界.", col));
		   }
		}
		
		col = getActualIndex(col);
		//删除列数据
		for(int i=0;i<dataArray.length;i++){
			List<Object> list = new ArrayList<Object>();
			for(Object obj:dataArray[i]){
				list.add(obj);
			}
			list.remove(col);
			dataArray[i] = list.toArray();
		}
		//删除字段
		fields.remove(col);
		setFields(fields);
		return this;
	}

	public DynamicDataSet deleteColumn(String colName) throws Exception {
		Integer index = getColumn(colName);
    	if(index==null){
    	   throw new Exception(String.format("本数据集没有找到字段%s", colName));
    	}
		deleteColumn(getShowIndex(index));
		return this;
	}

	public DynamicDataSet deleteRow(int row) throws Exception {
		int rowNum = getRows();
		if(isIndexFromOne()){
		   if(row<1 || row>=rowNum+1){
		      throw new Exception(String.format("删除第%s行数据越界.", row));
		   }
		}else{
		   if(row<0 || row>=rowNum){
			  throw new Exception(String.format("删除第%s行数据越界.", row));
		   }
		}
		row = getActualIndex(row);
		
		//删除行数据
		Object[][] newArray = new Object[rowNum-1][];
		int k=-1;
		for(int i=0;i<newArray.length;i++){
			if(i==row){
			   k = k+2;
			}else{
			   k++;
			}
			newArray[i] = dataArray[k];
		}
		dataArray = newArray;
		//调整指针
		if(currentRow>=row){
			currentRow--;
		}
		return this;
	}

	public DynamicDataSet insertColumn(int col, Field field) throws Exception {
		int colNum = getColumns();
		if(isIndexFromOne()){
		   if(col<1 || col>= colNum+1){
			  throw new  Exception(String.format("插入第%s列数据越界.", col));
		   }
		}else{
		   if(col<0 || col>= colNum){
		      throw new  Exception(String.format("插入第%s列数据越界.", col));
		   }
		}
			
		col = getActualIndex(col);
		
		//插入列数据
		for(int i=0;i<dataArray.length;i++){
			List<Object> list = new ArrayList<Object>();
			for(Object obj:dataArray[i]){
				list.add(obj);
			}
			list.add(col, null);
			dataArray[i] = list.toArray();
		}
		//插入字段
		fields.add(col, field);
		setFields(fields);
		return this;
	}

	public DynamicDataSet insertRow(int row) throws Exception {
		int rowNum = getRows();
		
		if(isIndexFromOne()){
		   if(row<1 || row>rowNum+1){
			  throw new Exception(String.format("插入第%s行数据越界.", row));
		   }
		}else{
		   if(row<0 || row>rowNum){
			  throw new Exception(String.format("插入第%s行数据越界.", row));
		   }
		}
		
		row = getActualIndex(row);
		//插入行数据
		Object[][] newArray = new Object[rowNum+1][];
		if(row==rowNum){
			for(int i=0;i<row;i++){
				 newArray[i] = dataArray[i];
			}
			newArray[row] = new Object[getColumns()];
		}else{
			int k=-1;
			for(int i=0;i<newArray.length;i++){
				if(i!=row){
				   k++;
				   newArray[i] = dataArray[k];
				}else{
				   newArray[i] = new Object[getColumns()];
				}
			}
		}
		
		dataArray = newArray;
		//调整指针
		if(currentRow>=row){
		   currentRow++;
		}
		return this;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DynamicDataSet sort(Comparator c) throws Exception {
		Arrays.sort(dataArray, c);
		return this;
	}

	public int getCurrentRow() {
		return getShowIndex(currentRow);
	}
	
	public DataSet cloneDataSet() throws CloneNotSupportedException{
    	return clone();
    }
	
	public DataSet clone() throws CloneNotSupportedException{
		SimpleDataSet dataSet = (SimpleDataSet) super.clone();
		List<Field> newFields = new ArrayList<Field>();
		for(Field field:getFields()){
			newFields.add(field);
		}
		dataSet.setFields(newFields);
		dataSet.dataArray = new Object[dataArray.length][];
		for(int i=0;i< dataArray.length;i++){
			dataSet.dataArray[i] = new Object[dataArray[i].length];
			for(int j=0;j<dataArray[i].length;j++){
				dataSet.dataArray[i][j] = dataArray[i][j];
			}
		}
    	return dataSet;
    }

}
