package org.tinygroup.tinyscript.interpret.exception;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Recognizer;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.interpret.InterpretExceptionInfo;

/**
 * Recognizer解析异常
 * @author yancheng11334
 *
 */
public class RecognizerException extends ScriptException implements InterpretExceptionInfo{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 3665837093790604968L;

	private int line;
	
	private int charPositionInLine;
	
	private String msg;
	
	private String sourceName;
	
	private Parser parser;
	
	public RecognizerException(Recognizer<?, ?> recognizer,int codeLine,int codeCharPositionInLine,String errorMsg){
		super();
		if(recognizer instanceof Parser){
		   parser = (Parser) recognizer;
		}
		this.sourceName = recognizer.getInputStream().getSourceName();
		this.line = codeLine;
		this.charPositionInLine = codeCharPositionInLine;
		this.msg = errorMsg;
	}

	public String getMsg() {
		return msg;
	}

	public String getSourceName() {
		return sourceName;
	}
	
	public int getExceptionType() {
		return 1;
	}

	public String getExceptionScript() {
		return parser==null?null:parser.getCurrentToken().getText();
	}

	public int getStartLine() {
		return line;
	}

	public int getStartCharPositionInLine() {
		return charPositionInLine;
	}

	public int getStopLine() {
		return -1;
	}

	public int getStopCharPositionInLine() {
		return -1;
	}

	public Exception getSource() {
		return this;
	}
	
}
