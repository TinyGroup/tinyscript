package org.tinygroup.tinyscript.dataset;

import java.util.Comparator;


/**
 * 动态结构数据集
 * @author yancheng11334
 *
 */
public abstract class DynamicDataSet extends AbstractDataSet{

	/**
	 * 删除某列
	 * @param col
	 * @return
	 * @throws Exception
	 */
	public abstract DynamicDataSet  deleteColumn(int col) throws Exception;
	
	/**
	 * 删除某列
	 * @param colName
	 * @return
	 * @throws Exception
	 */
	public abstract DynamicDataSet  deleteColumn(String colName) throws Exception;
	
	/**
	 * 删除某行
	 * @param row
	 * @return
	 * @throws Exception
	 */
	public abstract DynamicDataSet  deleteRow(int row) throws Exception;
	
	/**
	 * 新建某列
	 * @param col
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public abstract DynamicDataSet  insertColumn(int col,Field field) throws Exception;
	
	/**
	 * 新建某列
	 * @param col
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public DynamicDataSet  insertColumn(int col,String fieldName) throws Exception{
		Field field = new Field(fieldName,fieldName,"Object");
		return insertColumn(col,field);
	}
	
	/**
	 * 新建某列
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public DynamicDataSet  insertColumn(String fieldName) throws Exception{
		int colNum = getColumns();
		int col = isIndexFromOne()?colNum:colNum-1;
		return insertColumn(col,fieldName);
	}
	
	
	/**
	 * 新建某行
	 * @param row
	 * @return
	 * @throws Exception
	 */
	public abstract DynamicDataSet  insertRow(int row) throws Exception;
	
	/**
	 * 比较器排序
	 * @param c
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public abstract DynamicDataSet  sort(Comparator c) throws Exception;
	
	/**
	 * 获取当前行
	 * @return
	 */
	public abstract int getCurrentRow() throws Exception;
}
