package org.tinygroup.tinyscript.interpret.exception;

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
		sb.append("\n错误位置:");
		sb.append("[").append(info.getStartLine()).append("-").append(info.getStartCharPositionInLine()).append("]");
		if(info.getStopLine()<0 && info.getStopCharPositionInLine()<0){
		   sb.append("\n");
		}else{
		   sb.append("-[").append(info.getStopLine()).append("-").append(info.getStopCharPositionInLine()).append("]");
		   sb.append("\n");
		}
		sb.append("错误文本:").append(info.getExceptionScript()).append("\n");
		sb.append("错误类型:");
		switch(info.getExceptionType()){
		  case 1: { sb.append("解释异常\n"); break;}
		  case 2: { sb.append("词法异常\n"); break;}
		  case 3: { sb.append("运行时异常\n");break;}
		  default:{ sb.append("未知异常\n");}
		}
		String msg = info.getMsg();
		if(msg!=null){
			sb.append("详细原因:").append(msg).append("\n");
		}
		return sb.toString();
	}
}
