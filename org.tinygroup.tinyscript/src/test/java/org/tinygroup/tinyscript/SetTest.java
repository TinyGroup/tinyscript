package org.tinygroup.tinyscript;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.ComputeEngine;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.impl.DefaultComputeEngine;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;

/**
 * 测试Set
 * @author yancheng11334
 *
 */
public class SetTest  extends TestCase {

	public void testUnion() throws Exception{
		ComputeEngine  engine = new DefaultComputeEngine();
		ScriptContext context = new DefaultScriptContext();
		Set<String> m1 = new HashSet<String>();
		m1.add("aaa");
		m1.add("bbb");
		m1.add("ccc");
		Set<String> m2 = new HashSet<String>();
		m2.add("bbb");
		m2.add("ddd");
		
		context.put("m1", m1);
		context.put("m2", m2);
		Set set = (Set) engine.execute("return m1.unite(m2);", context);
		
		assertEquals(4,set.size());
		assertEquals(true,set.contains("aaa"));
		assertEquals(true,set.contains("bbb"));
		assertEquals(true,set.contains("ccc"));
		assertEquals(true,set.contains("ddd"));
		
		//测试操作符+
		set = (Set)engine.execute("return m1+m2;", context);
		assertEquals(4,set.size());
		assertEquals(true,set.contains("aaa"));
		assertEquals(true,set.contains("bbb"));
		assertEquals(true,set.contains("ccc"));
		assertEquals(true,set.contains("ddd"));
	}
	
	public void testIntersection() throws Exception{
		ComputeEngine  engine = new DefaultComputeEngine();
		ScriptContext context = new DefaultScriptContext();
		Set<String> m1 = new HashSet<String>();
		m1.add("aaa");
		m1.add("bbb");
		m1.add("ccc");
		Set<String> m2 = new HashSet<String>();
		m2.add("bbb");
		m2.add("ddd");
		
		context.put("m1", m1);
		context.put("m2", m2);
		Set set = (Set) engine.execute("return m1.intersect(m2);", context);
		
		assertEquals(1,set.size());
		assertEquals(true,set.contains("bbb"));
		assertEquals(false,set.contains("ddd"));
		
		//测试操作符&
        set = (Set) engine.execute("return m1 & m2;", context);
		
		assertEquals(1,set.size());
		assertEquals(true,set.contains("bbb"));
	}
	
	public void testSubtract() throws Exception{
		ComputeEngine  engine = new DefaultComputeEngine();
		ScriptContext context = new DefaultScriptContext();
		Set<String> m1 = new HashSet<String>();
		m1.add("aaa");
		m1.add("bbb");
		m1.add("ccc");
		Set<String> m2 = new HashSet<String>();
		m2.add("bbb");
		m2.add("ddd");
		
		context.put("m1", m1);
		context.put("m2", m2);
		Set set = (Set) engine.execute("return m1.subtract(m2);", context);
		
		assertEquals(2,set.size());
		assertEquals(true,set.contains("aaa"));
		assertEquals(false,set.contains("bbb"));
		assertEquals(true,set.contains("ccc"));
		
		//测试操作符-
        set = (Set) engine.execute("return m1-m2;", context);
		
		assertEquals(2,set.size());
		assertEquals(true,set.contains("aaa"));
		assertEquals(false,set.contains("bbb"));
		assertEquals(true,set.contains("ccc"));
	}
	
	public void testXor() throws Exception{
		ComputeEngine  engine = new DefaultComputeEngine();
		ScriptContext context = new DefaultScriptContext();
		Set<String> m1 = new HashSet<String>();
		m1.add("aaa");
		m1.add("bbb");
		m1.add("ccc");
		Set<String> m2 = new HashSet<String>();
		m2.add("bbb");
		m2.add("ddd");
		
		context.put("m1", m1);
		context.put("m2", m2);
		Set set = (Set) engine.execute("return m1.xor(m2);", context);
		
		assertEquals(3,set.size());
		assertEquals(true,set.contains("aaa"));
		assertEquals(true,set.contains("ccc"));
		assertEquals(true,set.contains("ddd"));
		
		set = (Set) engine.execute("return m1 ^ m2;", context);
			
		assertEquals(3,set.size());
		assertEquals(true,set.contains("aaa"));
		assertEquals(true,set.contains("ccc"));
		assertEquals(true,set.contains("ddd"));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testOperator2() throws Exception{
		ComputeEngine  engine = new DefaultComputeEngine();
		ScriptContext context = new DefaultScriptContext();
		Set m1 = new HashSet();
		m1.add(2);
		m1.add(4);
		m1.add(6);
		m1.add(9);
		m1.add(10);
		context.put("set", m1);
		
		Set result = (Set) engine.execute("return set + 20;", context);
		
		assertEquals(true,result.contains(22));
		assertEquals(true,result.contains(24));
		assertEquals(true,result.contains(26));
		assertEquals(true,result.contains(29));
		assertEquals(true,result.contains(30));
		
		m1.clear();
		m1.add(3);
		m1.add(0);
		m1.add(9);
		m1.add(7);
		
		result = (Set) engine.execute("return set - 10;", context);
		assertEquals(true,result.contains(-7));
		assertEquals(true,result.contains(-10));
		assertEquals(true,result.contains(-1));
		assertEquals(true,result.contains(-3));
		
		m1.clear();
		m1.add(1.0f);
		m1.add(2.0f);
		m1.add(3.0f);
		m1.add(4.0f);
		
		result = (Set) engine.execute("return set * 10;", context);
		assertEquals(true,result.contains(10.0f));
		assertEquals(true,result.contains(20.0f));
		assertEquals(true,result.contains(30.0f));
		assertEquals(true,result.contains(40.0f));
		
		result = (Set) engine.execute("return set / 10;", context);
		assertEquals(true,result.contains(0.1f));
		assertEquals(true,result.contains(0.2f));
		assertEquals(true,result.contains(0.3f));
		assertEquals(true,result.contains(0.4f));
		
		m1.clear();
		m1.add(3);
		m1.add(4);
		
		result = (Set) engine.execute("return set % 2;", context);
		assertEquals(true,result.contains(0));
		assertEquals(true,result.contains(1));
	}
}
