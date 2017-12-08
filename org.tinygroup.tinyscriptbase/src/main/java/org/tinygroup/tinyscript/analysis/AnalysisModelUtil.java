package org.tinygroup.tinyscript.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 分析模型工具类
 * @author yancheng11334
 *
 */
public final class AnalysisModelUtil {

	private static Map<String,AnalysisModelProcessor> processorMap = new HashMap<String,AnalysisModelProcessor>();
	
	static{
		addProcessor(new TrendLineProcessor());
	}
	
	private AnalysisModelUtil(){
		
	}
	
	/**
	 * 新增分析模型处理器
	 * @param processor
	 */
	public static void addProcessor(AnalysisModelProcessor processor){
		processorMap.put(processor.getName(), processor);
	}
	
	/**
	 * 删除分析模型处理器
	 * @param modelName
	 */
	public static void removeProcessor(String modelName){
		processorMap.remove(modelName);
	}
	
	/**
	 * 判断是否存在某个分析模型
	 * @param modelName
	 * @return
	 */
	public static boolean exsitAnalysisModel(String modelName){
		return processorMap.containsKey(modelName);
	}
	
	/**
	 * 获取全部模型名称列表
	 * @return
	 */
	public static List<String> getAnalysisModelNames(){
		return new ArrayList<String>(processorMap.keySet());
	}
	
	/**
	 * 执行分析逻辑
	 * @param modelName
	 * @param dataList
	 * @param context
	 * @param configs
	 * @return
	 * @throws ScriptException
	 */
	public static List<Object> analyse(String modelName,List<Object> dataList,ScriptContext context,Object... configs) throws ScriptException{
		AnalysisModelProcessor processor = processorMap.get(modelName);
		if(processor==null){
		   throw new ScriptException(ResourceBundleUtil.getDefaultMessage("analysis.model.nofound", modelName)); 
		}
		return processor.analyse(dataList, context, configs);
	}
}
