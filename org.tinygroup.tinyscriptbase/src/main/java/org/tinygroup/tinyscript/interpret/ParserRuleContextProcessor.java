package org.tinygroup.tinyscript.interpret;

import org.antlr.v4.runtime.ParserRuleContext;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;

/**
 * 上下文处理器
 * @author yancheng11334
 *
 */
public interface ParserRuleContextProcessor<T extends ParserRuleContext> {

	 Class<T> getType();
	 
	 ScriptResult process(T parseTree, ScriptInterpret interpret, ScriptSegment segment, ScriptContext context)throws Exception;
}
