package org.tinygroup.tinyscript.collection.function.list;

import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

public class FillFunction extends AbstractScriptFunction {

	public String getNames() {
		return "fill";
	}
	
	public String getBindingTypes() {
		return "java.util.List";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (parameters.length == 2 && parameters[0] != null) {
                List list = (List) parameters[0];
                for(int i=0;i<list.size();i++){
                	list.set(i, parameters[1]);
                }
			} else if (parameters.length == 4 && parameters[0] != null && parameters[1] != null && parameters[2] != null) {
				List list = (List) parameters[0];
				int start = ExpressionUtil.convertInteger(parameters[1]);
				int end = ExpressionUtil.convertInteger(parameters[2]);
				if(getScriptEngine().isIndexFromOne()){
					start = start-1;
					end = end-1;
				}
				for(int i=start;i<=end;i++){
					list.set(i, parameters[3]);
				}
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}

		return null;
	}

}
