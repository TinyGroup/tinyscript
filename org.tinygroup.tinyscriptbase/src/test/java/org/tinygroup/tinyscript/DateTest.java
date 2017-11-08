package org.tinygroup.tinyscript;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.function.date.DateUtil;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

public class DateTest extends TestCase {

	private ScriptEngine scriptEngine;

	protected void setUp() throws Exception {
		scriptEngine = new DefaultScriptEngine();
		scriptEngine.start();
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
		Date date1 = DateUtil.convertDateByString("2017-10-31 11:1:1", "yyyy-MM-dd HH:mm:ss");
		Date date2 = DateUtil.convertDateByString("2017-11-2", "yyyy-MM-dd");
		ScriptContext context = new DefaultScriptContext();
		context.put("date1", date1);
		context.put("date2", date2);

		assertNotNull(scriptEngine.execute("return now();"));
		assertNotNull(scriptEngine.execute("return today();"));

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = dateFormat.parse("2018-10-31 11:1:1");
		assertEquals(date, scriptEngine.execute("return dateAdd(\"YEAR\",1,date1);", context));
		assertEquals(1, scriptEngine.execute("return dateDiff(\"WEEK\",date1,date2,\"wednesday\");", context));
		assertEquals("tuesday", scriptEngine.execute("return dateName(\"WEEKDAY\",date1);", context));
		assertEquals(44, scriptEngine.execute("return datePart(\"WEEK\",date1,\"monday\");", context));
		assertNotNull(scriptEngine.execute("return dateTrunc(\"WEEKDAY\",date1);", context));

	}

}
