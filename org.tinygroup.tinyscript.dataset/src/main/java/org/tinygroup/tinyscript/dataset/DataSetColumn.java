package org.tinygroup.tinyscript.dataset;


/**
 * 数据集列对象
 * @author yancheng11334
 *
 */
public interface DataSetColumn {

	 /**
	  *  获得列字段名
	  * @return
	  */
	Field getField();
	 
	 /**
	  * 获得列下标
	  * @return
	  */
	 int getColumnNo();
	 
	 /**
	  * 获得记录数
	  * @return
	  */
	 int size() throws Exception ;
	 
	 /**
	  * 获得列值
	  * @param row
	  * @return
	  * @throws Exception
	  */
	 <T> T getData(int row) throws Exception;
	 
	 /**
	  * 更新列值
	  * @param row
	  * @param data
	  * @throws Exception
	  */
	 <T> void setData(int row,T data) throws Exception;
	 
	 /**
	  * 获得行数
	  * @return
	  * @throws Exception
	  */
	 int getRows() throws Exception;
	 
	    /**
		 * 元素下标是否从1开始
		 * <br>true表示从1开始,false表示从0开始
		 * @return
		 */
	  boolean isIndexFromOne();
}
