package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;

/**
 * 全联函数
 * @author yancheng11334
 *
 */
public class DataSetFullJoinFunction extends AbstractJoinFunction {

	public String getNames() {
		return "joinFull";
	}

	protected List<int[]> joinTwoDataSet(AbstractDataSet leftDs,
			AbstractDataSet rightDs,  int[] leftColumns, int[] rightColumns)
			throws Exception {
		// 初始化关联存储结构
	    List<int[]> joinList = new ArrayList<int[]>();
	    List<int[]> rightList = new ArrayList<int[]>();
	    Map<Object, Integer> values = new HashMap<Object, Integer>();
	    
	    // 遍历左数据集
	    int lrow = leftDs.getRows();
	    for (int row = 0; row < lrow; row++) {
	    	List<Object> v = leftDs.getDatas(leftDs.getShowIndex(row), leftColumns);
	    	int[] join = new int[2];
	    	join[1] = -1;
			if (v != null) {
				values.put(v, row);
				join[0] = row;
			}else{
				join[0] = -1;
			}
			joinList.add(join);
	    }
	    
	    // 遍历右数据集
	    int rrow = rightDs.getRows();
		for (int row = 0; row < rrow; row++) {
			List<Object> v = rightDs.getDatas(rightDs.getShowIndex(row), rightColumns);
			if (v != null) {
				Integer lnum = values.get(v);
				if(lnum!=null){
					int[] join = joinList.get(lnum);
					join[1] = row;
				}else{	
					int[] join = new int[2];
					join[0] = -1;
					join[1] = row;
					rightList.add(join);
				}
			}
		}
		
	    //合并非匹配记录
	    joinList.addAll(rightList);
	    
		return joinList;
	}

	protected List<int[]> joinTwoDataSet(AbstractDataSet leftDs,
			AbstractDataSet rightDs, LambdaFunction leftFunction,
			LambdaFunction rightFunction, ScriptContext context)
			throws Exception {
		// 初始化关联存储结构
	    List<int[]> joinList = new ArrayList<int[]>();
	    List<int[]> rightList = new ArrayList<int[]>();
	    Map<Object, Integer> values = new HashMap<Object, Integer>();
	    
	    ScriptContext leftContext = new DefaultScriptContext();
		leftContext.setParent(context);
		ScriptContext rightContext = new DefaultScriptContext();
		rightContext.setParent(context);
		
	    // 遍历左数据集
	    int lrow = leftDs.getRows();
	    for (int row = 0; row < lrow; row++) {
	    	createRowContext(leftDs, leftContext, leftDs.getShowIndex(row));
			Object v = leftFunction.execute(leftContext).getResult();
	    	int[] join = new int[2];
	    	join[1] = -1;
			if (v != null) {
				values.put(v, row);
				join[0] = row;
			}else{
				join[0] = -1;
			}
			joinList.add(join);
	    }
	    
	    // 遍历右数据集
	    int rrow = rightDs.getRows();
		for (int row = 0; row < rrow; row++) {
			createRowContext(rightDs, rightContext, rightDs.getShowIndex(row));
			Object v = rightFunction.execute(rightContext).getResult();
			if (v != null) {
				Integer lnum = values.get(v);
				if(lnum!=null){
					int[] join = joinList.get(lnum);
					join[1] = row;
				}else{	
					int[] join = new int[2];
					join[0] = -1;
					join[1] = row;
					rightList.add(join);
				}
			}
		}
		
	    //合并非匹配记录
	    joinList.addAll(rightList);
	    
		return joinList;
	}

}
