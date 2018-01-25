package org.tinygroup.tinyscript.tree.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.tree.impl.DataNodeUtil;
import org.tinygroup.tinyscript.tree.impl.TreeDataNode;

/**
 * TreeDataNode转换JSON字符串
 * @author yancheng11334
 *
 */
public class TreeDataNodeToJsonFunction extends AbstractScriptFunction {

	public String getBindingTypes() {
		return TreeDataNode.class.getName();
	}
	
	public String getNames() {
		return "toJson";
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
			if(parameters == null || parameters.length == 0){
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			}else if(checkParameters(parameters, 1)){
				TreeDataNode tree = (TreeDataNode) parameters[0];
				return DataNodeUtil.toJson(tree);
			}else{
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		}catch(ScriptException e){
			throw e;
		}catch(Exception e){
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()),e);
		}
	}

}
