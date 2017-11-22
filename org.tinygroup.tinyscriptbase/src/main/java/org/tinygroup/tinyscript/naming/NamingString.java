package org.tinygroup.tinyscript.naming;

/**
 * 命名字符串
 * @author yancheng11334
 *
 */
public interface NamingString {
    
	/**
	 * 判断命名规则类型
	 * @return
	 */
	NamingStringEnum  getType();
	
	/**
	 * 转换指定类型的字符串
	 * @param type
	 * @return
	 */
	String convertTo(NamingStringEnum type);
}
