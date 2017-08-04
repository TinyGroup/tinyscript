package org.tinygroup.tinyscript;

import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;
import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

/**
 * 测试静态相关
 * 
 * @author yancheng11334
 */
public class JavaStaticClassTest extends TestCase {

	private ScriptEngine scriptEngine;

	protected void setUp() throws Exception {
		scriptEngine = new DefaultScriptEngine();
		scriptEngine.start();
	}

	/**
	 * 测试静态类的静态属性
	 * @throws Exception
	 */
	public void testJavaLangSystem() throws Exception {
		try {
			// 这个应该能在控制台输入HelloWorld
			scriptEngine.execute("System.out.println(\"HelloWorld\");");
			scriptEngine.execute("System.out.println();");
			scriptEngine.execute("System.out.printf(\"\t%d\t*\t%d\t=\t%d\",2,3,2*3);");
			scriptEngine.execute("System.out.printf(\"Hello,world\");");
			scriptEngine.execute("System.out.printf(\"Hello,%s\",\"John\");");
			scriptEngine.execute("System.out.printf(\"Hello,%s,%s\",\"John\",\"Tom\");");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * 测试非静态类的静态属性
	 * @throws Exception
	 */
	public void testNotStaticClass() throws Exception {
		try {
			//测试类实例方式访问
            assertEquals(Student.country, scriptEngine.execute("s = new org.tinygroup.tinyscript.Student(); return s.country;"));
            //测试import方式访问
            assertEquals(Student.country, scriptEngine.execute("import org.tinygroup.tinyscript.Student; return Student.country;"));
            assertEquals(Student.country, scriptEngine.execute("import org.tinygroup.tinyscript.*; return Student.country;"));
            
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * 测试静态方法
	 * @throws Exception
	 */
	public void testStaticMethod() throws Exception {
		//测试是否支持空值
		assertEquals(false, scriptEngine.execute("import org.apache.commons.lang.StringUtils; return StringUtils.isEmpty(\"abc\");")); //非空值
		assertEquals(true, scriptEngine.execute("import org.apache.commons.lang.StringUtils; return StringUtils.isEmpty(null);")); //空值
		
		//测试同名方法indexOf,允许不歧义的null值
		assertEquals(0, scriptEngine.execute("import org.apache.commons.lang.StringUtils; return StringUtils.indexOf(\"aabaabaa\", 'a');")); //indexOf(String str, char searchChar)
		assertEquals(2, scriptEngine.execute("import org.apache.commons.lang.StringUtils; return StringUtils.indexOf(\"aabaabaa\", 'b');")); //indexOf(String str, char searchChar)
		assertEquals(-1, scriptEngine.execute("import org.apache.commons.lang.StringUtils; return StringUtils.indexOf(null, 'a');")); //indexOf(String str, char searchChar)
		assertEquals(0, scriptEngine.execute("import org.apache.commons.lang.StringUtils; return StringUtils.indexOf(\"aabaabaa\", \"a\");")); //indexOf(String str, String searchStr)
		assertEquals(2, scriptEngine.execute("import org.apache.commons.lang.StringUtils; return StringUtils.indexOf(\"aabaabaa\", \"b\");")); //indexOf(String str, String searchStr)
		assertEquals(-1, scriptEngine.execute("import org.apache.commons.lang.StringUtils; return StringUtils.indexOf(null, \"b\");")); //indexOf(String str, String searchStr)
		
		//第二个参数为null,最终会导致方法执行歧义,method.invoke(class,args)发生异常
		//assertEquals(-1, scriptEngine.execute("import org.apache.commons.lang.StringUtils; return StringUtils.indexOf(\"aabaabaa\", null);")); 
	}

}
