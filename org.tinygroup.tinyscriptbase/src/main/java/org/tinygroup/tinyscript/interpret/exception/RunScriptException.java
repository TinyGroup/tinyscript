package org.tinygroup.tinyscript.interpret.exception;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
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
	
	private int exceptionType = ERROR_TYPE_RUNNING;
	
	private ScriptSegment segment;
	
	private String msg;

	public RunScriptException(Exception e,ParseTree tree,ScriptSegment segment){
		this(e,tree,segment,ERROR_TYPE_RUNNING,null);
	}
	
	public RunScriptException(Exception e,ParseTree tree,ScriptSegment segment,int exceptionType){
		this(e,tree,segment,exceptionType,null);
	}
	
	public RunScriptException(Exception e,ParseTree tree,ScriptSegment segment,int exceptionType,String msg){
		super(e);
		this.tree = tree;
		this.segment = segment;
		this.exceptionType = exceptionType;
		this.msg = msg;
	}
	
	public int getExceptionType() {
		return exceptionType;
	}

	public String getExceptionScript() {
		try {
			return segment.getScript(getStartLine(), getStartCharPositionInLine(), getStopLine(), getStopCharPositionInLine());
		} catch (ScriptException e) {
			return tree.getText();
		}
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
		return msg;
	}

	public Exception getSource() {
		return this;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + exceptionType;
		result = prime * result + ((msg == null) ? 0 : msg.hashCode());
		result = prime * result + ((segment == null) ? 0 : segment.hashCode());
		result = prime * result + ((tree == null) ? 0 : tree.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RunScriptException other = (RunScriptException) obj;
		if (exceptionType != other.exceptionType)
			return false;
		if (msg == null) {
			if (other.msg != null)
				return false;
		} else if (!msg.equals(other.msg))
			return false;
		if (segment == null) {
			if (other.segment != null)
				return false;
		} else if (!segment.equals(other.segment))
			return false;
		if (tree == null) {
			if (other.tree != null)
				return false;
		} else if (!tree.equals(other.tree))
			return false;
		return true;
	}

	
}
