package org.tinygroup.tinyscript.impl;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptFlowManager;
import org.tinygroup.tinyscript.config.ScriptComponentConfig;
import org.tinygroup.tinyscript.config.ScriptComponentConfigs;
import org.tinygroup.tinyscript.config.ScriptFlowConfig;
import org.tinygroup.tinyscript.config.ScriptFlowConfigs;

/**
 * 默认的脚本流程管理器实现
 * @author yancheng11334
 *
 */
public class DefaultScriptFlowManager implements ScriptFlowManager{

	private  Map<String,ScriptComponentConfig> componentMap = new HashMap<String,ScriptComponentConfig>();
	
	private  Map<String,ScriptFlowConfig> flowMap = new HashMap<String,ScriptFlowConfig>();
	
	public void addScriptComponentConfigs(ScriptComponentConfigs configs) {
		if(configs!=null && configs.getComponentList()!=null){
		   for(ScriptComponentConfig config:configs.getComponentList()){
			   addScriptComponentConfig(config);
		   }
		}
	}

	public void removeScriptComponentConfigs(ScriptComponentConfigs configs) {
		if(configs!=null && configs.getComponentList()!=null){
		   for(ScriptComponentConfig config:configs.getComponentList()){
			   removeScriptComponentConfig(config);
		   }
		}
	}

	public void addScriptComponentConfig(ScriptComponentConfig config) {
        if(config!=null){
           componentMap.put(config.getId(), config);
        }
	}

	public void removeScriptComponentConfig(ScriptComponentConfig config) {
        if(config!=null){		
           componentMap.remove(config.getId());
        }
	}

	public ScriptComponentConfig getScriptComponentConfig(String componentId) {
		return componentMap.get(componentId);
	}

	public void addScriptFlowConfigs(ScriptFlowConfigs configs) {
		if(configs!=null && configs.getFlowList()!=null){
		   for(ScriptFlowConfig config:configs.getFlowList()){
			   addScriptFlowConfig(config);
		   }
		}
	}

	public void removeScriptFlowConfigs(ScriptFlowConfigs configs) {
        if(configs!=null && configs.getFlowList()!=null){
           for(ScriptFlowConfig config:configs.getFlowList()){
        	   removeScriptFlowConfig(config);
  		   }
		}
	}

	public void addScriptFlowConfig(ScriptFlowConfig config) {
		if(config!=null){
		   flowMap.put(config.getId(), config);
		}
		
	}

	public void removeScriptFlowConfig(ScriptFlowConfig config) {
		if(config!=null){
		   flowMap.remove(config.getId());
		}
	}

	public ScriptFlowConfig getScriptFlowConfig(String flowId) {
		return flowMap.get(flowId);
	}

}
