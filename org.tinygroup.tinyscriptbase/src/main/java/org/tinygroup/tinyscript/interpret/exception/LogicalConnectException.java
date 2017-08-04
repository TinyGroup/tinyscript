package org.tinygroup.tinyscript.interpret.exception;

import org.tinygroup.tinyscript.ScriptException;

/**
 * 逻辑连接异常(实现短路与和短路或)
 * @author yancheng11334
 *
 */
public class LogicalConnectException extends ScriptException{

	private Boolean tag;

	public LogicalConnectException(Boolean tag) {
		super();
		this.tag = tag;
	}
	
	public Boolean getTag(){
		return tag;
	}
}
