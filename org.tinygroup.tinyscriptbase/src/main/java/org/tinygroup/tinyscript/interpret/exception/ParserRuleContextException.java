package org.tinygroup.tinyscript.interpret.exception;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.interpret.InnerScriptReader;
import org.tinygroup.tinyscript.interpret.InterpretExceptionInfo;

/**
 * 语法规则上下文异常
 * 
 * @author yancheng11334
 * 
 */
public class ParserRuleContextException extends ScriptException implements
		InterpretExceptionInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3743392233859106251L;

	private transient ParserRuleContext context;

	private InnerScriptReader reader = null;

	// 定义这两个变量解决context可能出现start和end上下颠倒的问题
	private transient Token start;
	private transient Token stop;

	public ParserRuleContextException(RecognitionException e,
			ParserRuleContext parserRuleContext, String text) {
		super(e);
		this.context = parserRuleContext;
		if (text != null) {
			try {
				reader = new InnerScriptReader(text);
			} catch (Exception ex) {
				// 忽略异常
				reader = null;
			}
		}
	}

	private void initToken() {
		// 检查是否已经初始化token节点
		if (start != null || stop != null) {
			return;
		}
		if (context.getStop() == null) {
			start = context.getStart();
		} else {
			if (context.getStart().getLine() < context.getStop().getLine()
					|| (context.getStart().getLine() == context.getStop()
							.getLine() && context.getStart()
							.getCharPositionInLine() < context.getStop()
							.getCharPositionInLine())) {
				start = context.getStart();
				stop = context.getStop();
			} else {
				//前后颠倒，忽略错误的结束
				start = context.getStart();
			}
		}
	}

	public Token getStratToken() {
		return start;
	}

	public Token getStopToken() {
		return stop;
	}

	public String getText() {
		return context.getText();
	}

	public int getExceptionType() {
		return ERROR_TYPE_PARSER;
	}

	public String getExceptionScript() {
		initToken();
		try {
			if (reader == null) {
				return context.getText();
			} else {
				if (stop != null) {
					return reader.getScript(start.getLine(),
							start.getCharPositionInLine(), stop.getLine(),
							stop.getCharPositionInLine());
				} else {
					return reader.getScriptToStop(start.getLine(),
							start.getCharPositionInLine());
				}
			}
		} catch (Exception e) {
			return context.getText();
		}

	}

	public int getStartLine() {
		initToken();
		return start == null ? -1 : start.getLine();
	}

	public int getStartCharPositionInLine() {
		initToken();
		return start == null ? -1 : start.getCharPositionInLine();
	}

	public int getStopLine() {
		initToken();
		return stop == null ? -1 : stop.getLine();
	}

	public int getStopCharPositionInLine() {
		initToken();
		return stop == null ? -1 : stop.getCharPositionInLine();
	}

	public String getMsg() {
		return null;
	}

	public Exception getSource() {
		return this;
	}
}
