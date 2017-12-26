package org.tinygroup.tinyscript;

import java.util.List;
import java.util.Map;

/**
 * tiny脚本段
 * @author yancheng11334
 *
 */
public interface ScriptSegment extends ScriptEngineOperator{

	/**
	 * 获得脚本段主键
	 * @return
	 */
	String getSegmentId();
	
	/**
	 * 获得包信息
	 * @return
	 */
	String getPackage();
	
	/**
	 * 获得类信息
	 * @return
	 */
	ScriptClass getScriptClass();
	
	/**
	 * 获得引入类信息
	 * @return
	 */
	List<String> getImportList();
	
	/**
	 * 获得脚本内容
	 * @return
	 */
	String getScript();
	
	/**
	 * 获得脚本内容
	 * @param startLine
	 * @param startCharPositionInLine
	 * @param stopLine
	 * @param stopCharPositionInLine
	 * @return
	 */
	String getScript(int startLine,int startCharPositionInLine,int stopLine,int stopCharPositionInLine) throws ScriptException;
	
	/**
	 * 获得脚本内容(从开始到指定位置)
	 * @param line
	 * @param charPositionInLine
	 * @return
	 */
	String getScriptFromStart(int line,int charPositionInLine) throws ScriptException;
	
	/**
	 * 获得脚本内容(从指定位置到结束)
	 * @param line
	 * @param charPositionInLine
	 * @return
	 */
	String getScriptToStop(int line,int charPositionInLine) throws ScriptException;
	
	/**
	 * 执行脚本片段
	 * @param context
	 * @return
	 * @throws ScriptException
	 */
	Object execute(ScriptContext context) throws ScriptException;
	
	/**
	 * 获取脚本片段运行缓存
	 * @return
	 */
	Map<Object,Object> getCache();
}
