package org.tinygroup.tinyscript.interpret.exception;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.interpret.InterpretExceptionInfo;

/**
 * 运行时脚本异常
 * @author yancheng11334
 *
 */
public class RunScriptException extends ScriptException implements InterpretExceptionInfo{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8470435201340782572L;
	
	private ParseTree tree;

	public RunScriptException(Exception e,ParseTree tree){
		super(e);
		this.tree = tree;
	}
	public int getExceptionType() {
		return 3;
	}


	public String getExceptionScript() {
		if(tree instanceof TerminalNode){
			TerminalNode terminalNode = (TerminalNode) tree;
			return terminalNode.getSymbol().getText();
		}else if(tree instanceof ParserRuleContext){
			ParserRuleContext parserRuleContext = (ParserRuleContext) tree;
			return parserRuleContext.getText();
		}
		return null;
	}

	public int getStartLine() {
		if(tree instanceof TerminalNode){
			TerminalNode terminalNode = (TerminalNode) tree;
			return terminalNode.getSymbol().getLine();
		}else if(tree instanceof ParserRuleContext){
			ParserRuleContext parserRuleContext = (ParserRuleContext) tree;
			return parserRuleContext.getStart().getLine();
		}
		return -1;
	}

	public int getStartCharPositionInLine() {
		if(tree instanceof TerminalNode){
			TerminalNode terminalNode = (TerminalNode) tree;
			return terminalNode.getSymbol().getCharPositionInLine();
		}else if(tree instanceof ParserRuleContext){
			ParserRuleContext parserRuleContext = (ParserRuleContext) tree;
			return parserRuleContext.getStart().getCharPositionInLine();
		}
		return -1;
	}

	public int getStopLine() {
		if(tree instanceof ParserRuleContext){
			ParserRuleContext parserRuleContext = (ParserRuleContext) tree;
			return parserRuleContext.getStop().getLine();
		}
		return -1;
	}

	public int getStopCharPositionInLine() {
		if(tree instanceof ParserRuleContext){
			ParserRuleContext parserRuleContext = (ParserRuleContext) tree;
			return parserRuleContext.getStop().getCharPositionInLine();
		}
		return -1;
	}

	public String getMsg() {
		Throwable cause = getCause();
		StringBuilder sb = new StringBuilder();
		while(cause!=null){
			sb.append(cause.getMessage()).append("\n");
			cause = cause.getCause();
		}
		return sb.toString();
	}

	public Exception getSource() {
		return this;
	}

}
