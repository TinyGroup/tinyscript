package org.tinygroup.tinyscript.interpret.exception;

import org.antlr.v4.runtime.Recognizer;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.interpret.InnerScriptReader;
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
	
	private InnerScriptReader reader = null;
	
	public RecognizerException(Recognizer<?, ?> recognizer,int codeLine,int codeCharPositionInLine,String errorMsg){
		super();
		this.line = codeLine;
		this.charPositionInLine = codeCharPositionInLine;
		this.msg = errorMsg;
		
		String text = recognizer.getInputStream().toString();
		if (text != null) {
			try {
				reader = new InnerScriptReader(text);
			} catch (Exception ex) {
				// 忽略异常
				reader = null;
			}
		}
	}

	public String getMsg() {
		return msg;
	}
	
	public int getExceptionType() {
		return ERROR_TYPE_RECOGNIZER;
	}

	public String getExceptionScript() {
		try {
			return reader==null?null:reader.getScriptToStop(line, charPositionInLine);
		} catch (Exception e) {
			// 忽略异常
			return null;
		}
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
