package org.tinygroup.tinyscript;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

/**
 * 脚本引擎的测试随机数据
 * @author yancheng11334
 *
 */
public class RandomTest  extends TestCase{

	public void testBase() throws Exception {
		ScriptEngine scriptEngine = new DefaultScriptEngine();
		ScriptContext context = new DefaultScriptContext();
		context.put("limit", 1000);
		
		//测试double随机数
		assertEquals(Double.class, scriptEngine.execute("return randDouble();",context).getClass());
	    assertEquals(true, scriptEngine.execute("d = randDouble(); return d>=0.0d && d<1.0d;",context));
	    assertEquals(true, scriptEngine.execute("d = randDouble(100d); return d>=0.0d && d<100d;",context));
	    assertEquals(true, scriptEngine.execute("d = randDouble(limit); return d>=0.0d && d<limit;",context));
	    assertEquals(false, scriptEngine.execute("d1 = randDouble(limit); d2 = randDouble(limit); return d1==d2;",context));
	    
	    //测试float随机数
	    assertEquals(Float.class, scriptEngine.execute("return randFloat();",context).getClass());
	    assertEquals(true, scriptEngine.execute("f = randFloat(); return f>=0.0f && f<1.0f;",context));
	    assertEquals(true, scriptEngine.execute("f = randFloat(100); return f>=0.0f && f<100f;",context));
	    assertEquals(true, scriptEngine.execute("f = randFloat(limit); return f>=0.0f && f<limit;",context));
	    assertEquals(false, scriptEngine.execute("f1 = randFloat(limit); f2 = randFloat(limit); return f1==f2;",context));
	    
	    //测试int随机数
	    assertEquals(Integer.class, scriptEngine.execute("return randInt();",context).getClass());
	    assertEquals(true, scriptEngine.execute("n = randInt(); return n>=0;",context));
	    assertEquals(true, scriptEngine.execute("n = randInt(10000); return n>=0 && n<10000;",context));
	    assertEquals(true, scriptEngine.execute("n = randInt(limit); return n>=0 && n<limit;",context));
	    assertEquals(false, scriptEngine.execute("n1 = randInt(limit); n2 = randInt(limit); return n1==n2;",context));
	    
	    //测试long随机数
	    assertEquals(Long.class, scriptEngine.execute("return randLong();",context).getClass());
	    assertEquals(true, scriptEngine.execute("n = randLong(); return n>=0;",context));
	    assertEquals(true, scriptEngine.execute("n = randLong(10000); return n>=0 && n<10000;",context));
	    assertEquals(true, scriptEngine.execute("n = randLong(limit); return n>=0 && n<limit;",context));
	    assertEquals(false, scriptEngine.execute("n1 = randLong(limit); n2 = randLong(limit); return n1==n2;",context));
	}
	
	public void testArray() throws Exception {
		ScriptEngine scriptEngine = new DefaultScriptEngine();
		ScriptContext context = new DefaultScriptContext();
		
		List<String> list = new ArrayList<String>();
		context.put("list", list);
		
		assertEquals(null,scriptEngine.execute("return randArray(list);", context));
		list.add("Ham");
		assertEquals("Ham",scriptEngine.execute("return randArray(list);", context));
		list.add("Ham2");
		System.out.println(scriptEngine.execute("return randArray(list);", context));
		
		String[] ss = new String[2];
		ss[0] = "ttt";
		ss[1] = "fff";
		context.put("ss", ss);
		System.out.println(scriptEngine.execute("return randArray(ss);", context));
		
	}
}
