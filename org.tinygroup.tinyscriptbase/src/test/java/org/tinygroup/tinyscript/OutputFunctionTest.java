package org.tinygroup.tinyscript;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

/**
 * 测试输出函数
 * 
 * @author yancheng11334
 * 
 */
public class OutputFunctionTest extends TestCase {

	private ScriptEngine scriptEngine;

	protected void setUp() throws Exception {
		scriptEngine = new DefaultScriptEngine();
	}
	
	public void testPrint() throws Exception {
		System.out.println("testPrint is start...");
		scriptEngine.execute("print(true);");
		scriptEngine.execute("print('a');");
		scriptEngine.execute("print(\"abc\".toCharArray());");
		scriptEngine.execute("print(2.345d);");
		scriptEngine.execute("print(.777f);");
		scriptEngine.execute("print(100);");
		scriptEngine.execute("print(9999999999L);");
		scriptEngine.execute("print(\"kkk\");");
		scriptEngine.execute("list=[1,2,4]; print(list);");
		System.out.println("testPrint is end!");
	}

	public void testPrintf() throws Exception {
		System.out.println("testPrintf is start...");
		scriptEngine.execute("printf(\"\t%d\t+\t%d\t=\t%d\",3,4,7);");
		System.out.println("testPrintf is end!");
	}
	
	public void testPrintln() throws Exception {
		System.out.println("testPrintln is start...");
		scriptEngine.execute("println(true);");
		scriptEngine.execute("println('a');");
		scriptEngine.execute("println(\"abc\".toCharArray());");
		scriptEngine.execute("println(2.345d);");
		scriptEngine.execute("println(.777f);");
		scriptEngine.execute("println(100);");
		scriptEngine.execute("println(9999999999L);");
		scriptEngine.execute("println(\"kkk\");");
		scriptEngine.execute("list=[1,2,4]; println(list);");
		System.out.println("testPrintln is end!");
	}
}
