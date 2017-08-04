package org.tinygroup.tinyscript;

import junit.framework.TestCase;

import org.tinygroup.tinyrunner.Runner;
import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

public class BeanTest extends TestCase {

	private ScriptEngine scriptEngine;

	protected void setUp() throws Exception {
		Runner.init("application.xml", null);
		scriptEngine = new DefaultScriptEngine();
		scriptEngine.start();
	}
	
	public void testBase() throws Exception{
		Student student =(Student) scriptEngine.execute("return student1;");
		assertNotNull(student);
		assertEquals(true, student.isRegistered());
		assertEquals(50, student.getAge());
		assertEquals("hello", student.getName());
	}

}
