package org.tinygroup.tinyscript.interpret;

import org.antlr.v4.runtime.tree.ParseTree;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;

public interface TerminalNodeProcessor<T extends ParseTree> {

   int getType();
   
   ScriptResult process(T terminalNode, ScriptSegment segment, ScriptContext context) throws Exception;
}
