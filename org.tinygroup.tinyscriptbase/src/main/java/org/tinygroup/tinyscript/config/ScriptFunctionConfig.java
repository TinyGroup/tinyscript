package org.tinygroup.tinyscript.config;

import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * ScriptFunction定义项(暂不支持参数、结果定义项)
 * @author yancheng11334
 *
 */
public class ScriptFunctionConfig extends AbstractFunctionConfig{
    
	private String resourceName;
	private String code;
	
	/**
	 * 仅定义函数名和函数类型
	 * @param name
	 */
	public ScriptFunctionConfig(String name){
		super(name,"script");
	}
	
	/**
	 * 定义函数名、函数类型和国际化描述
	 * @param name
	 * @param resourceName
	 * @param code
	 */
	public ScriptFunctionConfig(String name,String resourceName,String code){
		super(name,"script");
		this.resourceName = resourceName;
		this.code = code;
	}
	
	/**
	 * 获取国际化的函数描述信息
	 */
	public String getDescription() {
		if(code!=null){
		   if(resourceName!=null){
			  return ResourceBundleUtil.getResourceMessage(resourceName, code);
		   }else{
			  return ResourceBundleUtil.getDefaultMessage(code);
		   }
		}
		return null;
	}
	
}
