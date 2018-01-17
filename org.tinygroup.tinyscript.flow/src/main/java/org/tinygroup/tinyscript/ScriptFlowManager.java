package org.tinygroup.tinyscript;

import org.tinygroup.tinyscript.config.ScriptComponentConfig;
import org.tinygroup.tinyscript.config.ScriptComponentConfigs;
import org.tinygroup.tinyscript.config.ScriptFlowConfig;
import org.tinygroup.tinyscript.config.ScriptFlowConfigs;

/**
 * 脚本流程管理器
 * @author yancheng11334
 *
 */
public interface ScriptFlowManager {

	void addScriptComponentConfigs(ScriptComponentConfigs configs);
	
	void removeScriptComponentConfigs(ScriptComponentConfigs configs);
	
    void addScriptComponentConfig(ScriptComponentConfig config);
	
	void removeScriptComponentConfig(ScriptComponentConfig config);
	
	ScriptComponentConfig getScriptComponentConfig(String componentId);
	
	void addScriptFlowConfigs(ScriptFlowConfigs configs);
	
	void removeScriptFlowConfigs(ScriptFlowConfigs configs);
	
    void addScriptFlowConfig(ScriptFlowConfig config);
	
	void removeScriptFlowConfig(ScriptFlowConfig config);
	
	ScriptFlowConfig getScriptFlowConfig(String flowId);
}
