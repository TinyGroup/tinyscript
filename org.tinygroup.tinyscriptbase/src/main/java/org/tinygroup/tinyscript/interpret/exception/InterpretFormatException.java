package org.tinygroup.tinyscript.interpret.exception;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.interpret.InterpretExceptionInfo;

/**
 * 解释器格式化异常
 * @author yancheng11334
 *
 */
public class InterpretFormatException extends ScriptException{

	private InterpretExceptionInfo info;
	/**
	 * 
	 */
	private static final long serialVersionUID = 6187784709978607513L;
	
	public InterpretFormatException(InterpretExceptionInfo info){
		this.info = info;
	}
	
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		Throwable cause = info.getSource();
		while(cause!=null){
			//递归处理异常信息
			formatExceptionMessage(sb,cause);
			cause = cause.getCause();
		}
		return sb.toString();
	}
	
	/**
	 * 格式化异常信息
	 * @param sb
	 * @param cause
	 */
	protected void formatExceptionMessage(StringBuilder sb,Throwable cause){
		printLine(sb);
        if(cause instanceof InterpretExceptionInfo){
           //带位置信息的脚本异常
        	InterpretExceptionInfo interpretExceptionInfo = (InterpretExceptionInfo) cause;
        	printTypeMessage(sb,interpretExceptionInfo.getExceptionType());
        	printDetailMessage(sb,interpretExceptionInfo.getMsg());
        	printPlaceMessage(sb,interpretExceptionInfo);
        	printTextMessage(sb,interpretExceptionInfo);
        }else if(cause instanceof ScriptException){
           //无位置信息的脚本异常
        	ScriptException scriptException = (ScriptException) cause;
        	printTypeMessage(sb,ScriptException.ERROR_TYPE_RUNNING);
        	printDetailMessage(sb,scriptException.getMessage());
        }else{
           //第三方异常
        	printTypeMessage(sb,ScriptException.ERROR_TYPE_OTHER);
        	printDetailMessage(sb,cause.getMessage());
        	printClassMessage(sb,cause);
        }
	}
	
	/**
	 * 输出错误位置信息
	 * @param sb
	 * @param interpretExceptionInfo
	 */
	private void printPlaceMessage(StringBuilder sb,InterpretExceptionInfo interpretExceptionInfo){
		sb.append("错误位置:");
		sb.append("[").append(interpretExceptionInfo.getStartLine()).append(",").append(interpretExceptionInfo.getStartCharPositionInLine()).append("]");
		if(interpretExceptionInfo.getStopLine()<0 && interpretExceptionInfo.getStopCharPositionInLine()<0){
		   sb.append("\n");
		}else{
		   sb.append("-[").append(interpretExceptionInfo.getStopLine()).append(",").append(interpretExceptionInfo.getStopCharPositionInLine()).append("]");
		   sb.append("\n");
		}
	}
	
	/**
	 * 输出错误类型信息
	 * @param sb
	 * @param interpretExceptionInfo
	 */
	private void printTextMessage(StringBuilder sb,InterpretExceptionInfo interpretExceptionInfo){
		sb.append("错误文本:").append(interpretExceptionInfo.getExceptionScript()).append("\n");
	}
	
	/**
	 * 输出错误文本信息
	 * @param sb
	 * @param interpretExceptionInfo
	 */
	private void printTypeMessage(StringBuilder sb,int exceptionType){
		sb.append("错误类型:");
		switch(exceptionType){
		  case ScriptException.ERROR_TYPE_RECOGNIZER: { sb.append("解释异常\n"); break;}
		  case ScriptException.ERROR_TYPE_PARSER: { sb.append("词法异常\n"); break;}
		  case ScriptException.ERROR_TYPE_RUNNING: { sb.append("运行时异常\n");break;}
		  case ScriptException.ERROR_TYPE_FUNCTION: { sb.append("函数异常\n");break;}
		  case ScriptException.ERROR_TYPE_FIELD: { sb.append("属性异常\n");break;}
		  case ScriptException.ERROR_TYPE_OTHER: { sb.append("第三方异常\n");break;}
		  default:{ sb.append("未知异常\n");}
		}
	}
	
	private void printClassMessage(StringBuilder sb,Object obj){
		sb.append("异常类:").append(obj.getClass().getName()).append("\n");
	}
	
	private void printDetailMessage(StringBuilder sb,String msg){
	    if(!StringUtil.isEmpty(msg)){
	    	sb.append("详细原因:").append(msg).append("\n");
	    }
	}
	
	private void printLine(StringBuilder sb){
		sb.append("\n");
	}
}
