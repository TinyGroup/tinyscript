package org.tinygroup.tinyscript;

import java.util.List;

import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.impl.DataSetBean;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.impl.DefaultTinyScriptEngine;

import junit.framework.TestCase;

/**
 * 测试序表转List用例
 * @author yancheng11334
 *
 */
public class DataSetToListTest extends TestCase {

	 /**
	  * 不提供class信息，直接转换map的序列
	  * @throws Exception
	  */
	 @SuppressWarnings("unchecked")
	public void testWithoutClass() throws Exception{
		 //无参场景
		 ScriptEngine  engine = new DefaultTinyScriptEngine();
		 List<DataSetBean> list = (List<DataSetBean>) engine.execute("ds = readCsv(\"src/test/resources/customer1.csv\",\"gbk\"); return ds.toList();");
		 assertNotNull(list);
		 assertEquals(3, list.size());
		 DataSetBean c1 = list.get(1);
		 assertEquals(7, c1.size());
		 assertEquals("卫东能", c1.get("user_name"));
		 assertEquals("28", c1.get("user_age"));
	 }
	
	/**
	 * 提供class信息，动态计算序表字段和值对象属性的关联，并转换值对象序列
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void testWithClass() throws Exception{
		 
		 ScriptEngine  engine = new DefaultTinyScriptEngine();
		 ScriptContext context = new DefaultScriptContext();
		 //分离读取DataSet操作，简化后面的脚本操作，突出主题
		 DataSet ds = (DataSet) engine.execute("return readCsv(\"src/test/resources/customer1.csv\",\"gbk\");");
		 context.put("ds", ds);
		 
		 //测试类名
		 List<Customer1> list = (List<Customer1>) engine.execute("return ds.toList(\"org.tinygroup.tinyscript.Customer1\");",context);
		 assertNotNull(list);
		 assertEquals(3, list.size());
		 
		 Customer1 c1 = list.get(1);
		 assertEquals("卫东能", c1.getUserName());
		 assertEquals("28", c1.getUserAge());
		 assertEquals("TRUE", c1.getOnlineTag());
		 
		 //测试类
		 context.put("clazz", Customer1.class);
		 list = (List<Customer1>) engine.execute("return ds.toList(clazz);",context);
		 assertNotNull(list);
		 assertEquals(3, list.size());
		 c1 = list.get(1);
		 assertEquals("卫东能", c1.getUserName());
		 assertEquals("28", c1.getUserAge());
		 assertEquals("TRUE", c1.getOnlineTag());
	 }
	
	/**
	 * 提供class信息，通过rename函数修改列名，并转换值对象序列
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void testWithClass2() throws Exception{
		ScriptEngine  engine = new DefaultTinyScriptEngine();
		ScriptContext context = new DefaultScriptContext();
		//分离读取DataSet操作，简化后面的脚本操作，突出主题
		DataSet ds = (DataSet) engine.execute("return readCsv(\"src/test/resources/customer1.csv\",\"gbk\").rename(\"user_name\",\"name\").rename(\"user_pass\",\"password\").rename(\"user_sex\",\"sex\").rename(\"user_age\",\"age\").boolean(\"online_tag\");");
		context.put("ds", ds);
		
		List<Customer2> list = (List<Customer2>) engine.execute("return ds.toList(\"org.tinygroup.tinyscript.Customer2\");",context);
		assertNotNull(list);
		assertEquals(3, list.size());
		
		Customer2 c2 = list.get(1);
		assertEquals("卫东能", c2.getName());
		assertEquals(28, c2.getAge());
		assertEquals(true, c2.isOnlineTag());
	}
	
	/**
	 * 提供class信息和指定关联，并转换值对象序列
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void testWithClassAndRelation() throws Exception{
		ScriptEngine  engine = new DefaultTinyScriptEngine();
		ScriptContext context = new DefaultScriptContext();
		//分离读取DataSet操作，简化后面的脚本操作，突出主题
		DataSet ds = (DataSet) engine.execute("return readCsv(\"src/test/resources/customer1.csv\",\"gbk\").boolean(\"online_tag\");");
		context.put("ds", ds);
		
		//测试类名
		List<Customer2> list = (List<Customer2>) engine.execute("return ds.toList(\"org.tinygroup.tinyscript.Customer2\",{\"user_name\":\"name\",\"user_pass\":\"password\",\"user_sex\":\"sex\",\"mobile\":\"mobile\",\"address\":\"address\",\"online_tag\":\"onlineTag\",\"user_age\":\"age\"});",context);
		assertNotNull(list);
		assertEquals(3, list.size());
		
		Customer2 c2 = list.get(1);
		assertEquals("卫东能", c2.getName());
		assertEquals(28, c2.getAge());
		assertEquals(true, c2.isOnlineTag());
		
		//测试类
		context.put("clazz", Customer2.class);
		list = (List<Customer2>) engine.execute("return ds.toList(clazz,{\"user_name\":\"name\",\"user_pass\":\"password\",\"user_sex\":\"sex\",\"mobile\":\"mobile\",\"address\":\"address\",\"online_tag\":\"onlineTag\",\"user_age\":\"age\"});",context);
		assertNotNull(list);
		assertEquals(3, list.size());
		c2 = list.get(1);
	    assertEquals("卫东能", c2.getName());
	    assertEquals(28, c2.getAge());
	    assertEquals(true, c2.isOnlineTag());
	}
	
	/**
	 * 调用自定义lambda创建对象
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void testWithLambda() throws Exception{
		ScriptEngine  engine = new DefaultTinyScriptEngine();
		ScriptContext context = new DefaultScriptContext();
		//分离读取DataSet操作，简化后面的脚本操作，突出主题
		DataSet ds = (DataSet) engine.execute("return readCsv(\"src/test/resources/customer1.csv\",\"gbk\");");
		context.put("ds", ds);
		
		//通过编程代码块，可以实现最复杂的业务场景
		List<Customer2> list = (List<Customer2>) engine.execute("return ds.toList(()->{" +
		"customer = new org.tinygroup.tinyscript.Customer2();"+
		"customer.name = user_name;"+
		"customer.password = user_pass;"+
		"customer.sex = int(user_sex);"+
		"customer.age = int(user_age);"+
		"customer.mobile = mobile;"+
		"customer.address = address;"+
		"customer.onlineTag = boolean(online_tag);"+
		"return customer;"+
		"});",context);
		assertNotNull(list);
		assertEquals(3, list.size());
		
		Customer2 c2 = list.get(1);
		assertEquals("卫东能", c2.getName());
		assertEquals(28, c2.getAge());
		assertEquals(true, c2.isOnlineTag());
	}
}
