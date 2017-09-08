package org.tinygroup.tinyscript;

/**
 * tiny脚本异常
 * @author yancheng11334
 *
 */
public class ScriptException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9221984710368035612L;
	
	public static final int ERROR_TYPE_RECOGNIZER = 1;
	
	public static final int ERROR_TYPE_PARSER = 2;
	
	public static final int ERROR_TYPE_RUNNING = 3;
	
	public static final int ERROR_TYPE_FUNCTION = 31;
	
	public static final int ERROR_TYPE_FIELD = 32;
	
	public static final int ERROR_TYPE_OTHER = 4;
	
	public ScriptException() {
		super();
	}

	public ScriptException(String message, Throwable cause) {
		super(message, cause);
	}

	public ScriptException(String message) {
		super(message);
	}

	public ScriptException(Throwable cause) {
		super(cause);
	}
	
}
