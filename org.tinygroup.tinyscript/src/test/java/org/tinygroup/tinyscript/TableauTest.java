package org.tinygroup.tinyscript;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tinygroup.tinyrunner.Runner;
import org.tinygroup.tinyscript.impl.DefaultTinyScriptEngine;

public class TableauTest {
	@BeforeClass
	public static void doBeforeClass() throws Exception {
		Runner.init("application.xml", null);
	}

	@Test
	public void testBase() throws Exception {
		ScriptEngine engine = new DefaultTinyScriptEngine();
		Assert.assertEquals(-1, engine.execute("return sign(-0.1);"));
		Assert.assertEquals(0, engine.execute("return sign(0.0);"));
		Assert.assertEquals(1, engine.execute("return sign(114);"));
		Assert.assertEquals(0.5235987755982988, engine.execute("return radians(30);"));
		Assert.assertEquals(2.0, engine.execute("return log(100,10);"));
		Assert.assertEquals(2.0, engine.execute("return ln(exp(2));"));
		Assert.assertEquals(7.3890560989306495, engine.execute("return exp(2);"));
		Assert.assertEquals(29.999999999999996, engine.execute("return degrees(radians(30));"));
		Assert.assertEquals(-1.1682333052318372, engine.execute("return cot(15);"));
		Assert.assertEquals(0.7853981633974483, engine.execute("return atan2(5,5);"));
		Assert.assertEquals(3, engine.execute("return percentile([1..5],0.5);"));
	}
}
