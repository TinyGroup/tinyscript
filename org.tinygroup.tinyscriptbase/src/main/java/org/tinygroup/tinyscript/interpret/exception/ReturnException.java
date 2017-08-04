package org.tinygroup.tinyscript.interpret.exception;

import org.tinygroup.tinyscript.ScriptException;

/**
 * 返回中断异常
 * @author yancheng11334
 *
 */
public class ReturnException extends ScriptException{

	private Object value;

	public ReturnException(Object value) {
		super();
		this.value = value;
	}

	public Object getValue() {
		return value;
	}
	
}
