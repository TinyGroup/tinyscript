package org.tinygroup.tinyscript.interpret.exception;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.interpret.InterpretExceptionInfo;

/**
 * 语法规则上下文异常
 * @author yancheng11334
 *
 */
public class ParserRuleContextException extends ScriptException implements InterpretExceptionInfo{

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

	public int getExceptionType() {
		return 2;
	}

	public String getExceptionScript() {
		return context.getText();
	}

	public int getStartLine() {
		return context.getStart().getLine();
	}

	public int getStartCharPositionInLine() {
		return context.getStart().getCharPositionInLine();
	}

	public int getStopLine() {
		return context.getStop().getLine();
	}

	public int getStopCharPositionInLine() {
		return context.getStop().getCharPositionInLine();
	}

	public String getMsg() {
		return null;
	}

	public Exception getSource() {
		return this;
	}
}
