package org.tinygroup.tinyscript;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

public class ArraysTests  extends TestCase {

	private ScriptEngine scriptEngine = null;

	protected void setUp() throws Exception {
		scriptEngine = new DefaultScriptEngine();
	}
	
	public void testArray() throws Exception{
		assertTrue(scriptEngine.execute("a = {1,2,3,4};") instanceof Object[]);
	}
	
	public void testArrayConstructionSupport1() throws Exception{
		assertTrue(scriptEngine.execute("a = new String[5];") instanceof String[]);
    }
	
	 public void testArrayConstructionSupport2() throws Exception{
		assertTrue((Boolean) scriptEngine.execute("xStr = new String[5]; xStr.length == 5;"));
	 }
	 
	 public void testArrayConstructionSupport3() throws Exception{
		assertEquals("foo", scriptEngine.execute("xStr = new String[5][5];  xStr[4][0] = \"foo\"; return xStr[4][0];"));
	 }
	 
	 public void testArrayConstructionSupport4() throws Exception{
	    assertEquals(10, scriptEngine.execute("xStr = new String[5][10];  xStr[4][1] = \"foo\"; return xStr[4].length;"));
	 }
	 
	 public void testArrayDefinitionWithInitializer() throws Exception {
		    String[] compareTo = new String[]{"foo", "bar"};
		    String[] results = (String[]) scriptEngine.execute("new String[] { \"foo\", \"bar\" };");

		    for (int i = 0; i < compareTo.length; i++) {
		      if (!compareTo[i].equals(results[i])) throw new AssertionError("arrays do not match.");
		    }
    }
	 
	 public void testArrayDefinitionWithCoercion() throws Exception {
		 double[] d = (double[])  scriptEngine.execute("new double[] { 1,2,3,4 };");
		 assertEquals(2d,d[1]);
	 }
	 
	 public void testArrayDefinitionWithCoercion2() throws Exception {
		 float[] f = (float[])  scriptEngine.execute("new float[] { 1,2,3,4 };");
		 assertEquals(2f,f[1]);
	 }
	 
	 public void testArrayCreation2() throws Exception {
		 String[][] s = (String[][]) scriptEngine.execute("new String[][] {{\"2008-04-01\", \"2008-05-10\"},{\"2007-03-01\", \"2007-02-12\"}};");
		 assertEquals("2007-03-01",
			        s[1][0]);
	 }

		 
}
