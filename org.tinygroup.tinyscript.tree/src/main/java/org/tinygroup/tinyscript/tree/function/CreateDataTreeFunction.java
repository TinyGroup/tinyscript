package org.tinygroup.tinyscript.tree.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.tree.DataNode;
import org.tinygroup.tinyscript.tree.impl.DefaultDataNode;

/**
 * 创建树型结构
 * @author yancheng11334
 *
 */
public class CreateDataTreeFunction extends AbstractScriptFunction {

	public String getNames() {
		return "createTree";
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
			if(parameters == null || parameters.length == 0){
				throw new ScriptException(String.format("%s函数的参数为空!", getNames()));
			}else if(checkParameters(parameters, 2)){
				return createTree((String)parameters[0],parameters[1]);
			}else{
				throw new ScriptException(String.format("%s函数的参数格式不正确!", getNames()));
			}
		}catch(ScriptException e){
			throw e;
		}catch(Exception e){
			throw new ScriptException(String.format("%s函数执行发生异常:", getNames()),e);
		}
	}
	
	private DataNode createTree(String name,Object value){
		return new DefaultDataNode(name,value);
	}

}
