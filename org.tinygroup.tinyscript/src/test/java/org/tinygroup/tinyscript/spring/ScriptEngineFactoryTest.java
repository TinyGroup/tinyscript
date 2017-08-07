package org.tinygroup.tinyscript.spring;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tinygroup.tinyrunner.Runner;
import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptEngineFactory;
import org.tinygroup.tinyscript.impl.DefaultComputeEngine;
import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

/**
 * 通过Spring构建引擎对象
 * @author yancheng11334
 *
 */
public class ScriptEngineFactoryTest {

	@BeforeClass
	public static void doBeforeClass() throws Exception {
		Runner.init("application.xml", null);
	}
	
	
	@Test
	public void testBase() throws Exception {
		//测试默认的构造函数
		ScriptEngine engine = ScriptEngineFactory.createByClass();
		Assert.assertEquals(DefaultScriptEngine.class, engine.getClass());
		
		//测试不同bean实例
		engine = ScriptEngineFactory.createByBean("bean1");
		Assert.assertEquals(DefaultComputeEngine.class, engine.getClass());
		Assert.assertEquals(false, engine.isIndexFromOne());
		
		Assert.assertFalse(engine.getScriptContext().exist("os"));
		Assert.assertFalse(engine.getScriptContext().exist("user"));
		
		engine = ScriptEngineFactory.createByBean("bean3");
		Assert.assertEquals(DefaultComputeEngine.class, engine.getClass());
		Assert.assertEquals(true, engine.isIndexFromOne());
		
		Assert.assertTrue(engine.getScriptContext().exist("os"));
		Assert.assertTrue(engine.getScriptContext().exist("user"));
		Assert.assertEquals("windows", engine.getScriptContext().get("os"));
		Assert.assertEquals("abc123", engine.getScriptContext().get("user"));
		
		//测试全局参数defaultScriptEngine
		engine = ScriptEngineFactory.createByBean();
		Assert.assertEquals(DefaultScriptEngine.class, engine.getClass());
		Assert.assertEquals(true, engine.isIndexFromOne());
	}
}
