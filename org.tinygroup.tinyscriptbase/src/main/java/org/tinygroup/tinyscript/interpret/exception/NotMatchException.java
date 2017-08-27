package org.tinygroup.tinyscript.interpret.exception;

import org.tinygroup.tinyscript.ScriptException;

/**
 * 行为或操作不匹配
 * @author yancheng11334
 *
 */
public class NotMatchException extends ScriptException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4674614890213450608L;

	public NotMatchException() {
		super();
	}

	public NotMatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotMatchException(String message) {
		super(message);
	}

	public NotMatchException(Throwable cause) {
		super(cause);
	}
}
