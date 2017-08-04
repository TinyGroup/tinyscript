package org.tinygroup.tinyscript;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

/**
 * 测试脚本引擎的内置常量
 * 
 * @author yancheng11334
 * 
 */
public class ConstantTest extends TestCase {

	private ScriptEngine scriptEngine;

	protected void setUp() throws Exception {
		scriptEngine = new DefaultScriptEngine();
		scriptEngine.start();
	}

	public void testDefault() throws Exception {
		// 用户可以通过engine.getScriptContext()动态注册静态常量对象
		// 目前内置常量仅PI和E
		assertEquals(Math.PI, scriptEngine.execute("return PI ;"));
		assertEquals(Math.E, scriptEngine.execute("return E ;"));
	}

	public void testConstant() throws Exception {
		// 还未注册加速度常量
		assertEquals(null, scriptEngine.execute("return G ;"));

		scriptEngine.getScriptContext().put("G", 9.8d); // 注册常量
		assertEquals(9.8d, scriptEngine.execute("return G ;"));

		scriptEngine.getScriptContext().remove("G"); // 卸载常量
		assertEquals(null, scriptEngine.execute("return G ;"));
	}

}
