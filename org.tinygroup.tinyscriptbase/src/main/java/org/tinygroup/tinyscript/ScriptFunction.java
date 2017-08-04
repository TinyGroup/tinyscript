package org.tinygroup.tinyscript;

/**
 * tiny脚本函数
 * @author yancheng11334
 *
 */
public interface ScriptFunction extends ScriptEngineOperator{

	/**
	 * 是否支持表达式参数
	 * @return
	 */
	boolean  enableExpressionParameter();
	
	 /**
     * 绑定到类型上，使之成为这些类型的成员函数，如果有多个可以用半角的逗号分隔
     * 比如：此方法返回“java.lang.Integer”，表示可以在脚本片段中的Integer类型的对象integer
     * 用“integer.someFunction(...)”的方式调用此方法
     * 为了方便扩展，这方法也可以返回多个类型，比如：“java.lang.Integer,java.lang.Long”，
     * 就表示同时给Integer和Long类型添加此扩展方法
     *
     * @return
     */
    String getBindingTypes();

    /**
     * 返回函数名，如果有多个名字，则用逗号分隔
     *
     * @return
     */
    String getNames();
    
    /**
     * 执行函数体
     * @param segment
     * @param context
     * @param parameters
     * @return
     * @throws ScriptException
     */
    Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException;
}
