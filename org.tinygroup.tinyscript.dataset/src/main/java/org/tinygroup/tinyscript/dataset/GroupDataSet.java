package org.tinygroup.tinyscript.dataset;

import java.util.List;

import org.tinygroup.tinyscript.dataset.impl.AggregateResult;

/**
 * 分组数据集
 * @author yancheng11334
 *
 */
public abstract class GroupDataSet extends DynamicDataSet{

	/**
	 * 获取完整的分组数据
	 * @return
	 */
	public abstract List<DynamicDataSet> getGroups();
	
	/**
	 * 创建聚合结果
	 * @param aggregateName
	 */
	public abstract void createAggregateResult(String aggregateName);
	
	/**
	 * 获取聚合结果
	 * @param row
	 * @param aggregateName
	 * @return
	 */
	public abstract <T> T getData(int row,String aggregateName);
	
	/**
	 * 设置聚合结果
	 * @param row
	 * @param aggregateName
	 * @param value
	 */
	public abstract <T> void setData(int row,String aggregateName,T value);
	
	/**
	 * 拆分分组数据
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	public abstract GroupDataSet subGroup(int beginIndex,int endIndex) throws Exception;
	
	/**
	 * 拆分分组数据
	 * @param beginIndex
	 * @return
	 */
	public abstract GroupDataSet subGroup(int beginIndex) throws Exception;
	
	
	public abstract List<AggregateResult> getAggregateResultList() throws Exception;
	
	/**
	 * 合并分组数据，返回一般数据集
	 * @return
	 * @throws Exception
	 */
	public abstract DataSet merge() throws Exception;
	
}
