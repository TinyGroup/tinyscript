package org.tinygroup.tinyscript.analysis;

import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;

/**
 * 分析模型算法处理器
 * @author yancheng11334
 *
 */
public interface AnalysisModelProcessor {

	/**
	 * 获取模型名称
	 * @return
	 */
	String  getName();
	
	/**
	 * 执行分析逻辑
	 * @param dataList
	 * @param context
	 * @param configs
	 * @return
	 * @throws ScriptException
	 */
	List<Object>  analyse(List<Object> dataList,ScriptContext context,Object... configs) throws ScriptException;
}
