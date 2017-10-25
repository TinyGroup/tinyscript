package org.tinygroup.tinyscript.interpret.terminal;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;

/**
 * 块注释
 * @author yancheng11334
 *
 */
public class BlockCommentNodeProcessor extends AbstractTerminalNodeProcessor {

	public int getType() {
		return TinyScriptParser.BLOCK_COMMENT;
	}

	protected Object processTerminalNode(TerminalNode terminalNode,
			ScriptSegment segment, ScriptContext context) throws Exception {
		return null;
	}

}
