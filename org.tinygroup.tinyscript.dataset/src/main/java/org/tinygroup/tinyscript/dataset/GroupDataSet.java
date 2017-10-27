package org.tinygroup.tinyscript.dataset;

import java.util.List;

import org.tinygroup.tinyscript.dataset.impl.AggregateResult;

/**
 * 分组数据集
 * 
 * @author yancheng11334
 *
 */
public abstract class GroupDataSet extends DynamicDataSet {

	/**
	 * 获取完整的分组数据
	 * 
	 * @return
	 */
	public abstract List<DynamicDataSet> getGroups();

	/**
	 * 创建聚合结果
	 * 
	 * @param aggregateName
	 */
	public abstract void createAggregateResult(String aggregateName);

	/**
	 * 获取聚合结果
	 * 
	 * @param row
	 * @param aggregateName
	 * @return
	 */
	public abstract <T> T getData(int row, String aggregateName);

	/**
	 * 设置聚合结果
	 * 
	 * @param row
	 * @param aggregateName
	 * @param value
	 */
	public abstract <T> void setData(int row, String aggregateName, T value);

	/**
	 * 拆分分组数据
	 * 
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	public abstract GroupDataSet subGroup(int beginIndex, int endIndex) throws Exception;

	/**
	 * 拆分分组数据
	 * 
	 * @param beginIndex
	 * @return
	 */
	public abstract GroupDataSet subGroup(int beginIndex) throws Exception;

	public abstract List<AggregateResult> getAggregateResultList() throws Exception;

	/**
	 * 合并分组数据，返回一般数据集
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract DataSet merge() throws Exception;

	@Override
	public String toString() {
		List<AggregateResult> list;
		List<DynamicDataSet> dataSets = getGroups();
		try {
			list = getAggregateResultList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		StringBuilder sb = new StringBuilder();
		sb.append("----------------------------------------\n");
		for (int k = 0; k < dataSets.size(); k++) {
			try {
				if(k == 0) {
					for (Field f : dataSets.get(k).getFields()) {
						sb.append(f.getName()).append(" ");
					}
					for (AggregateResult result : list) {
						sb.append(result.getName()).append(" ");
					}
					sb.append("\n");
				}		
				
				sb.append("----------------------------------------\n");
				for (int i = 0; i < dataSets.get(k).getRows(); i++) {
					for (int j = 0; j < dataSets.get(k).getColumns(); j++) {
						sb.append(dataSets.get(k).getData(getShowIndex(i), getShowIndex(j))).append(" ");
					}
					for (AggregateResult result : list) {
						sb.append(result.getData(k)).append(" ");
					}
					sb.append("\n");
				}
			} catch (Exception e) {
				// 可能会抛出异常
				throw new RuntimeException(e);
			}
		}
		sb.append("----------------------------------------\n");
		return sb.toString();
	}

}
