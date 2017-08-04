package org.tinygroup.tinyscript.interpret.terminal;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ScriptUtil;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;

public class IdentifierNodeProcessor extends AbstractTerminalNodeProcessor  {

    public int getType() {
        return TinyScriptParser.Identifier;
    }

	public Object processTerminalNode(TerminalNode terminalNode, ScriptSegment segment,
			ScriptContext context)
			throws Exception {
		String name = terminalNode.getText();

		//增加bean处理
		Object value = ScriptUtil.getVariableValue(context, name);
		if(value!=null){
		   return value;
		}
		
		//执行import判断
		return ScriptUtil.findJavaClass(name, segment, null);
	}

}
