package org.tinygroup.tinyscript.function;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.config.FunctionConfig;
import org.tinygroup.tinyscript.config.ScriptFunctionConfig;

/**
 * 动态名称函数(支持一组业务行为相同，但名称不同的函数)
 * @author yancheng11334
 *
 */
public abstract class DynamicNameScriptFunction extends AbstractScriptFunction{

	/**
	 * 没有固定名称,所以返回null
	 */
	public String getNames() {
		return null;
	}
	
	/**
	 * 动态函数是否包含某名称业务行为
	 * @param name
	 * @return
	 */
	public abstract boolean exsitFunctionName(String name);
	
	/**
	 * 返回动态函数的包含的业务信息
	 * @return
	 */
	public abstract List<String> getFunctionNames();
	
	public List<FunctionConfig> getFunctionConfigs(){
		//如果需要返回详细描述、字段等特征，需要的函数自行覆盖实现
		 List<FunctionConfig> configs = new ArrayList<FunctionConfig>();
		 List<String> names = getFunctionNames();
		 for(String name:names){
			 configs.add(new ScriptFunctionConfig(name)); 
		 }
		 return configs;
	}

}
