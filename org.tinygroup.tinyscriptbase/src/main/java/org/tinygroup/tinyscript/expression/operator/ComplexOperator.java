package org.tinygroup.tinyscript.expression.operator;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptCollectionModel;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.collection.CollectionModelUtil;
import org.tinygroup.tinyscript.expression.Operator;

/**
 * 复合的操作符处理器
 * @author yancheng11334
 *
 */
public final class ComplexOperator extends AbstractOperator{
    /**
     * 动态扩展的操作符处理器
     */
	private List<Operator> operatorList = new ArrayList<Operator>();
	
	/**
	 * 默认的操作符处理器
	 */
	private Operator defaultOperator; 
	
	public ComplexOperator(Operator operator){
		this.defaultOperator = operator;
	}
	
	public void addOperator(Operator operator){
//		if(!operatorList.contains(operator)){
//			operatorList.add(operator);
//		}
		for(Operator oldOperator:operatorList){
		   if(oldOperator.equals(operator) || oldOperator.getClass().isInstance(operator)){
			  return;
		   }
		}
		operatorList.add(operator);
	}
	
	public void removeOperator(Operator operator){
		operatorList.remove(operator);
	}
	
	public Object operation(Object... parameter) throws ScriptException {
		if(!operatorList.isEmpty()){
		   //优先执行扩展的操作符处理器
		   for(Operator operator:operatorList){
			   if(operator.isMatch(parameter)){
				  return operator.operation(parameter);
			   }
		   }
		}
		//集合模型执行逻辑(操作符处理器必须支持集合运算，起码返回值要是集合类型)
		if(defaultOperator.supportCollection() && parameter!=null && parameter.length>0){
			ScriptCollectionModel model = CollectionModelUtil.getScriptCollectionModel(parameter[0]);
			if(model!=null){
			   return model.executeOperator(getOperation(), parameter);
			}
		}
		//执行默认的操作符处理器
		return defaultOperator.operation(parameter);
	}

	public String getOperation() {
		return defaultOperator.getOperation();
	}

	public int getParameterCount() {
		return defaultOperator.getParameterCount();
	}

}
