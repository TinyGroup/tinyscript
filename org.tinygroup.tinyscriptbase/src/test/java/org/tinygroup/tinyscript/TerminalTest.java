package org.tinygroup.tinyscript;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

public class TerminalTest extends TestCase {

	private ScriptEngine scriptEngine;

	protected void setUp() throws Exception {
		scriptEngine = new DefaultScriptEngine();
		scriptEngine.start();
	}
	
	public void testTerminal() throws Exception{
		assertEquals('1', scriptEngine.execute("return '1' ;")); //十进制
		assertEquals(123, scriptEngine.execute("return 123 ;")); //十进制
		assertEquals(1233333333L, scriptEngine.execute("return 1233333333L ;")); //十进制
		assertEquals(72, scriptEngine.execute("return 0110;")); //八进制
		assertEquals(72L, scriptEngine.execute("return 0110L;")); //八进制
		assertEquals(27516, scriptEngine.execute("return 0x6B7C;")); //十六进制
		assertEquals(27516L, scriptEngine.execute("return 0x6b7cl;")); //十六进制
		assertEquals(7, scriptEngine.execute("return 0b111;")); //二进制
		assertEquals(7L, scriptEngine.execute("return 0b111L;")); //二进制
		
		assertEquals(1.1f, scriptEngine.execute("return 1.1;")); //float型浮点
		assertEquals(1.1f, scriptEngine.execute("return 1.1f;")); //float型浮点
		assertEquals(2.53D, scriptEngine.execute("return 2.53D;")); //double型浮点
		assertEquals(.1f, scriptEngine.execute("return .1f;")); //float型浮点
		assertEquals(.53d, scriptEngine.execute("return .53d;")); //double型浮点
		assertEquals(9f, scriptEngine.execute("return 9f;")); //float型浮点
		assertEquals(10d, scriptEngine.execute("return 10d;")); //double型浮点
		
		assertEquals(1.0e3f, scriptEngine.execute("return 1.0e3f;")); //科学计数法float型浮点
		assertEquals(1.0e3d, scriptEngine.execute("return 1.0e3d;")); //科学计数法double型浮点
		assertEquals(0x7.5p8f, scriptEngine.execute("return 0x7.5p8f;")); //十六进制浮点
		
		assertEquals('a', scriptEngine.execute("return 'a' ;")); //单字符
		assertEquals('\t', scriptEngine.execute("return '\t' ;")); //特殊单字符
		assertEquals("abc", scriptEngine.execute("return \"abc\" ;")); //字符串
		assertEquals("null", scriptEngine.execute("return \"null\";")); //特殊字符串
		assertEquals("if", scriptEngine.execute("return \"if\";")); //特殊字符串
		
		assertEquals(true, scriptEngine.execute("return true;"));
		assertEquals(false, scriptEngine.execute("return false;"));
		
		assertEquals(null, scriptEngine.execute("return null;")); //空值
		
	}
	
	public void testString() throws Exception{
		assertEquals("dog and ", scriptEngine.execute("a=\"cat\"; return \"dog and ${b}\";")); //字符串的$渲染,语法符合找不到对象，返回空值
		assertEquals("dog and cat", scriptEngine.execute("a=\"cat\"; return \"dog and ${a}\";")); //字符串的$渲染,仅支持一层
		assertEquals("cat{}[]", scriptEngine.execute("a=\"cat\"; return \"${a}{}[]\";")); //字符串的$渲染,仅支持一层
		assertEquals("mmmmmcat", scriptEngine.execute("a=\"cat\"; return \"mmmmm${a}\";")); //字符串的$渲染,仅支持一层
		
		assertEquals("dog and 6", scriptEngine.execute(" return \"dog and ${1+2+3}\";")); //支持表达式
		assertEquals("##cat and dog##", scriptEngine.execute("a=\"cat\"; b=\"dog\"; op=\"and\"; return \"##${a} ${op} ${b}##\";")); //包含多个变量
	}

}
