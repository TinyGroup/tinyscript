package org.tinygroup.tinyscript.expression.calculator;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.expression.NumberCalculator;

/**
 * 支持集合运算
 * @author yancheng11334
 *
 */
public abstract class CollectionNumberCalculator implements NumberCalculator{

	public List<Object> computeCollection(List<Object>...collections) throws ScriptException {
		List<Object> list = new ArrayList<Object>();
		return list;
	}
	
	public Object compute(List<Object> numbers) throws ScriptException {
		//判断当前序列是否包含集合元素
		if(ExpressionUtil.containsCollection(numbers)){
		   List<Object> list = new ArrayList<Object>();
		   //将序列拆分成序列组
		   List<List<Object>> newNumberLists = ExpressionUtil.splitCollection(numbers);
		   for(List<Object> newNumberList:newNumberLists){
			   //递归执行compute
			   list.add(compute(newNumberList));
		   }
		   return list;
		}else{
		   //不包含集合元素则执行具体的计算逻辑
		   return computeItem(numbers);
		}
	}
	
	/**
	 * 计算非集合元素
	 * @param numbers
	 * @return
	 * @throws ScriptException
	 */
	public abstract Object computeItem(List<Object> numbers) throws ScriptException ;
}
