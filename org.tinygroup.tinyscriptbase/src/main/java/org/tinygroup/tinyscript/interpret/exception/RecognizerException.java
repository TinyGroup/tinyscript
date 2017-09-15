package org.tinygroup.tinyscript.interpret.exception;

import java.util.regex.Pattern;

import org.antlr.v4.runtime.CommonToken;
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
	
	private static final Pattern EXTRANEOUS_RULE = Pattern.compile("extraneous\\s+input\\s+'.*'\\s+expecting\\s+\\{.*\\}");
	
	private static final Pattern MISSIING_RULE = Pattern.compile("missing\\s+'.*'\\s+at\\s+'.*'");
	
	private static final int ERROR_TYPE_EXTRANEOUS = 11;
	
	private static final int ERROR_TYPE_MISSING = 12;
	
	/**
	 * 异常类型，涉及内部处理，对外统一展示ERROR_TYPE_RECOGNIZER
	 */
	private int exceptionType = -1; 

	private int startLine;
	
	private int startCharPositionInLine;
	
    private int stopLine = -1;
	
	private int stopCharPositionInLine = -1;
	
	private String msg;
	
	private InnerScriptReader reader = null;
	
	public RecognizerException(Recognizer<?, ?> recognizer,CommonToken commonToken,int codeLine,int codeCharPositionInLine,String errorMsg){
		super();
		this.msg = errorMsg;
		this.exceptionType = dealExceptionType(msg);
		
		if(exceptionType==ERROR_TYPE_EXTRANEOUS || exceptionType==ERROR_TYPE_MISSING){
			initMissing(recognizer,commonToken);
		}else{
			initDefault(recognizer,codeLine,codeCharPositionInLine);
		}
		
	}
	
	
	private int dealExceptionType(String msg){
		if(EXTRANEOUS_RULE.matcher(msg).find()){
		   return ERROR_TYPE_EXTRANEOUS;
		}else if(MISSIING_RULE.matcher(msg).find()){
		   return ERROR_TYPE_MISSING;
		}
		return ERROR_TYPE_RECOGNIZER;
	}
	
	private void initMissing(Recognizer<?, ?> recognizer,CommonToken commonToken){
		try{
			this.startLine = 1;
			this.startCharPositionInLine = 0;
			this.stopLine = commonToken.getLine();
			this.stopCharPositionInLine = commonToken.getCharPositionInLine();
			
			String text = commonToken.getTokenSource().getInputStream().toString();
			if (text != null) {
				reader = new InnerScriptReader(text);
			}
			
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void initDefault(Recognizer<?, ?> recognizer,int codeLine,int codeCharPositionInLine){
		this.startLine = codeLine;
		this.startCharPositionInLine = codeCharPositionInLine;
		try {
			String text = recognizer.getInputStream().toString();
			if (text != null) {
				reader = new InnerScriptReader(text);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
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
			if(reader!=null){
			   if(exceptionType==ERROR_TYPE_EXTRANEOUS || exceptionType==ERROR_TYPE_MISSING){
				  return reader.getScript(startLine, startCharPositionInLine, stopLine, stopCharPositionInLine);
			   }else{
				  return reader.getScriptToStop(startLine, startCharPositionInLine);
			   }
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public int getStartLine() {
		return startLine;
	}

	public int getStartCharPositionInLine() {
		return startCharPositionInLine;
	}

	public int getStopLine() {
		return stopLine;
	}

	public int getStopCharPositionInLine() {
		return stopCharPositionInLine;
	}

	public Exception getSource() {
		return this;
	}
	
}
