package org.tinygroup.tinyscript.config;

import java.util.List;

/**
 * 函数方法的定义项
 * @author yancheng11334
 *
 */
public interface FunctionConfig extends Comparable<FunctionConfig>{
    
	/**
	 * 获取函数名
	 * @return
	 */
	String getName();
	
	/**
	 * 获取函数描述
	 * @return
	 */
	String getDescription();
	
	/**
	 * 获取函数类型
	 * @return
	 */
	String getFunctionType();
	
    /**
     * 获取参数定义项
     * @return
     */
	List<ParameterConfig> getParameters();
	
	/**
	 * 获取结果定义项
	 * @return
	 */
	ResultConfig getResult();
}
