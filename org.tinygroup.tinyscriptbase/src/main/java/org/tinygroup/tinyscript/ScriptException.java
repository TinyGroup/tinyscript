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
