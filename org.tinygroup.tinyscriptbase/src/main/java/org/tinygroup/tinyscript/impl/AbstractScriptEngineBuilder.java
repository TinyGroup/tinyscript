package org.tinygroup.tinyscript.impl;

import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptEngineBuilder;
import org.tinygroup.tinyscript.ScriptException;

/**
 * 定义抽象组装器
 * @author yancheng11334
 *
 */
public abstract class AbstractScriptEngineBuilder implements ScriptEngineBuilder{

	/**
	 * 注册动态组件,如系统函数
	 */
	public void registerComponent(ScriptEngine engine) throws ScriptException {
		registerFunction(engine);
	}

	/**
	 * 注册静态处理器
	 */
	public void registerProcessor(ScriptEngine engine) throws ScriptException {
		registerAssignValueProcessor();
		registerCollectionModel();
		registerOperator();
		registerOperatorWithContext();
		registerBooleanConverter();
		registerIteratorConverter();
		registerNumberCalculator();
		registerAttributeProcessor();
		registerFunctionCallProcessor();
		registerSingleMethodProcessor();
		registerNewInstanceProcessor();
		registerInstanceOfProcessor();
		registerMethodParameterRule();
		registerConstructorParameterRule();
		registerObjectItemProcessor();
		registerCustomProcessor();
	}

	/**
	 * 注册系统函数
	 * @param engine
	 * @throws ScriptException
	 */
	protected abstract void registerFunction(ScriptEngine engine) throws ScriptException;
	
	/**
	 * 注册赋值处理器
	 * @throws ScriptException
	 */
	protected abstract void registerAssignValueProcessor() throws ScriptException;
	
	/**
	 * 注册集合模型
	 * @throws ScriptException
	 */
	protected abstract void registerCollectionModel() throws ScriptException;
	
	/**
	 * 注册表达操作符
	 * @throws ScriptException
	 */
	protected abstract void registerOperator() throws ScriptException;
	
	/**
	 * 注册上下文表达操作符
	 * @throws ScriptException
	 */
	protected abstract void registerOperatorWithContext() throws ScriptException;
	
	/**
	 * 注册布尔值转换器
	 * @throws ScriptException
	 */
	protected abstract void registerBooleanConverter() throws ScriptException;
	
	/**
	 * 注册迭代器转换器
	 * @throws ScriptException
	 */
	protected abstract void registerIteratorConverter() throws ScriptException;
	
	/**
	 * 注册聚合计算器
	 * @throws ScriptException
	 */
	protected abstract void registerNumberCalculator() throws ScriptException;
	
	/**
	 * 注册属性处理器
	 * @throws ScriptException
	 */
	protected abstract void registerAttributeProcessor() throws ScriptException;
	
	/**
	 * 注册方法调用处理器
	 * @throws ScriptException
	 */
	protected abstract void registerFunctionCallProcessor() throws ScriptException;
	
	/**
	 * 注册单方法接口处理器
	 * @throws ScriptException
	 */
	protected abstract void registerSingleMethodProcessor() throws ScriptException;
	
	/**
	 * 注册新实例处理器
	 * @throws ScriptException
	 */
	protected abstract void registerNewInstanceProcessor() throws ScriptException;
	
	/**
	 * 注册InstanceOf指令处理器
	 * @throws ScriptException
	 */
	protected abstract void registerInstanceOfProcessor() throws ScriptException;
	
	/**
	 * 注册Java参数匹配规则
	 * @throws ScriptException
	 */
	protected abstract void registerMethodParameterRule() throws ScriptException;
	
	/**
	 * 注册Java构造参数匹配规则
	 * @throws ScriptException
	 */
	protected abstract void registerConstructorParameterRule() throws ScriptException;
	
	/**
	 * 注册对象下标处理器
	 * @throws ScriptException
	 */
	protected abstract void registerObjectItemProcessor() throws ScriptException;
	
	/**
	 * 注册用户自定义规则处理器
	 * @throws ScriptException
	 */
	protected abstract void registerCustomProcessor() throws ScriptException;
}
