package org.tinygroup.tinyscript.impl;


import java.util.HashMap;
import java.util.Map;

import org.tinygroup.context.Context;
import org.tinygroup.tinyscript.ScriptClass;
import org.tinygroup.tinyscript.ScriptClassMethod;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
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
		//支持多层上下文结构，设置引擎上下文为最顶层上下文
		Context nowContext = context;
		Context parentContext = nowContext.getParent();
		while(parentContext!=null){
			nowContext = parentContext;
			parentContext = nowContext.getParent();
		}
		if(!nowContext.equals(scriptContext)){
		   //关联引擎上下文
		   nowContext.setParent(scriptContext);
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
		if (enableCache) {
			// 优先从缓存中查找脚本段
			ScriptSegment segment = segmentCaches.get(queryRule);
			if (segment != null) {
				return segment;
			}
			// 执行真正查询，并保存结果到缓存
			segment = findScriptSegmentWithoutCache(queryRule);
			if (segment != null) {
				segmentCaches.put(queryRule, segment);
			}
			return segment;
		}else{
			return findScriptSegmentWithoutCache(queryRule);
		}
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
	
	public Object execute(String className, String methodName,
			Object... parameters) throws ScriptException {
		ScriptSegment segment = findScriptSegment(className);
		if(segment==null){
		   throw new ScriptException(ResourceBundleUtil.getDefaultMessage("engine.notfind.segment", className));
		}
		ScriptClass scriptClass = segment.getScriptClass();
		if(scriptClass==null){
		   throw new ScriptException(ResourceBundleUtil.getDefaultMessage("engine.undefine.scriptclass", className));
		}
		ScriptClassMethod scriptClassMethod = scriptClass.getScriptMethod(methodName);
		if(scriptClassMethod==null){
		   throw new ScriptException(ResourceBundleUtil.getDefaultMessage("engine.notfind.scriptclassmethod", className,methodName));
		}
		ScriptContext context = new DefaultScriptContext();
		context.setParent(scriptContext);
		//执行脚本方法
		return scriptClassMethod.execute(context, parameters);
	}
	
	public Object execute(Map<String,Object> maps,String className, String methodName) throws ScriptException {
		ScriptSegment segment = findScriptSegment(className);
		if(segment==null){
		   throw new ScriptException(ResourceBundleUtil.getDefaultMessage("engine.notfind.segment", className));
		}
		ScriptClass scriptClass = segment.getScriptClass();
		if(scriptClass==null){
		   throw new ScriptException(ResourceBundleUtil.getDefaultMessage("engine.undefine.scriptclass", className));
		}
		ScriptClassMethod scriptClassMethod = scriptClass.getScriptMethod(methodName);
		if(scriptClassMethod==null){
		   throw new ScriptException(ResourceBundleUtil.getDefaultMessage("engine.notfind.scriptclassmethod", className,methodName));
		}
		ScriptContext context = new DefaultScriptContext(maps);
		context.setParent(scriptContext);
		//执行脚本方法
		return scriptClassMethod.execute(context);
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
