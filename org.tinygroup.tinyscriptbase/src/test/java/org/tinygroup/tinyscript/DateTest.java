package org.tinygroup.tinyscript;

import java.sql.Timestamp;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

public class DateTest extends TestCase {

	private ScriptEngine scriptEngine;

	protected void setUp() throws Exception {
		scriptEngine = new DefaultScriptEngine();
		scriptEngine.start();
	}
	
	public void testDateDiff() throws Exception{
		Timestamp t1 = Timestamp.valueOf("2017-04-28 00:00:13.0");
		Timestamp t2 = Timestamp.valueOf("2017-04-27 23:59:59.0");
		ScriptContext context = new DefaultScriptContext();
		context.put("t1", t1);
		context.put("t2", t2);
		
		assertEquals(14L, scriptEngine.execute("return datediff(t1,t2);", context));
	}

	
}
