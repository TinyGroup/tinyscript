package org.tinygroup.tinyscript;

import junit.framework.TestCase;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

import java.util.List;
import java.util.Map;

/**
 * 测试脚本引擎的集合功能
 * @author yancheng11334
 *
 */
public class PropertyTest extends TestCase{

	 private ScriptEngine scriptEngine;
		
		protected void setUp() throws Exception {
			scriptEngine = new DefaultScriptEngine();
			scriptEngine.start();
	    }
		
		public void testUser()  throws Exception{
			ScriptContext context=new DefaultScriptContext();
			context.put("user",new User("abc"));
			assertEquals("abc", scriptEngine.execute("return user.getName();",context));
			assertEquals(true, scriptEngine.execute("return user.isGender();",context));
			assertEquals(true, scriptEngine.execute("return user.gender;",context));
			assertEquals(11, scriptEngine.execute("return user.age;",context));
			assertEquals(11, scriptEngine.execute("return user.getAge();",context));
			assertEquals("abc", scriptEngine.execute("return user.name;",context));
			assertEquals(11, scriptEngine.execute("return user.age;",context));
			assertEquals(null, scriptEngine.execute("return user1.name;", context));
		}

		static public class User {
			public static int age=11;
			private String name;
			private boolean gender=true;

			public static int getAge() {
				return age;
			}

			public static void setAge(int age) {
				User.age = age;
			}

			public boolean isGender() {
				return gender;
			}

			public void setGender(boolean gender) {
				this.gender = gender;
			}

			public User(String name){
				this.name=name;
			}
			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}
		}
}
