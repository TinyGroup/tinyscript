package org.tinygroup.tinyscript.interpret.exception;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.tinygroup.tinyscript.ScriptException;

/**
 * 语法规则上下文异常
 * @author yancheng11334
 *
 */
public class ParserRuleContextException extends ScriptException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3743392233859106251L;
	
	private transient ParserRuleContext context;
	
	public ParserRuleContextException(Exception e,ParserRuleContext parserRuleContext){
		super(e);
		this.context = parserRuleContext;
	}
	
	public Token getStratToken(){
		return context.getStart();
	}
	
	public Token getStopToken(){
		return context.getStop();
	}
	
	public String getText(){
		return context.getText();
	}
	
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n位置[");
		sb.append(context.getStart().getLine());
		sb.append(",");
		sb.append(context.getStart().getCharPositionInLine());
		sb.append("]-[");
		sb.append(context.getStop().getLine());
		sb.append(",");
		sb.append(context.getStop().getCharPositionInLine());
		sb.append("]\n");
		sb.append(context.getText());
		sb.append("\n");
		return sb.toString();
	}
}
