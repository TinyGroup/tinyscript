package org.tinygroup.tinyscript;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

/**
 * 脚本引擎工厂方法
 * 
 * @author yancheng11334
 * 
 */
public final class ScriptEngineFactory {

	private ScriptEngineFactory() {

	}

	/**
	 * 返回默认的ScriptEngine,不涉及加载注册脚本函数、处理器等
	 * 
	 * @return
	 * @throws ScriptException
	 */
	public static ScriptEngine createByClass() throws ScriptException {
		return new DefaultScriptEngine();
	}
	
	/**
	 * 返回默认配置的ScriptEngine实例，通过默认生成器加载注册脚本函数、处理器等
	 * @return
	 * @throws ScriptException
	 */
	public static ScriptEngine createByBean() throws ScriptException {
		//从全局配置获取默认的引擎配置信息
		String scriptEngineBean = ConfigurationUtil.getConfigurationManager().getConfiguration("defaultScriptEngine");
		if(scriptEngineBean==null){
		   throw new ScriptException("请检查配置文件是否定义[defaultScriptEngine]配置项");
		}
		return createByBean(scriptEngineBean);
	}

	/**
	 * 返回指定bean配置的ScriptEngine实例，通过默认生成器加载注册脚本函数、处理器等
	 * @param scriptEngineBean
	 * @return
	 * @throws ScriptException
	 */
	public static ScriptEngine createByBean(String scriptEngineBean) throws ScriptException {
		return createByBean(ScriptEngineBuilder.DEFAULT_BEAN_NAME,scriptEngineBean);
	}
	
	/**
	 * 返回指定bean配置的ScriptEngine实例，通过指定bean的引擎生成器加载注册脚本函数、处理器等
	 * @param builderBean
	 * @param scriptEngineBean
	 * @return
	 * @throws ScriptException
	 */
	public static ScriptEngine createByBean(String builderBean,String scriptEngineBean) throws ScriptException {
		try {
			ScriptEngine scriptEngine = (ScriptEngine) BeanContainerFactory
					.getBeanContainer(
							ScriptEngineFactory.class.getClassLoader())
					.getBean(scriptEngineBean);
			ScriptEngineBuilder builder = (ScriptEngineBuilder) BeanContainerFactory
					.getBeanContainer(
							ScriptEngineBuilder.class.getClassLoader())
					.getBean(builderBean);
			builder.registerComponent(scriptEngine);
			builder.registerProcessor(scriptEngine);
			return scriptEngine;
		} catch (Exception e) {
			throw new ScriptException("构建ScriptEngine实例发生异常:", e);
		}
	}
}
