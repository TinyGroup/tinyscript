package org.tinygroup.tinyscript.interpret.terminal;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;

/**
 * 默认的终端处理节点
 * @author yancheng11334
 *
 */
public class DefaultTerminalNodeProcessor extends AbstractTerminalNodeProcessor{

	public int getType() {
		return 0;
	}

	public Object processTerminalNode(TerminalNode terminalNode, ScriptSegment segment,
			ScriptContext context)
			throws Exception {
		return terminalNode.getSymbol().getText();
	}


}
