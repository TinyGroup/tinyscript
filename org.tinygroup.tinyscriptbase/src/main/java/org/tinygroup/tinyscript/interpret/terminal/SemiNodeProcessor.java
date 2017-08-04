package org.tinygroup.tinyscript.interpret.terminal;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;

public class SemiNodeProcessor extends AbstractTerminalNodeProcessor {

	public int getType() {
		return TinyScriptParser.SEMI;
	}

	public Object processTerminalNode(TerminalNode terminalNode,
			ScriptSegment segment, ScriptContext context)
			throws Exception {
		return null;
	}

}
