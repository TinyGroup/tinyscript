package org.tinygroup.tinyscript.function;

import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.analysis.AnalysisModelUtil;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;

/**
 * 分析模型函数(非绑定)
 * @author yancheng11334
 *
 */
public class AnalysisModelFunction extends DynamicNameScriptFunction {

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		String functionName = ScriptContextUtil.getDynamicFunctionName(context);
		try{
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", functionName)); 
			}
			List<Object> dataList = ExpressionUtil.convertCollection(parameters[0]);
			if(dataList!=null && dataList.size()>0){
			   Object[] newParameters = new Object[parameters.length-1];
			   System.arraycopy(parameters, 1, newParameters, 0, newParameters.length);
			   return AnalysisModelUtil.analyse(functionName, dataList, context, newParameters);
			}
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", functionName)); 
		}catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", functionName),e); 
		}
	}

	public boolean exsitFunctionName(String name) {
		return AnalysisModelUtil.exsitAnalysisModel(name);
	}

	public List<String> getFunctionNames() {
		return AnalysisModelUtil.getAnalysisModelNames();
	}

}
