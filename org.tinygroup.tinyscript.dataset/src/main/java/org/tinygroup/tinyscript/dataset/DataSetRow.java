package org.tinygroup.tinyscript.dataset;

import java.util.List;


/**
 * 数据集的行对象
 * @author yancheng11334
 *
 */
public interface DataSetRow {

	/**
	 * 返回行的字段信息
	 * @return
	 */
	List<Field> getFields();
	
	/**
	 * 获取行值
	 * @param col
	 * @return
	 * @throws Exception
	 */
	<T> T getData(int col) throws Exception;
	
	/**
	 * 获取行值
	 * @param colName
	 * @return
	 * @throws Exception
	 */
	<T> T getData(String colName) throws Exception;
	
	/**
	 * 更新行值
	 * @param col
	 * @param data
	 * @throws Exception
	 */
	<T> void setData(int col,T data) throws Exception;
	
	/**
	 * 更新行值
	 * @param colName
	 * @param data
	 * @throws Exception
	 */
	<T> void setData(String colName,T data) throws Exception;
	
	/**
	 * 元素下标是否从1开始
	 * <br>true表示从1开始,false表示从0开始
	 * @return
	 */
	boolean isIndexFromOne();
}
