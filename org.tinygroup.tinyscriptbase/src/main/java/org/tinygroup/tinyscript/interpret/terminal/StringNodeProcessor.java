package org.tinygroup.tinyscript.interpret.terminal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;

public class StringNodeProcessor extends AbstractTerminalNodeProcessor {
	private static final String rule = "[$][{][^}]*[}]";
	private static final Pattern pattern = Pattern.compile(rule);

	public int getType() {
		return TinyScriptParser.StringLiteral;
	}

	public Object processTerminalNode(TerminalNode terminalNode,
			ScriptSegment segment, ScriptContext context) throws Exception {
		String s = terminalNode.getText();
		s = s.replaceAll("\\\\\"", "\"");
		s = s.replaceAll("[\\\\][\\\\]", "\\\\");
		s = s.substring(1, s.length() - 1);

		if(pattern.matcher(s).find()) {
			// 进行$渲染
            Matcher matcher = pattern.matcher(s);
            StringBuilder sb = new StringBuilder();
            int pos = 0;
            while (matcher.find()) {
            	String group = matcher.group();
                String script = createScript(group);
                sb.append(s.substring(pos, matcher.start()));
                Object value = segment.getScriptEngine().execute(script, context);
                if(value!=null){
                   sb.append(value);  //替换动态解释内容
                }
                pos = matcher.end();
            }
            sb.append(s.substring(pos));
            return sb.toString();
		}
		
		return s;
	}
	
	protected String createScript(String s){
		s = s.substring(2, s.length()-1); // 去掉${}
		if(!s.endsWith(";")){
        	s = s+";";
        }
		return s;
	}

}
