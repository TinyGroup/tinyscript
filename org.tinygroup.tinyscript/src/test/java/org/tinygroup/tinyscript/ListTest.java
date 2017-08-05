package org.tinygroup.tinyscript;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.ComputeEngine;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.impl.DefaultComputeEngine;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;

/**
 * 序列测试用例
 * @author yancheng11334
 *
 */
public class ListTest extends TestCase {

	/**
	 * 测试序列基本操作
	 * @throws Exception 
	 */
	public void testBase() throws Exception{
		ComputeEngine  engine = new DefaultComputeEngine();
		List list = null;  
		list = (List) engine.execute("list = [1,2,3,5,8,13]; return list;");
		assertEquals(6, list.size());
		
		ScriptContext context = new DefaultScriptContext();
		context.put("list", list);
		
		assertEquals(true,engine.execute("a=21; list.add(a) ; return list.size()==7;",context));
		assertEquals(true,engine.execute("list.remove(6) ; return list.size()==6;",context));
		
		assertEquals(13,engine.execute("return list[6];",context));
		assertEquals(14,engine.execute("list[6]=14; return list[6];",context));
		
		assertEquals("李",engine.execute("ss=[\"赵\",\"钱\",\"孙\",\"李\",\"周\",\"吴\",\"郑\"]; return ss[4];"));
		assertEquals("王",engine.execute("ss=[\"赵\",\"钱\",\"孙\",\"李\",\"周\",\"吴\",\"郑\"]; ss[4]=\"王\"; return ss[4];"));
		assertEquals(3,engine.execute("ss=[\"赵\",\"钱\",\"孙\",\"李\",\"周\",\"吴\",\"郑\"]; nums=[1,2,3]; return ss[nums].size();"));
		
		//fill扩展函数
		assertEquals("a",engine.execute("ss=[\"赵\",\"钱\",\"孙\",\"李\",\"周\",\"吴\",\"郑\"]; ss.fill(\"a\"); return ss[1];"));
		assertEquals("a",engine.execute("ss=[\"赵\",\"钱\",\"孙\",\"李\",\"周\",\"吴\",\"郑\"]; ss.fill(\"a\"); return ss[7];"));
		assertEquals("a",engine.execute("ss=[\"赵\",\"钱\",\"孙\",\"李\",\"周\",\"吴\",\"郑\"]; ss.fill(1,5,\"a\"); return ss[1];"));
		assertEquals("a",engine.execute("ss=[\"赵\",\"钱\",\"孙\",\"李\",\"周\",\"吴\",\"郑\"]; ss.fill(1,5,\"a\"); return ss[5];"));
		assertEquals("赵",engine.execute("ss=[\"赵\",\"钱\",\"孙\",\"李\",\"周\",\"吴\",\"郑\"]; ss.fill(2,6,\"a\"); return ss[1];"));
		assertEquals("郑",engine.execute("ss=[\"赵\",\"钱\",\"孙\",\"李\",\"周\",\"吴\",\"郑\"]; ss.fill(1,5,\"a\"); return ss[7];"));
		
		//克隆
		assertEquals(true,engine.execute("newlist = list.clone(); list[1]=8; return newlist[1]==1;",context));
		
		//过滤
		assertEquals(false,engine.execute("list = [1,2,3,5,8,13]; result = list.filter(#>=10); list.add(0); return result==list;",context));  //返回原list
		assertEquals(true,engine.execute("list = [1,2,3,5,8,13]; result = list.remove(#>=10); list.add(0); return result==list;",context)); //返回新list
		//assertEquals(4,engine.execute("list.filter(\"~<10\"); return list.size();",context));
		
		assertEquals(5,engine.execute("list = [1,2,3,5,8,13]; newlist = list.filter(#<10); return newlist.size();",context));
		assertEquals(1,engine.execute("list = [1,2,3,5,8,13]; list.remove(#>=10); return list.size();",context));
		assertEquals(6,engine.execute("list = [1,2,3,5,8,13]; newlist = list.filter(1==1); return newlist.size();",context));
		assertEquals(0,engine.execute("list = [1,2,3,5,8,13]; list.remove(1==0); return list.size();",context));
	}
	
	/**
	 * 测试序列之间的交并差
	 * @throws Exception
	 */
	public void testListOperator() throws Exception{
		ComputeEngine  engine = new DefaultComputeEngine();
		assertEquals(1,engine.execute("a=[1,2,3,4]; b=[4,5,6]; c = a.intersect(b); return c.size();"));
		assertEquals(4,engine.execute("a=[1,2,3,4]; b=[4,5,6]; c = a.intersect(b); return c[1];"));
		assertEquals(0,engine.execute("a=[1,2,3,4]; b=[5,6,20]; c = a.intersect(b); return c.size();"));
		
		assertEquals(6,engine.execute("a=[1,2,3,4]; b=[4,5,6]; c = a.unite(b); return c.size();"));
		assertEquals(6,engine.execute("a=[1,2,3,4]; b=[4,5,6]; c = a.unite(b); return c[6];"));
		
		assertEquals(3,engine.execute("a=[1,2,3,4]; b=[4,5,6]; c = a.subtract(b); return c.size();"));
		assertEquals(3,engine.execute("a=[1,2,3,4]; b=[4,5,6]; c = a.subtract(b); return c[3];"));
		
		assertEquals(5,engine.execute("a=[1,2,3,4]; b=[4,5,6]; c = a.xor(b); return c.size();"));
		assertEquals(3,engine.execute("a=[1,2,3,4]; b=[4,5,6]; c = a.xor(b); return c[3];"));
	}
	
	/**
	 * 测试序列的排序
	 * @throws Exception
	 */
	public void testSort() throws Exception{
		ComputeEngine  engine = new DefaultComputeEngine();
		ScriptContext context = new DefaultScriptContext();
		//自然排序
		assertEquals("[-1, 4, 5, 6, 12, 20]",engine.execute("a=[6,20,-1,12,5,4]; a.sort(\"asc\");return a.toString();"));  //升序
		assertEquals("[20, 12, 6, 5, 4, -1]",engine.execute("a=[6,20,-1,12,5,4]; a.sort(\"desc\");return a.toString();"));  //降序
		assertEquals("[a, ab, b, d, dd]",engine.execute("a=[\"a\",\"dd\",\"d\",\"ab\",\"b\"]; a.sort(\"asc\");return a.toString();"));  //升序
		assertEquals("[dd, d, b, ab, a]",engine.execute("a=[\"a\",\"dd\",\"d\",\"ab\",\"b\"]; a.sort(\"desc\");return a.toString();"));  //降序
		
		//字段属性排序
		context.put("list", createUserList());
		engine.execute("list.sort(\"age,sex desc\");", context);
		
		List<User1> list = context.get("list");
		assertEquals("S001",list.get(0).getId());
		assertEquals("S003",list.get(3).getId());
		assertEquals("S004",list.get(4).getId());
		
		context.put("list", createUserList());
		engine.execute("list.sort(\"id\");", context);
	    list = context.get("list");
	    assertEquals("S006",list.get(4).getId());
	    assertEquals("S001",list.get(0).getId());
	    
	}
	
	@SuppressWarnings({ "rawtypes" })
	public void testOperator() throws Exception{
		ComputeEngine  engine = new DefaultComputeEngine();
		ScriptContext context = new DefaultScriptContext();
		List result = null;
		//测试通过+操作符执行并集操作
		result = (List) engine.execute("list1=[\"aaa\",\"bbb\",\"ccc\"]; list2=[\"bbb\",\"ddd\"]; return list1+list2;", context);
		assertEquals(4, result.size());
		assertEquals(true, result.contains("aaa"));
		assertEquals(true, result.contains("bbb"));
		assertEquals(true, result.contains("ccc"));
		assertEquals(true, result.contains("ddd"));
		
		//测试通过-操作符执行差集操作
		result = (List) engine.execute("list1=[\"aaa\",\"bbb\",\"ccc\"]; list2=[\"bbb\",\"ddd\"]; return list1-list2;", context);
		assertEquals(2, result.size());
		assertEquals(true, result.contains("aaa"));
		assertEquals(true, result.contains("ccc"));
		
		//测试通过&操作符执行交集操作
		result = (List) engine.execute("list1=[\"aaa\",\"bbb\",\"ccc\"]; list2=[\"bbb\",\"ddd\"]; return list1 & list2;", context);
		assertEquals(1, result.size());
		assertEquals(true, result.contains("bbb"));
		
		//测试通过^操作符执行异或操作
	    result = (List) engine.execute("list1=[\"aaa\",\"bbb\",\"ccc\"]; list2=[\"bbb\",\"ddd\"]; return list1 ^ list2;", context);
		assertEquals(3, result.size());
		assertEquals(true, result.contains("aaa"));
		assertEquals(true, result.contains("ccc"));
		assertEquals(true, result.contains("ddd"));
	}
	
	@SuppressWarnings({"rawtypes" })
	public void testOperator2() throws Exception{
		ComputeEngine  engine = new DefaultComputeEngine();
		List result = null;
		result = (List) engine.execute("list=[2,4,6,9,10]; number = 20; return list + number;");
		assertEquals(22, result.get(0));
		assertEquals(24, result.get(1));
		assertEquals(26, result.get(2));
		assertEquals(29, result.get(3));
		assertEquals(30, result.get(4));
		
		result = (List) engine.execute("list=[3,0,9,7]; number = 10; return list - number;");
		assertEquals(-7, result.get(0));
		assertEquals(-10, result.get(1));
		assertEquals(-1, result.get(2));
		assertEquals(-3, result.get(3));
		
		result = (List) engine.execute("list=[1.0,2.0,3.0,4.0]; number = 10; return list * number;");
		assertEquals(10.0f, result.get(0));
		assertEquals(20.0f, result.get(1));
		assertEquals(30.0f, result.get(2));
		assertEquals(40.0f, result.get(3));
		
		result = (List) engine.execute("list=[1.0,2.0,3.0,4.0]; number = 10; return list / number;");
		assertEquals(0.1f, result.get(0));
		assertEquals(0.2f, result.get(1));
		assertEquals(0.3f, result.get(2));
		assertEquals(0.4f, result.get(3));
		
		result = (List) engine.execute("list=[101,202]; number = 2; return list % number;");
		assertEquals(1, result.get(0));
		assertEquals(0, result.get(1));
	}
	
	private List<User1>  createUserList(){
		List<User1> list = new ArrayList<User1>();
		list.add(new User1("S001","John",10,1));
		list.add(new User1("S004","Air",95,2));
		list.add(new User1("S002","Bom",38,2));
		list.add(new User1("S003","Zam",38,1));
		list.add(new User1("S006","kam",38,2));
		return list;
	}
}
