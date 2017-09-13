package org.tinygroup.tinyscript.function.math;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 支持集合元素的数学函数抽象类
 * @author yancheng11334
 *
 */
public abstract class AbstractMathCollectionFunction extends AbstractScriptFunction{

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getMessage("function.parameter.empty", getNames()));
			}else if(parameters.length==getParameterCount()){
				return compute(parameters);
			}else {
				throw new ScriptException(ResourceBundleUtil.getMessage("function.parameter.error", getNames()));
			}
		}catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getMessage("function.run.error", getNames()),e);
		}
	}
	
	/**
	 * 得到参数个数
	 * @return
	 */
	protected abstract int getParameterCount();
	
	/**
	 * 获取集合参数的size
	 * @param parameters
	 * @return
	 */
	protected int getCollectionSize(Object... parameters){
		for(int i=0;i<parameters.length;i++){
		   if(ExpressionUtil.isCollection(parameters[i])){
			  List<Object> list = ExpressionUtil.convertCollection(parameters[i]);
			  return list.size();
		   }
		}
		return -1;
	}
	/**
	 * 递归执行集合元素
	 * @param parameters
	 * @return
	 * @throws ScriptException
	 */
	protected Object compute(Object... parameters) throws ScriptException {
		int size = getCollectionSize(parameters);
		if(size<0){
		   //执行具体数学运算逻辑
		   return computeItem(parameters);
		}else{
		   //递归执行
		   List<Object> result = new ArrayList<Object>();
		   FunctionParameter functionParameter = new FunctionParameter(parameters);
		   for(int i=0;i<size;i++){
			   result.add(compute(functionParameter.getNewParameters(i)));
		   }
		   return result;
		}
	}
	
	/**
	 * 计算具体逻辑
	 * @param parameters
	 * @return
	 */
	protected abstract Object computeItem(Object... parameters) throws ScriptException ;
	
	
	/**
	 * 参数包装类
	 * @author yancheng11334
	 *
	 */
	class FunctionParameter {
		Object[] array;
		public FunctionParameter(Object... parameters){
			array = new Object[parameters.length];
			for(int i=0;i<parameters.length;i++){
			    if(ExpressionUtil.isCollection(parameters[i])){
			       array[i] = ExpressionUtil.convertCollection(parameters[i]);
			    }else{
			       array[i] = parameters[i];
			    }
			}
		}
		
		@SuppressWarnings("rawtypes")
		private Object getValue(int i,int j){
		   if(array[i] instanceof List){
			  List list = (List) array[i];
			  return list.get(j);
		   }else{
			  //单个元素
			  return array[i];
		   }
		}
		
		public Object[] getNewParameters(int p){
			Object[] parameters = new Object[array.length];
			for(int i=0;i<array.length;i++){
				parameters[i] = getValue(i,p);
			}
			return parameters;
		}
	}

}
