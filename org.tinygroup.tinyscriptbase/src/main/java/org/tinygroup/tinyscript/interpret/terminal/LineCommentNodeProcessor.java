package org.tinygroup.tinyscript.interpret.terminal;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;

/**
 * 行注释
 * @author yancheng11334
 *
 */
public class LineCommentNodeProcessor extends AbstractTerminalNodeProcessor {

	public int getType() {
		return TinyScriptParser.LINE_COMMENT;
	}

	protected Object processTerminalNode(TerminalNode terminalNode,
			ScriptSegment segment, ScriptContext context) throws Exception {
		return null;
	}

}
