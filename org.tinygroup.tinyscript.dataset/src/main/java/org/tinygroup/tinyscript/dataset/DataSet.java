package org.tinygroup.tinyscript.dataset;

import java.util.List;

/**
 * 数据集，用来统一描述二维数据
 * Created by luoguo on 2014/7/4.
 */
public interface DataSet {
	
	/**
	 * 数据集名称
	 * @return
	 */
    String getName();

    /**
     * 返回字段字段定义
     *
     * @return
     */
    List<Field> getFields();

    /**
     * 返回是否只读
     *
     * @return
     */
    boolean isReadOnly();

    /**
     * 第一条记录
     */
    void first() throws Exception;

    /**
     * 前一条记录
     */
    boolean previous() throws Exception;

    /**
     * 到第一条之前
     */
    void beforeFirst() throws Exception;

    /**
     * 移动到最后一条记录之后
     */
    void afterLast() throws Exception;

    /**
     * 移到后一条记录
     */
    boolean next() throws Exception;

    /**
     * 移动到指定记录
     *
     * @param row
     */
    boolean absolute(int row) throws Exception;

    /**
     * 返回记录数
     *
     * @return
     */
    int getRows() throws Exception;

    /**
     * 返回列数
     *
     * @return
     */
    int getColumns() throws Exception;

    /**
     * 读取指定行，指定列的值
     * @param row
     * @param col
     * @return
     * @throws Exception
     */
    <T> T getData(int row, int col) throws Exception;
    
    /**
     * 设置指定行，指定列的值
     * @param row
     * @param col
     * @param data
     * @throws Exception
     */
    <T> void setData(int row, int col, T data) throws Exception;

    /**
     * 读取当前行，指定列的值
     * @param col
     * @return
     * @throws Exception
     */
    <T> T getData(int col) throws Exception;

    /**
     * 设置当前行，指定列的值
     * @param col
     * @param data
     * @throws Exception
     */
    <T> void setData(int col, T data) throws Exception;

    /**
     * 读取当前行，指定列的值
     * @param fieldName
     * @return
     * @throws Exception
     */
    <T> T getData(String fieldName) throws Exception;

    /**
     * 设置当前行，指定列的值
     * @param fieldName
     * @param data
     * @throws Exception
     */
    <T> void setData(String fieldName, T data) throws Exception;
    
    /**
	 * 元素下标是否从1开始
	 * <br>true表示从1开始,false表示从0开始
	 * @return
	 */
	boolean isIndexFromOne();
	
	/**
	 * 设置元素下标是否从1开始
	 * <br>true表示从1开始,false表示从0开始 
	 * @param tag
	 */
	void setIndexFromOne(boolean tag);
}
