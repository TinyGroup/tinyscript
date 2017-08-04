package org.tinygroup.tinyscript.interpret.terminal;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;

public class CharNodeProcessor extends AbstractTerminalNodeProcessor {

    public int getType() {
        return TinyScriptParser.CharacterLiteral;
    }

	public Object processTerminalNode(TerminalNode terminalNode, ScriptSegment segment,
			ScriptContext context)
			throws Exception {
		String s = terminalNode.getText();
//		s = s.replaceAll("\\\\'", "'");
//        s = s.replaceAll("[\\\\][\\\\]", "\\\\");
        s= s.substring(1, s.length()-1);
        return s.charAt(0);
	}

}
