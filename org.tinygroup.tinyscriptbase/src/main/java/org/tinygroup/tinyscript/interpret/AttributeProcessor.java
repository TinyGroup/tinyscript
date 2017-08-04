package org.tinygroup.tinyscript.interpret;

/**
 * 属性处理器
 * @author yancheng11334
 *
 */
public interface AttributeProcessor {

	 /**
	  * 判断处理器是否匹配
	  * @param object
	  * @param name
	  * @return
	  */
	 boolean isMatch(Object object, Object name);
	 
	 /**
	  * 获得属性值
	  * @param object
	  * @param name
	  * @return
	  * @throws Exception
	  */
	 Object getAttribute(Object object, Object name) throws Exception;
}
