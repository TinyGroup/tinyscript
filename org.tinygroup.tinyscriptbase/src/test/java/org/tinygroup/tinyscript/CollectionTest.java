package org.tinygroup.tinyscript;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

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
		
		//测试集合的递归子元素属性
		List<User> users = new ArrayList<User>();
		users.add(new User("hello"));
		users.add(new User("tom"));
		users.add(new User("none"));
		ScriptContext context = new DefaultScriptContext();
		context.put("users", users);
		list = (List) scriptEngine.execute("return users.name;",context);
		assertEquals("hello", list.get(0));
		assertEquals("tom", list.get(1));
		assertEquals("none", list.get(2));
		
		//测试in和not in指令
		assertEquals(true, scriptEngine.execute("list=[\"Tom\",\"John\"]; return \"John\" in list;",context));
		assertEquals(true, scriptEngine.execute("list=[\"Tom\",\"John\"]; return !(\"None\" in list);",context));
		assertEquals(true, scriptEngine.execute("list=[\"Tom\",\"John\"]; return \"None\" not  in list;",context));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
		
		//测试集合key优先于子元素属性
		assertEquals(15, scriptEngine.execute("map = {\"key1\":12+3,\"key2\":\"china\"}; return map.key1;"));
		
		//测试遍历子元素属性
		ScriptContext context = new DefaultScriptContext();
		Map smap = new HashMap();
		smap.put("user1", new User("hello"));
		smap.put("user2", new User("world"));
		context.put("smap", smap);
		map = (Map) scriptEngine
				.execute(" return smap.name;",context);
		assertEquals("hello", map.get("user1"));
		assertEquals("world", map.get("user2"));
		
		//测试in和not in指令
		assertEquals(true, scriptEngine.execute("map = {\"key1\":\"english\",\"key2\":\"china\"}; return \"key1\" in map;",context));
		assertEquals(true, scriptEngine.execute("map = {\"key1\":\"english\",\"key2\":\"china\"}; return !(\"key3\" in map);",context));
		assertEquals(true, scriptEngine.execute("map = {\"key1\":\"english\",\"key2\":\"china\"}; return \"key3\" not in map;",context));

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
		
		//测试数组的遍历子属性
		ScriptContext context = new DefaultScriptContext();
		User[] users = new User[3];
		users[0] = new User("hello");
		users[1] = new User("tom");
		users[2] = new User("none");
		context.put("users", users);
		array = (Object[]) scriptEngine.execute(" return users.name;",context);
		assertEquals("hello", array[0]);
		assertEquals("tom", array[1]);
		assertEquals("none",array[2]);
		
		//测试in和not in指令
		assertEquals(true, scriptEngine.execute("array={\"Tom\",\"John\"}; return \"John\" in array;",context));
		assertEquals(true, scriptEngine.execute("array={\"Tom\",\"John\"}; return !(\"None\"  in array);",context));
		assertEquals(true, scriptEngine.execute("array={\"Tom\",\"John\"}; return \"None\" not  in array;",context));
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
