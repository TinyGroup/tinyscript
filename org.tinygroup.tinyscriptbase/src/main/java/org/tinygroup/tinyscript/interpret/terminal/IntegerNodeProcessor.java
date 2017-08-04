package org.tinygroup.tinyscript.interpret.terminal;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;

/**
 * 整型数处理节点
 * @author yancheng11334
 *
 */
public class IntegerNodeProcessor extends AbstractTerminalNodeProcessor {


    public int getType() {
        return TinyScriptParser.IntegerLiteral;
    }

	public Object processTerminalNode(TerminalNode terminalNode, ScriptSegment segment,
			ScriptContext context)
			throws Exception {
		String s = terminalNode.getText().toLowerCase();
		if(s.endsWith("l")){
			if(s.startsWith("+")){
				return parseLong(s.substring(1));
			}if(s.startsWith("-")){
				return -parseLong(s.substring(1));
			}else{
				return parseLong(s);
			}
		}else{
            if(s.startsWith("+")){
            	return parseInteger(s.substring(1));
			}if(s.startsWith("-")){
				return -parseInteger(s.substring(1));
			}else{
				return parseInteger(s);
			}
		}
	}
	
	private Long parseLong(String s){
		s = s.substring(0,s.length()-1);
		if(s.startsWith("0x")){
			return Long.parseLong(s.substring(2), 16);
		}else if(s.startsWith("0b")){
			return Long.parseLong(s.substring(2), 2);
		}else if(s.startsWith("0")){
			return Long.parseLong(s, 8);
		}else{
			return Long.parseLong(s, 10);
		}
	}
	
	private Integer parseInteger(String s){
        if(s.startsWith("0x")){
			return Integer.parseInt(s.substring(2), 16);
		}else if(s.startsWith("0b")){
			return Integer.parseInt(s.substring(2), 2);
		}else if(s.startsWith("0")){
			return Integer.parseInt(s, 8);
		}else{
			return Integer.parseInt(s, 10);
		}
	}

}
