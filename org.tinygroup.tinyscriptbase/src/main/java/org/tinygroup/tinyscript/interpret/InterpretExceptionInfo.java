package org.tinygroup.tinyscript.interpret;

/**
 * 解释器异常信息
 * @author yancheng11334
 *
 */
public interface InterpretExceptionInfo {

	int getExceptionType();
	
	String getExceptionScript();
	
	int getStartLine();
	
	int getStartCharPositionInLine();
	
	int getStopLine();
	
	int getStopCharPositionInLine();
	
	String getMsg();
	
	Exception getSource();
}
