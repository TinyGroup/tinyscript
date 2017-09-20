package org.tinygroup.tinyscript.tree.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.tree.DataNode;
import org.tinygroup.tinyscript.tree.xml.DocumentXmlNode;

/**
 * 读取xml树
 * @author yancheng11334
 *
 */
public class ReadXmlFunction extends AbstractScriptFunction {

	public String getNames() {
		return "readXml";
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
			if(parameters == null || parameters.length == 0){
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			}else if(checkParameters(parameters, 1)){
				return readXml((String)parameters[0]);
			}else{
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		}catch(ScriptException e){
			throw e;
		}catch(Exception e){
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()),e);
		}
	}
	
	private DataNode readXml(String xml){
		return new DocumentXmlNode(xml);
	}


}
