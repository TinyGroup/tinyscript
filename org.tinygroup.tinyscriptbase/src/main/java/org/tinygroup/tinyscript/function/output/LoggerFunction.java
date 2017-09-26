package org.tinygroup.tinyscript.function.output;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.DynamicNameScriptFunction;
import org.tinygroup.tinyscript.interpret.FunctionCallUtil;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;

/**
 * 通过log4j实现脚本类日志输出
 * @author yancheng11334
 *
 */
public class LoggerFunction extends DynamicNameScriptFunction {

	private static final Set<String> names = new HashSet<String>();
	
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("tinyscript");
	
	static{
		names.add("trace");
		names.add("debug");
		names.add("info");
		names.add("warn");
		names.add("error");
	}
	
	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		String functionName = ScriptContextUtil.getDynamicFunctionName(context);
		try{
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", functionName)); 
			}else if(parameters!=null && parameters.length>=1){
				return FunctionCallUtil.operate(segment, context, logger, functionName, parameters);
			}else{
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", functionName)); 
			}
			
		}catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", functionName),e); 
		}
	}
	

	public boolean exsitFunctionName(String name) {
		return names.contains(name);
	}


	public List<String> getFunctionNames() {
		return new ArrayList<String>(names);
	}

}
