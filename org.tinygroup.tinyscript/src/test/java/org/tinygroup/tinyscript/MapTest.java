package org.tinygroup.tinyscript;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.tinyscript.ComputeEngine;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.impl.DefaultComputeEngine;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;

import junit.framework.TestCase;

/**
 * 测试map
 * @author yancheng11334
 *
 */
public class MapTest extends TestCase {

	public void testBase() throws Exception{
		
		ComputeEngine  engine = new DefaultComputeEngine();
		
		assertEquals("John", engine.execute(" map={\"name\":\"John\",\"age\":20}; return map[\"name\"];"));
		assertEquals(50, engine.execute(" map={\"name\":\"John\",\"age\":20};  map[\"age\"] =50; return map[\"age\"];"));
		
		assertEquals("John", engine.execute(" map={\"name\":\"John\",\"age\":20}; return map.name;"));
		assertEquals(50, engine.execute(" map={\"name\":\"John\",\"age\":20};  map.age =50; return map.age;"));
	}
	
	public void testUnion() throws Exception{
		ComputeEngine  engine = new DefaultComputeEngine();
		ScriptContext context = new DefaultScriptContext();
		Map<String,String> m1 = new HashMap<String,String>();
		m1.put("1", "aaa");
		m1.put("2", "bbb");
		m1.put("3", "ccc");
		Map<String,String> m2 = new HashMap<String,String>();
		m2.put("2", "bbb");
		m2.put("4", "ddd");
		
		context.put("m1", m1);
		context.put("m2", m2);
		Map map = (Map) engine.execute("return m1.unite(m2);", context);
		
		assertEquals(4,map.size());
		assertEquals(true,map.containsKey("1"));
		assertEquals(true,map.containsKey("2"));
		assertEquals(true,map.containsKey("3"));
		assertEquals(true,map.containsKey("4"));
		
		//测试操作符
        map = (Map) engine.execute("return m1+m2;", context);	
		assertEquals(4,map.size());
		assertEquals(true,map.containsKey("1"));
		assertEquals(true,map.containsKey("2"));
		assertEquals(true,map.containsKey("3"));
		assertEquals(true,map.containsKey("4"));
	}
	
	public void testIntersection() throws Exception{
		ComputeEngine  engine = new DefaultComputeEngine();
		ScriptContext context = new DefaultScriptContext();
		
		Map<String,String> m1 = new HashMap<String,String>();
		m1.put("1", "aaa");
		m1.put("2", "bbb");
		m1.put("3", "ccc");
		Map<String,String> m2 = new HashMap<String,String>();
		m2.put("2", "bbb");
		m2.put("4", "ddd");
		
		context.put("m1", m1);
		context.put("m2", m2);
		Map map = (Map) engine.execute("return m1.intersect(m2);", context);
		assertEquals(1,map.size());
		assertEquals(true,map.containsKey("2"));
		
		//测试操作符&
		map = (Map) engine.execute("return m1 & m2;", context);
		assertEquals(1,map.size());
		assertEquals(true,map.containsKey("2"));
	}
	
	public void testSubtract() throws Exception{
		ComputeEngine  engine = new DefaultComputeEngine();
		ScriptContext context = new DefaultScriptContext();
		
		Map<String,String> m1 = new HashMap<String,String>();
		m1.put("1", "aaa");
		m1.put("2", "bbb");
		m1.put("3", "ccc");
		Map<String,String> m2 = new HashMap<String,String>();
		m2.put("2", "bbb");
		m2.put("4", "ddd");
		
		context.put("m1", m1);
		context.put("m2", m2);
		Map map = (Map) engine.execute("return m1.subtract(m2);", context);
		assertEquals(2,map.size());
		assertEquals(true,map.containsKey("1"));
		assertEquals(true,map.containsKey("3"));
		
		map = (Map) engine.execute("return m1-m2;", context);
		assertEquals(2,map.size());
		assertEquals(true,map.containsKey("1"));
		assertEquals(true,map.containsKey("3"));
	}
	
	public void testXor() throws Exception{
		ComputeEngine  engine = new DefaultComputeEngine();
		ScriptContext context = new DefaultScriptContext();
		
		Map<String,String> m1 = new HashMap<String,String>();
		m1.put("1", "aaa");
		m1.put("2", "bbb");
		m1.put("3", "ccc");
		Map<String,String> m2 = new HashMap<String,String>();
		m2.put("2", "bbb");
		m2.put("4", "ddd");
		
		context.put("m1", m1);
		context.put("m2", m2);
		
		Map map = (Map) engine.execute("return m1.xor(m2);", context);
		assertEquals(3,map.size());
		assertEquals(true,map.containsKey("1"));
		assertEquals(true,map.containsKey("3"));
		assertEquals(true,map.containsKey("4"));
		
		map = (Map) engine.execute("return m1 ^ m2;", context);
		assertEquals(3,map.size());
		assertEquals(true,map.containsKey("1"));
		assertEquals(true,map.containsKey("3"));
		assertEquals(true,map.containsKey("4"));
	}
	
	public void testOperator2() throws Exception{
		ComputeEngine  engine = new DefaultComputeEngine();
		ScriptContext context = new DefaultScriptContext();
		
		Map result = null;
		
		result = (Map) engine.execute(" m={\"k1\":10,\"k2\":20,\"k3\":30}; return m+5;", context);
		assertEquals(true,result.containsValue(15));
		assertEquals(true,result.containsValue(25));
		assertEquals(true,result.containsValue(35));
		

		result = (Map) engine.execute(" m={\"k1\":10,\"k2\":20,\"k3\":30}; return m-5;", context);
		assertEquals(true,result.containsValue(5));
		assertEquals(true,result.containsValue(15));
		assertEquals(true,result.containsValue(25));
		
		result = (Map) engine.execute(" m={\"k1\":1.0f,\"k2\":2.0f,\"k3\":3.0f,\"k4\":4.0f}; return m*10;", context);
		assertEquals(true,result.containsValue(10.0f));
		assertEquals(true,result.containsValue(20.0f));
		assertEquals(true,result.containsValue(30.0f));
		assertEquals(true,result.containsValue(40.0f));
		
		result = (Map) engine.execute(" m={\"k1\":1.0f,\"k2\":2.0f,\"k3\":3.0f,\"k4\":4.0f}; return m/10;", context);
		assertEquals(true,result.containsValue(0.1f));
		assertEquals(true,result.containsValue(0.2f));
		assertEquals(true,result.containsValue(0.3f));
		assertEquals(true,result.containsValue(0.4f));
		
		result = (Map) engine.execute(" m={\"k1\":333,\"k2\":444}; return m%2;", context);
		assertEquals(true,result.containsValue(0));
		assertEquals(true,result.containsValue(1));
	}
}
