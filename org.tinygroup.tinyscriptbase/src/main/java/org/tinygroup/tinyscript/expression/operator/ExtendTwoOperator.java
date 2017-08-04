package org.tinygroup.tinyscript.expression.operator;

/**
 * 简化扩展的二元操作符处理器
 * @author yancheng11334
 *
 */
public abstract class ExtendTwoOperator extends TwoOperator{

	public boolean isMatch(Object... parameter) {
		return checkParameter(parameter[0],parameter[1]);
	}
	
	protected abstract boolean checkParameter(Object left, Object right);
}
