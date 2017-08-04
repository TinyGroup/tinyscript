package org.tinygroup.tinyscript.interpret;

/**
 * 脚本执行结果
 * @author yancheng11334
 *
 */
public final class ScriptResult {
	
	/**
	 *  无返回值的结果
	 */
	public static final ScriptResult VOID_RESULT = new ScriptResult(null,true,false);
	
	/**
	 * null值的结果
	 */
	public static final ScriptResult NULL_RESULT = new ScriptResult(null,false,false);
    
	private Object result;
	
	private boolean isVoid;
	
	private boolean isContinue;
	
	public ScriptResult(Object result) {
		this(result,false,false);
	}

	public ScriptResult(Object result, boolean isVoid, boolean isContinue) {
		super();
		this.result = result;
		this.isVoid = isVoid;
		this.isContinue = isContinue;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public boolean isVoid() {
		return isVoid;
	}

	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	public boolean isContinue() {
		return isContinue;
	}

	public void setContinue(boolean isContinue) {
		this.isContinue = isContinue;
	}

	
	public String toString() {
		return "ScriptResult [result=" + result + ", isVoid=" + isVoid
				+ ", isContinue=" + isContinue + "]";
	}
	
}
