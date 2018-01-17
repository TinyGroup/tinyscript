package org.tinygroup.tinyscript;

import java.util.List;
import java.util.Map;

/**
 * 脚本流程执行器
 * @author yancheng11334
 *
 */
public interface ScriptFlowExecutor extends ScriptEngineOperator{

	/**
	 * 获取脚本流程管理器
	 * @return
	 */
	ScriptFlowManager getScriptFlowManager();
	
	/**
	 * 执行流程
	 * @param flowId
	 * @param context
	 * @return
	 * @throws Exception
	 */
	Object executeFlow(String flowId,ScriptContext context) throws Exception;
	
	/**
	 * 执行流程
	 * @param flowId
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Object executeFlow(String flowId,Map<String,Object> map) throws Exception;
	
	/**
	 * 执行流程
	 * @param flowId
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	Object executeFlow(String flowId,List<Object> parameters) throws Exception;
	
	/**
	 * 执行组件
	 * @param componentId
	 * @param context
	 * @return
	 * @throws Exception
	 */
    Object executeComponent(String componentId,ScriptContext context) throws Exception;
	
    /**
     * 执行组件
     * @param componentId
     * @param map
     * @return
     * @throws Exception
     */
	Object executeComponent(String componentId,Map<String,Object> map) throws Exception;
	
}
