package org.tinygroup.tinyscript.interpret.exception;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.interpret.InterpretExceptionInfo;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

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
        //优化异常信息显示顺序，由最内层到最外层
		List<Throwable> causes = new ArrayList<Throwable>();
		Throwable cause = info.getSource();
		while(cause!=null){
			checkCause(causes,cause);
			cause = cause.getCause();
		}
		
		for(int i=causes.size()-1;i>=0;i--){
			formatExceptionMessage(sb,causes.get(i));
		}
		return sb.toString();
	}
	
	//过滤异常
	private void  checkCause(List<Throwable> causes,Throwable cause){
		if(cause.getClass().equals(ScriptException.class) && cause.getMessage()==null){
		   //排除无意义的ScriptException
		   return ;
		}
		if(causes.size()>0){
			//通过遍历,解决递归环的问题
			for(Throwable old:causes){
			   //排除重复的相同异常
			   if(old.equals(cause)){
				  return;
			   }
			}
			
		}
		causes.add(cause);
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
        	printTypeMessage(sb,interpretExceptionInfo.getExceptionType(),interpretExceptionInfo.getMsg());
        	printPlaceMessage(sb,interpretExceptionInfo);
        	printTextMessage(sb,interpretExceptionInfo);
        }else if(cause instanceof ScriptException){
           //无位置信息的脚本异常
        	ScriptException scriptException = (ScriptException) cause;
        	printTypeMessage(sb,ScriptException.ERROR_TYPE_RUNNING,scriptException.getMessage());
        }else{
           //第三方异常
        	printTypeMessage(sb,ScriptException.ERROR_TYPE_OTHER,cause.getMessage());
        	printClassMessage(sb,cause);
        }
	}
	
	/**
	 * 输出错误位置信息
	 * @param sb
	 * @param interpretExceptionInfo
	 */
	private void printPlaceMessage(StringBuilder sb,InterpretExceptionInfo interpretExceptionInfo){
		sb.append(ResourceBundleUtil.getDefaultMessage("error.place"));
		if(interpretExceptionInfo.getStopLine()<0 && interpretExceptionInfo.getStopCharPositionInLine()<0){
		   sb.append(ResourceBundleUtil.getDefaultMessage("error.two.info",interpretExceptionInfo.getStartLine(),interpretExceptionInfo.getStartCharPositionInLine()));
		}else{
		   sb.append(ResourceBundleUtil.getDefaultMessage("error.four.info",interpretExceptionInfo.getStartLine(),interpretExceptionInfo.getStartCharPositionInLine(),interpretExceptionInfo.getStopLine(),interpretExceptionInfo.getStopCharPositionInLine()));
		}
		sb.append("\n");
	}
	
	/**
	 * 输出错误类型信息
	 * @param sb
	 * @param interpretExceptionInfo
	 */
	private void printTextMessage(StringBuilder sb,InterpretExceptionInfo interpretExceptionInfo){
		sb.append(ResourceBundleUtil.getDefaultMessage("error.text"));
		if(interpretExceptionInfo.getStartLine()!=interpretExceptionInfo.getStopLine()){
		   sb.append("\n"); //直接换行
		}
		sb.append(interpretExceptionInfo.getExceptionScript());
	}
	
	/**
	 * 输出错误文本信息
	 * @param sb
	 * @param exceptionType
	 * @param msg
	 */
	private void printTypeMessage(StringBuilder sb,int exceptionType,String msg){
		switch(exceptionType){
		  case ScriptException.ERROR_TYPE_RECOGNIZER: { sb.append(ResourceBundleUtil.getDefaultMessage("error.type.recognizer")); break;}
		  case ScriptException.ERROR_TYPE_PARSER: { sb.append(ResourceBundleUtil.getDefaultMessage("error.type.parser")); break;}
		  case ScriptException.ERROR_TYPE_RUNNING: { sb.append(ResourceBundleUtil.getDefaultMessage("error.type.running"));break;}
		  case ScriptException.ERROR_TYPE_FUNCTION: { sb.append(ResourceBundleUtil.getDefaultMessage("error.type.function"));break;}
		  case ScriptException.ERROR_TYPE_FIELD: { sb.append(ResourceBundleUtil.getDefaultMessage("error.type.field"));break;}
		  case ScriptException.ERROR_TYPE_EXPRESSION: { sb.append(ResourceBundleUtil.getDefaultMessage("error.type.expression"));break;}
		  case ScriptException.ERROR_TYPE_DIRECTIVE: { sb.append(ResourceBundleUtil.getDefaultMessage("error.type.directive"));break;}
		  case ScriptException.ERROR_TYPE_SCRIPTCLASS: { sb.append(ResourceBundleUtil.getDefaultMessage("error.type.scriptclass"));break;}
		  case ScriptException.ERROR_TYPE_OTHER: { sb.append(ResourceBundleUtil.getDefaultMessage("error.type.other"));break;}
		  default:{ sb.append(ResourceBundleUtil.getDefaultMessage("error.type.unknown"));}
		}
		if(msg!=null){
			sb.append(msg);
		}else{
			sb.append(ResourceBundleUtil.getDefaultMessage("error.no.detail"));
		}
		sb.append("\n");
	}
	
	private void printClassMessage(StringBuilder sb,Object obj){
		sb.append(ResourceBundleUtil.getDefaultMessage("error.class")).append(obj.getClass().getName()).append("\n");
	}
	
	private void printLine(StringBuilder sb){
		sb.append("\n");
	}
}
