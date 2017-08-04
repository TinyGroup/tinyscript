package org.tinygroup.tinyscript.interpret.terminal;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.impl.ScriptMethodContext;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;

public class ThisNodeProcessor extends AbstractTerminalNodeProcessor  {

	public int getType() {
		return TinyScriptParser.THIS;
	}

	protected Object processTerminalNode(TerminalNode terminalNode,
			ScriptSegment segment, ScriptContext context) throws Exception {
		if(context instanceof ScriptMethodContext){
		   return ((ScriptMethodContext)context).getScriptClassInstance();
		}
		return null;
	}

}
