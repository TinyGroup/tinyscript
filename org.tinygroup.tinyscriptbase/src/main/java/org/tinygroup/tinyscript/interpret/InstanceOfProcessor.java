package org.tinygroup.tinyscript.interpret;

/**
 * InstanceOf指令扩展
 * @author yancheng11334
 *
 */
public interface InstanceOfProcessor {

	/**
	 * 是否匹配
	 * @param object
	 * @param type
	 * @return
	 */
	boolean isMatch(Object object,Object type);
	
	/**
	 * 判断是否实例
	 * @param object
	 * @param type
	 * @return
	 * @throws Exception
	 */
	boolean isInstance(Object object,Object type) throws Exception;
}
