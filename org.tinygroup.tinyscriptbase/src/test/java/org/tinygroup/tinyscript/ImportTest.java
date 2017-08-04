package org.tinygroup.tinyscript;

import java.util.HashMap;

import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

import junit.framework.TestCase;

public class ImportTest extends TestCase{

	private ScriptEngine scriptEngine;

	protected void setUp() throws Exception {
		scriptEngine = new DefaultScriptEngine();
		scriptEngine.start();
	}
	
	public void test1() throws Exception{
		HashMap[] maps =  (HashMap[]) scriptEngine.execute("import java.util.*; maps = new HashMap[10]; return maps;");
	    assertEquals(10, maps.length);
	}
}
