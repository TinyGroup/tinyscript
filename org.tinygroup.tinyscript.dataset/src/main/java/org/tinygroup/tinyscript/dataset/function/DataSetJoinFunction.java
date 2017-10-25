package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.dataset.AbstractDataSet;

/**
 * 内联函数
 * 
 * @author yancheng11334
 *
 */
public class DataSetJoinFunction extends AbstractJoinFunction {

	public String getNames() {
		return "join";
	}

	protected List<int[]> joinTwoDataSet(AbstractDataSet leftDs,
			AbstractDataSet rightDs, int leftColumn, int rightColumn)
			throws Exception {
		// 初始化关联存储结构
		List<int[]> joinList = new ArrayList<int[]>();
		Map<Object, Integer> values = new HashMap<Object, Integer>();
		
		// 遍历右数据集
		int rrow = rightDs.getRows();
		for (int row = 0; row < rrow; row++) {
			Object v = rightDs.getData(rightDs.getShowIndex(row), rightDs.getShowIndex(rightColumn));
			if (v != null) {
				values.put(v, row);
			}
		}

		// 遍历左数据集
		int lrow = leftDs.getRows();
		for (int row = 0; row < lrow; row++) {
			Object v = leftDs.getData(leftDs.getShowIndex(row), leftDs.getShowIndex(leftColumn));
			if (v != null) {
				Integer rightRow = values.get(v);
				if(rightRow!=null){
				   int[] join = new int[2];
				   join[0] = row;
				   join[1] = rightRow;
				   joinList.add(join);
				}
			}
		}
		return joinList;
	}

}
