package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;

/**
 * 右联函数
 * @author yancheng11334
 *
 */
public class DataSetRightJoinFunction extends AbstractJoinFunction {

	public String getNames() {
		return "rightjoin";
	}

	protected List<int[]> joinTwoDataSet(AbstractDataSet leftDs,
			AbstractDataSet rightDs, int[] leftColumns, int[] rightColumns)
			throws Exception {
		// 初始化关联存储结构
	    List<int[]> joinList = new ArrayList<int[]>();
	    Map<Object, Integer> values = new HashMap<Object, Integer>();
	    
	    // 遍历右数据集
	    int rrow = rightDs.getRows();
	    for (int row = 0; row < rrow; row++) {
	    	List<Object> v = rightDs.getDatas(rightDs.getShowIndex(row), rightColumns);
	 	    int[] join = new int[2];
	    	join[0] = -1;
	 		if (v != null) {
	 		   values.put(v, row);
	 		   join[1] = row;
	 		}else{
	 		   join[1] = -1;
	 		}
	 		joinList.add(join);
	 	}	
	    
	    // 遍历左数据集
	    int lrow = leftDs.getRows();
	    for (int row = 0; row < lrow; row++) {
	    	List<Object> v = leftDs.getDatas(leftDs.getShowIndex(row), leftColumns);
	    	if (v != null) {
	    	   Integer rnum = values.get(v);
	    	   if(rnum!=null){
	    		  int[] join = joinList.get(rnum); 
	    		  join[0] = row;
	    	   }
	    	}
	    }
	    
	    //删除空记录
	  	Iterator<int[]> it = joinList.iterator();
	  	while(it.hasNext()){
	  	   int[] join = it.next();
	  	   if(join[1]<0){
	  		  it.remove();
	  	   }
	  	}
	    
		return joinList;
	}

	protected List<int[]> joinTwoDataSet(AbstractDataSet leftDs,
			AbstractDataSet rightDs, LambdaFunction leftFunction,
			LambdaFunction rightFunction, ScriptContext context)
			throws Exception {
		// 初始化关联存储结构
	    List<int[]> joinList = new ArrayList<int[]>();
	    Map<Object, Integer> values = new HashMap<Object, Integer>();
	    
	    ScriptContext leftContext = new DefaultScriptContext();
		leftContext.setParent(context);
		ScriptContext rightContext = new DefaultScriptContext();
		rightContext.setParent(context);
		
	    // 遍历右数据集
	    int rrow = rightDs.getRows();
	    for (int row = 0; row < rrow; row++) {
	    	createRowContext(rightDs, rightContext, rightDs.getShowIndex(row));
			Object v = rightFunction.execute(rightContext).getResult();
	 	    int[] join = new int[2];
	    	join[0] = -1;
	 		if (v != null) {
	 		   values.put(v, row);
	 		   join[1] = row;
	 		}else{
	 		   join[1] = -1;
	 		}
	 		joinList.add(join);
	 	}	
	    
	    // 遍历左数据集
	    int lrow = leftDs.getRows();
	    for (int row = 0; row < lrow; row++) {
	    	createRowContext(leftDs, leftContext, leftDs.getShowIndex(row));
			Object v = leftFunction.execute(leftContext).getResult();
	    	if (v != null) {
	    	   Integer rnum = values.get(v);
	    	   if(rnum!=null){
	    		  int[] join = joinList.get(rnum); 
	    		  join[0] = row;
	    	   }
	    	}
	    }
	    
	    //删除空记录
	  	Iterator<int[]> it = joinList.iterator();
	  	while(it.hasNext()){
	  	   int[] join = it.next();
	  	   if(join[1]<0){
	  		  it.remove();
	  	   }
	  	}
	    
		return joinList;
	}

}
