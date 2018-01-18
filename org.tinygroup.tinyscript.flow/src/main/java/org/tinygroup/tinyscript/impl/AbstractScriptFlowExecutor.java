package org.tinygroup.tinyscript.impl;

import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptEngineFactory;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptFlowExecutor;
import org.tinygroup.tinyscript.ScriptFlowManager;

/**
 * 抽象脚本流程执行器
 * @author yancheng11334
 *
 */
public abstract class AbstractScriptFlowExecutor implements ScriptFlowExecutor{

	private ScriptEngine scriptEngine;
	private ScriptFlowManager scriptFlowManager;
	private boolean initTag = false;
	
	protected static final String FLOW_INPUT_NAME = "$flowInput";
	
	public void setScriptEngine(ScriptEngine engine) {
		this.scriptEngine = engine;
	}

	public ScriptEngine getScriptEngine() {
		try {
		   if(scriptEngine==null){
			  scriptEngine = ScriptEngineFactory.createByBean();
			  initTag =true; 
		   }else if(!initTag){
			  ScriptEngineFactory.register(scriptEngine);
			  initTag =true; 
		   }
			 
		} catch (ScriptException e) {
			 throw new RuntimeException(e);
		}
		return scriptEngine;
	}

	public ScriptFlowManager getScriptFlowManager() {
		return scriptFlowManager;
	}

	public void setScriptFlowManager(ScriptFlowManager scriptFlowManager) {
		this.scriptFlowManager = scriptFlowManager;
	}

	public Object executeFlow(String flowId, Map<String, Object> map)
			throws Exception {
		return executeFlow(flowId,new DefaultScriptContext(map));
	}
	
	public Object executeFlow(String flowId, List<Object> parameters)
			throws Exception {
		ScriptContext context = new DefaultScriptContext();
		context.put(FLOW_INPUT_NAME, parameters);
		return executeFlow(flowId,context);
	}


	public Object executeComponent(String componentId, Map<String, Object> map)
			throws Exception {
		return executeComponent(componentId,new DefaultScriptContext(map));
	}

}
