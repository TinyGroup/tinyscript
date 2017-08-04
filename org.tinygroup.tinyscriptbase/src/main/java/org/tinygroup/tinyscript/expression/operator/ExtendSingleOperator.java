package org.tinygroup.tinyscript.expression.operator;

/**
 * 简化扩展的一元操作符处理器
 * @author yancheng11334
 *
 */
public abstract class ExtendSingleOperator extends SingleOperator{

	public boolean isMatch(Object... parameter) {
		return checkParameter(parameter[0]);
	}
	
	protected abstract boolean checkParameter(Object var);
}
