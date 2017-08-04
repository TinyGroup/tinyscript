package org.tinygroup.tinyscript.impl;


import java.util.HashMap;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptUtil;

/**
 * 抽象的tiny脚本引擎
 * 
 * @author yancheng11334
 * 
 */
public abstract class AbstractScriptEngine implements ScriptEngine {

	private String defaultEncode;
	private boolean enableCache = true;
	private boolean indexFromOne = false;
	private Map<Object, ScriptSegment> segmentCaches = new HashMap<Object, ScriptSegment>();
	private ScriptInterpret scriptInterpret;
	private ScriptContext scriptContext;
	
	public AbstractScriptEngine(){
		scriptContext = new DefaultScriptContext();
	}
	
	public AbstractScriptEngine(Map<?,?> map){
		scriptContext = new DefaultScriptContext(map);
	}

	public String getEncode() {
		return defaultEncode == null ? "utf-8" : defaultEncode;
	}

	public void setEncode(String encode) {
		this.defaultEncode = encode;
	}

	public boolean isEnableCache() {
		return enableCache;
	}

	public void setEnableCache(boolean tag) {
		this.enableCache = tag;
	}
	
	public boolean isIndexFromOne() {
		return indexFromOne;
	}

	public void setIndexFromOne(boolean tag) {
		this.indexFromOne = tag;
	}
	
	public void addScriptSegment(ScriptSegment segment){
		segmentCaches.put(segment.getSegmentId(), segment);
	}
	
	public void removeScriptSegment(ScriptSegment segment){
		segmentCaches.remove(segment.getSegmentId());
	}
	
	public ScriptSegment getScriptSegment(String segmentId){
		return segmentCaches.get(segmentId);
	}
	
	public ScriptContext getScriptContext(){
		return scriptContext;
	}

	public Object execute(ScriptSegment segment, ScriptContext context)
			throws ScriptException {
		if(context.getParent()==null && !context.equals(scriptContext)){
		   //关联引擎上下文
		   context.setParent(scriptContext);  
		}
		return segment.execute(context);
	}

	public Object execute(ScriptSegment segment) throws ScriptException {
		ScriptContext runContext = new DefaultScriptContext(); 
		runContext.setParent(scriptContext);
		return execute(segment,runContext);
	}

	public Object execute(String script, ScriptContext context)
			throws ScriptException {
		ScriptSegment segment = findScriptSegment(script);
		return execute(segment, context);
	}

	public Object execute(String script) throws ScriptException {
		ScriptContext runContext = new DefaultScriptContext(); 
		runContext.setParent(scriptContext);
		return execute(script, runContext);
	}

	/**
	 * 支持缓存的查询脚本段
	 */
	public ScriptSegment findScriptSegment(Object queryRule)
			throws ScriptException {
		ScriptSegment segment = null;
		if (enableCache) {
			// 优先从缓存中查找脚本段
			segment = segmentCaches.get(queryRule);
			if (segment != null) {
				return segment;
			}
		}
		// 执行真正查询，并保存结果到缓存
		segment = findScriptSegmentWithoutCache(queryRule);
		if (segment != null) {
			segmentCaches.put(queryRule, segment);
		}
		return segment;
	}

	public void stop() throws ScriptException {
		segmentCaches.clear();
	}

	public ScriptInterpret getScriptInterpret() {
		if (scriptInterpret == null) {
			return ScriptUtil.getDefault();
		}
		return scriptInterpret;
	}

	public void setScriptInterpret(
			ScriptInterpret interpret) {
		this.scriptInterpret = interpret;
	}

	/**
	 * 真正的查询脚本片段的逻辑实现
	 * 
	 * @param queryRule
	 * @return
	 * @throws ScriptException
	 */
	protected abstract ScriptSegment findScriptSegmentWithoutCache(
			Object queryRule) throws ScriptException;
}
