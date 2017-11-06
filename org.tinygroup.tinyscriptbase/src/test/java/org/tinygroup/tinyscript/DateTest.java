package org.tinygroup.tinyscript;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

public class DateTest extends TestCase {

	private ScriptEngine scriptEngine;

	protected void setUp() throws Exception {
		scriptEngine = new DefaultScriptEngine();
		scriptEngine.start();
	}

	public void testDateDiff() throws Exception {
		Timestamp t1 = Timestamp.valueOf("2017-04-28 00:00:13.0");
		Timestamp t2 = Timestamp.valueOf("2017-04-27 23:59:59.0");
		ScriptContext context = new DefaultScriptContext();
		context.put("t1", t1);
		context.put("t2", t2);

		assertEquals(14L, scriptEngine.execute("return datediff(\"SECOND\",t1,t2);", context));
	}

	public void testEqualsDate() throws Exception {
		Timestamp t1 = Timestamp.valueOf("2017-04-28 00:00:13.0");
		Timestamp t2 = Timestamp.valueOf("2017-04-27 23:59:59.0");
		Timestamp t3 = Timestamp.valueOf("2017-04-28 23:59:59.0");
		ScriptContext context = new DefaultScriptContext();
		context.put("t1", t1);
		context.put("t2", t2);
		context.put("t3", t3);

		assertEquals(false, scriptEngine.execute("return t1.equalsDate(t2);", context));
		assertEquals(true, scriptEngine.execute("return t1.equalsDate(t3);", context));
	}

	public void testTableau() throws ScriptException, ParseException {
		assertNotNull(scriptEngine.execute("return now();"));
		assertNotNull(scriptEngine.execute("return today();"));
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = dateFormat.parse("2018-10-31 11:1:1");
		assertEquals(date, scriptEngine.execute("return dateAdd(\"YEAR\",1,\"2017-10-31 11:1:1\");"));
		assertEquals(1, scriptEngine.execute("return datediff(\"WEEK\",\"2017-10-31\",\"2017-11-1\",\"wednesday\");"));
		assertEquals("Oct", scriptEngine.execute("return dateName(\"MONTH\",\"2017-10-31 11:1:1\");"));
		assertEquals(44, scriptEngine.execute("return datePart(\"WEEK\",\"2017-10-31 11:1:1\",\"monday\");"));
		assertNotNull(scriptEngine.execute("return dateTrunc(\"WEEKDAY\",\"2017-11-30 11:1:1\");"));
		assertEquals(true, scriptEngine.execute("return isDate(\"2017/11/30\");"));

	}

}
