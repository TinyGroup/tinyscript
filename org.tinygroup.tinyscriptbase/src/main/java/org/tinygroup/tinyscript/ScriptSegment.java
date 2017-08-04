package org.tinygroup.tinyscript;

import java.util.List;

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
	 * 执行脚本片段
	 * @param context
	 * @return
	 * @throws ScriptException
	 */
	Object execute(ScriptContext context) throws ScriptException;
	
}
