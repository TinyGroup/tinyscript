package org.tinygroup.tinyscript.interpret.exception;

import org.antlr.v4.runtime.Recognizer;
import org.tinygroup.tinyscript.ScriptException;

/**
 * Recognizer解析异常
 * @author yancheng11334
 *
 */
public class RecognizerException extends ScriptException{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 3665837093790604968L;

	private int line;
	
	private int charPositionInLine;
	
	private String msg;
	
	private String sourceName;
	
	public RecognizerException(Recognizer<?, ?> recognizer,int codeLine,int codeCharPositionInLine,String errorMsg){
		super();
		this.sourceName = recognizer.getInputStream().getSourceName();
		this.line = codeLine;
		this.charPositionInLine = codeCharPositionInLine;
		this.msg = errorMsg;
	}

	public int getLine() {
		return line;
	}

	public int getCharPositionInLine() {
		return charPositionInLine;
	}

	public String getMsg() {
		return msg;
	}

	public String getSourceName() {
		return sourceName;
	}
	
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		 sb.append("TinyScript parse failed.\n");
         sb.append(sourceName);
         sb.append(':');
         sb.append(line);
         sb.append(':');
         sb.append(charPositionInLine);
         sb.append("\nmessage: ");
         sb.append(msg);
         sb.append('\n');
		return sb.toString();
	}
	
}
