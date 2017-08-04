package org.tinygroup.tinyscript;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

/**
 * lambda表达式测试用例
 * @author yancheng11334
 *
 */
public class LambdaTest extends TestCase {

	private ScriptEngine scriptEngine;

	protected void setUp() throws Exception {
		scriptEngine = new DefaultScriptEngine();
		scriptEngine.start();
	}

	/**
	 * 测试带返回值的lambda表达式
	 * @throws Exception
	 */
	public void testExpression() throws Exception {
		//有名称的lambda函数测试
		
		//lambda函数
		assertEquals(3, scriptEngine.execute(" add(a,b)->{ a+b; };  return add(1,2);")); 
		
		//上下文无关
		assertEquals(7, scriptEngine.execute(" a=1;b=2;c=3;d=4; add(a,b)->{ a+b; };  return add(c,d);")); 
		
		//lambda函数嵌套
		assertEquals(12, scriptEngine.execute(" add(a,b)->{ a+b; }; subtract(a,b)->{ a-b; }; multiply(a,b)->{ a*b; }; return add(multiply(2,5),subtract(8,6));"));  
		
		//lambda函数与内置函数嵌套(计算某个自然数绝对值的平方根)
		assertEquals(2, scriptEngine.execute(" countNumber(num)->{ int(sqrt(abs(num))); };  return countNumber(4);")); 
		assertEquals(2, scriptEngine.execute(" countNumber(num)->{ int(sqrt(abs(num))); };  return countNumber(-4);")); 
		assertEquals(1, scriptEngine.execute(" countNumber(num)->{ int(sqrt(abs(num))); };  return countNumber(3);")); 
		
	}
	
//	public void testForEach() throws Exception {
//		ScriptContext context = new DefaultScriptContext();
//		List<String> list = new ArrayList<String>();
//		context.put("list", list);
//		scriptEngine.execute("list.forEach(8,it->{System.out.println(it);});",context);
//	}
	
	/**
	 * new Thread(new Runnable() {  
         @Override  
         public void run() {  
            System.out.println("Before Java8, too much code for too little to do");  
         }  
        }).start();  
	 * @throws Exception
	 */
	public void testRunnable() throws Exception {
		//系统默认执行
		//scriptEngine.execute("() -> System.out.println(\"In tinyScript, Lambda expression rocks !!\");");
		scriptEngine.execute(" new Thread( () -> System.out.println(\"In tinyScript, Lambda expression rocks !!\") ).start(); ");
		Thread.sleep(1000); //不可以删掉，保证主线程结束比较晚
	}
	
	public void testComparator() throws Exception {
		List list = (List)scriptEngine.execute("import java.util.*; list = [3,4,5,2,1]; Collections.sort(list); return list;");
		System.out.println(list); //自然排序,从小到大
		assertEquals(1,list.get(0));
		assertEquals(5,list.get(4));
		
		list = (List)scriptEngine.execute("import java.util.*; list = [3,4,5,2,1]; Collections.sort(list,(o1,o2)->{if(o1>o2){ return -1; }else if(o1<o2){ return 1; }else{ return 0;}}); return list;");
		System.out.println(list); //从大到小
		assertEquals(5,list.get(0));
		assertEquals(1,list.get(4));
	}
	
}
