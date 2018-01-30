package org.tinygroup.tinyscript;

import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.config.FunctionConfig;

/**
 * tiny脚本引擎
 * @author yancheng11334
 *
 */
public interface ScriptEngine {

	/**
	 * 获得编码
	 * @return
	 */
	String getEncode();
	
	/**
	 * 设置编码
	 * @param encode
	 */
	void setEncode(String encode);
	
	/**
	 * 元素下标是否从1开始(不涉及原生java的API)
	 * <br>true表示从1开始,false表示从0开始
	 * @return
	 */
	boolean isIndexFromOne();
	
	/**
	 * 设置元素下标是否从1开始(不涉及原生java的API)
	 * <br>true表示从1开始,false表示从0开始 
	 * @param tag
	 */
	void setIndexFromOne(boolean tag);
	
	/**
	 * 是否启用脚本缓存
	 * @return
	 */
	boolean isEnableCache();
	
	/**
	 * 设置启用脚本缓存参数
	 * @param tag
	 */
	void setEnableCache(boolean tag);
	
	/**
	 * 添加脚本段
	 * @param segment
	 */
	void addScriptSegment(ScriptSegment segment);
	
	/**
	 * 删除脚本段
	 * @param segment
	 */
	void removeScriptSegment(ScriptSegment segment);
	
	/**
	 * 查找脚本段
	 * @param segmentId
	 * @return
	 */
	ScriptSegment getScriptSegment(String segmentId);
	
	/**
	 * 得到脚本引擎内置的上下文环境
	 * @return
	 */
	ScriptContext getScriptContext();
	
	/**
	 * 添加脚本函数
	 * @param function
	 */
    void addScriptFunction(ScriptFunction function) throws ScriptException;
    
    /**
     * 卸载脚本函数
     * @param function
     */
    void removeScriptFunction(ScriptFunction function) throws ScriptException;
    
    /**
     * 根据对象和函数名查找函数
     * @param object
     * @param functionName
     * @return
     */
    ScriptFunction findScriptFunction(Object object, String functionName) throws ScriptException;
    
    /**
     * 根据对象查找函数定义项
     * @param object
     * @return
     * @throws ScriptException
     */
    List<FunctionConfig> getFunctionConfigs(Object object) throws ScriptException;
    
	/**
	 * 查询脚本段
	 * @param queryRule
	 * @return
	 * @throws ScriptException
	 */
	ScriptSegment findScriptSegment(Object queryRule) throws ScriptException;
	
	/**
	 * 根据脚本段执行
	 * @param segment
	 * @param context
	 * @return
	 * @throws ScriptException
	 */
	Object execute(ScriptSegment segment, ScriptContext context) throws ScriptException;
	
	
	/**
	 * 根据脚本段执行
	 * @param segment
	 * @return
	 * @throws ScriptException
	 */
	Object execute(ScriptSegment segment) throws ScriptException;
	
	/**
	 * 根据脚本内容执行
	 * @param script
	 * @param context
	 * @return
	 * @throws ScriptException
	 */
	Object execute(String script, ScriptContext context) throws ScriptException;
	
	/**
	 * 根据脚本内容执行
	 * @param script
	 * @return
	 * @throws ScriptException
	 */
	Object execute(String script) throws ScriptException;
	
	/***
	 * 根据脚本类，执行指定方法
	 * @param className
	 * @param methodName
	 * @param parameters
	 * @return
	 * @throws ScriptException
	 */
	Object execute(String className,String methodName,Object...parameters) throws ScriptException;
	
	/**
	 * 根据脚本类，执行指定方法
	 * @param maps
	 * @param className
	 * @param methodName
	 * @return
	 * @throws ScriptException
	 */
	Object execute(Map<String,Object> maps,String className,String methodName) throws ScriptException;
	
	/**
	 * 启动脚本引擎
	 * @throws ScriptException
	 */
	void start() throws ScriptException;
	
	/**
	 * 停止脚本引擎
	 * @throws ScriptException
	 */
	void stop() throws ScriptException;
	
}
