package org.tinygroup.tinyscript;

import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

import junit.framework.TestCase;

/**
 * 测试脚本引擎的集合功能
 * 
 * @author yancheng11334
 * 
 */
public class CollectionTest extends TestCase {

	private ScriptEngine scriptEngine;

	protected void setUp() throws Exception {
		scriptEngine = new DefaultScriptEngine();
		scriptEngine.start();
	}

	public void testList() throws Exception {
		List list = (List) scriptEngine
				.execute("list = new java.util.ArrayList(); return list;");
		assertEquals(true, list.isEmpty());
        
	
		list = (List) scriptEngine.execute("list = [1,2,3,4,5]; return list;");
		assertEquals(5, list.size());
		assertEquals(1, list.get(0));

		list = (List) scriptEngine
				.execute("list = [\"boy\",\"girl\"]; return list;");
		assertEquals(2, list.size());
		assertEquals("girl", list.get(1));
		
		//测试集合递归子元素方法
		list = (List) scriptEngine
				.execute("list = [\"boy\",\"girl\"]; return list.toUpperCase();");
		assertEquals("BOY", list.get(0));
		assertEquals("GIRL", list.get(1));
		
		//测试集合的集合
		list = (List) scriptEngine.execute("boys = [\"Tom\",\"John\"]; girls =[\"meimei\",\"lili\"]; list=[boys,girls];return list.toUpperCase();");
		List boys = (List) list.get(0);
		List girls = (List) list.get(1);
		assertEquals("TOM", boys.get(0));
		assertEquals("MEIMEI", girls.get(0));
	}

	public void testMap() throws Exception {
		Map map = (Map) scriptEngine
				.execute("map = new java.util.HashMap(); return map;");
		assertEquals(true, map.isEmpty());


		map = (Map) scriptEngine
				.execute("map = {\"key1\":12+3,\"key2\":\"china\"}; return map;");
		assertEquals(2, map.size());
		assertEquals(15, map.get("key1"));
		assertEquals("china", map.get("key2"));
		
		//测试集合递归子元素方法
		map = (Map) scriptEngine
				.execute("map = {\"key1\":\"english\",\"key2\":\"china\"}; return map.substring(2);");
		assertEquals("glish", map.get("key1"));
		assertEquals("ina", map.get("key2"));

	}
	
	public void testArray() throws Exception {
		Object[] array = (Object[]) scriptEngine.execute("array = {1,1+2,1*5+1}; array[1]=8; return array;");
		assertEquals(3, array.length);
		
		assertEquals(1, array[0]);
		assertEquals(8, array[1]);
		assertEquals(6, array[2]);
		
		//测试空数组
		array = (Object[]) scriptEngine.execute("array = {}; return array;");
		assertEquals(0, array.length);
	}

	public void testUser() throws Exception {
		ScriptContext context = new DefaultScriptContext();
		context.put("user", new User("123"));
		// System.out.println("["+scriptEngine.execute("return user.getName();",
		// context)+"]");
		// System.out.println("["+scriptEngine.execute("return user.name;",
		// context)+"]");
		assertEquals("123",
				scriptEngine.execute("return user.getName();", context));
		assertEquals("123", scriptEngine.execute("return user.name;", context));
	}

	public void testIndex() throws Exception {
		ScriptContext context = new DefaultScriptContext();
		assertEquals('1', scriptEngine.execute("return \"123\"[0];", context));
		assertEquals('1', scriptEngine.execute(
				"return \"123\".toCharArray()[0];", context));
	}

	public class User {
		private String name;

		public User(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

}
