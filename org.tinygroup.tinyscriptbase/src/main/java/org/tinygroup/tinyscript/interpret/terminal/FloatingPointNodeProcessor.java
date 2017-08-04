package org.tinygroup.tinyscript.interpret.terminal;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;

/**
 * 浮点数处理节点
 * @author yancheng11334
 *
 */
public class FloatingPointNodeProcessor extends AbstractTerminalNodeProcessor {


    public int getType() {
        return TinyScriptParser.FloatingPointLiteral;
    }

	public Object processTerminalNode(TerminalNode terminalNode, ScriptSegment segment,
			ScriptContext context)
			throws Exception {
		String s = terminalNode.getText().toLowerCase();
		if(s.endsWith("d")){
			if(s.startsWith("+")){
				return parseDouble(s.substring(1));
			}else if(s.startsWith("-")){
				return -parseDouble(s.substring(1));
			}else{
				return parseDouble(s);
			}
			
		}else{
			if(s.startsWith("+")){
            	return parseFloat(s.substring(1));
			}else if(s.startsWith("-")){
				return -parseFloat(s.substring(1));
			}else{
				return parseFloat(s);
			}
		}
	}
	
	private Float parseFloat(String s){
		return Float.parseFloat(s);
	}
	
	private Double parseDouble(String s){
		return Double.parseDouble(s);
	}

}
