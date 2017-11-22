package org.tinygroup.tinyscript.naming;

/**
 * 命名字符串枚举类
 * @author yancheng11334
 *
 */
public enum NamingStringEnum {
    /**
     * Java变量的命名规则，如“lowerCamel”
     */
	LOWER_CAMEL(1),
	/**
	 * 连字符连接变量的命名规则，如“lower-hyphen”
	 */
	LOWER_HYPHEN(2),
	/**
	 * C++变量命名规则，如“lower_underscore”
	 */
	LOWER_UNDERSCORE(3),
	/**
	 * Java和C++类的命名规则，如“UpperCamel”
	 */
	UPPER_CAMEL(4),
	/**
	 * Java和C++常量的命名规则，如“UPPER_UNDERSCORE”
	 */
	UPPER_UNDERSCORE(5),
	/**
	 * 未知的命名规则
	 */
	UNKNOWN(6);
	
	private int type;
	
	private NamingStringEnum(int type){
		this.type = type;
	}

	public int getType() {
		return type;
	}
	
}
