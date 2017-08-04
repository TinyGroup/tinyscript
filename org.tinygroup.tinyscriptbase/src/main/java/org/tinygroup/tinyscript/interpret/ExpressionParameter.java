package org.tinygroup.tinyscript.interpret;

import org.tinygroup.tinyscript.ScriptEngineOperator;

/**
 * 表达式参数(支持延迟计算)
 * @author yancheng11334
 *
 */
public interface ExpressionParameter extends ScriptEngineOperator{

	/**
	 * 获取表达式
	 * @return
	 */
	public String getExpression();
	
	/**
	 * 获取结果
	 * @return
	 * @throws Exception
	 */
	public Object eval() throws Exception;
	
	
}
