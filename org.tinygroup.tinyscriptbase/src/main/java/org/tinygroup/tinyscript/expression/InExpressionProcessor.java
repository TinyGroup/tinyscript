package org.tinygroup.tinyscript.expression;

public interface InExpressionProcessor {
	
	/**
	 * 判断处理器是否匹配
	 * @param collection
	 * @return
	 * @throws Exception
	 */
	public boolean isMatch(Object collection) throws Exception;

	/**
	 * 执行in的逻辑
	 * @param collection
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public boolean checkIn(Object collection, Object item) throws Exception;
}
