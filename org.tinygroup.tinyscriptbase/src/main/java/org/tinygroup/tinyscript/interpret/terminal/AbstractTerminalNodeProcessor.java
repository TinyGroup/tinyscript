package org.tinygroup.tinyscript.interpret.terminal;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.TerminalNodeProcessor;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;

/**
 * 抽象的TerminalNode处理器
 * @author yancheng11334
 *
 */
public abstract class AbstractTerminalNodeProcessor implements TerminalNodeProcessor<TerminalNode> {

	public boolean processChildren() {
        return false;
    }
	
	/**
	 * 提供异常的包装处理
	 */
	public ScriptResult process(TerminalNode terminalNode, ScriptSegment segment,
			ScriptContext context) throws Exception {
		try{
			return new ScriptResult(processTerminalNode(terminalNode,segment,context));
		}catch(Exception e){
			throw new ScriptException(String.format("[%s]类型的TerminalNode处理发生异常", TinyScriptParser.tokenNames[getType()]),e);
		}
	}
	
	/**
	 * 具体的TerminalNode处理逻辑
	 * @param terminalNode
	 * @param segment
	 * @param context
	 * @return
	 * @throws Exception
	 */
	protected abstract Object processTerminalNode(TerminalNode terminalNode, ScriptSegment segment,ScriptContext context) throws Exception;

}
