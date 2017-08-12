package org.tinygroup.tinyscript;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

/**
 * 脚本引擎的表达式测试
 * 
 * @author yancheng11334
 * 
 */
public class ExpressionTest extends TestCase {

	private ScriptEngine scriptEngine;

	protected void setUp() throws Exception {
		scriptEngine = new DefaultScriptEngine();
		scriptEngine.start();
	}

	public void testNumberType() throws Exception {
		assertEquals(1, scriptEngine.execute("return +1;")); // 正整数
		assertEquals(-1, scriptEngine.execute("return -1;")); // 负整数
		assertEquals(+1.2f, scriptEngine.execute("return +1.2f;")); // 正float型浮点
		assertEquals(-1.2f, scriptEngine.execute("return -1.2f;")); // 负float型浮点
		assertEquals(+1.2d, scriptEngine.execute("return +1.2d;")); // 正double型浮点
		assertEquals(-1.2d, scriptEngine.execute("return -1.2d;")); // 负double型浮点
	}

	public void testNumberOperator1() throws Exception {
		// 基本的加、减、乘、除、求模、括号
		assertEquals(6, scriptEngine.execute("return 1+2+3;"));
		assertEquals(7, scriptEngine.execute("return 1+2*3;"));
		assertEquals(222, scriptEngine.execute("return 100-5-50+177;"));
		assertEquals(29, scriptEngine.execute("return 10000/4/100+4;"));
		assertEquals(3, scriptEngine.execute("return 2343%5;"));
		assertEquals(0, scriptEngine.execute("return 1/2;"));
		assertEquals(0, scriptEngine.execute("return -1/2;"));
		assertEquals(5, scriptEngine.execute("return 3*(2+2)-7;"));
		assertEquals(7, scriptEngine.execute("return (2+5)*(6-3)-7*2;"));
		
		//测试Long的加减乘除取模
		assertEquals(52L,scriptEngine.execute("return 50L+2L;"));
		assertEquals(48L,scriptEngine.execute("return 50L-2L;"));
		assertEquals(100L,scriptEngine.execute("return 50L*2L;"));
		assertEquals(25L,scriptEngine.execute("return 50L/2L;"));
		assertEquals(0L,scriptEngine.execute("return 50L%2L;"));
	}

	public void testNumberOperator2() throws Exception {
		// 浮点的加、减、乘、除
		assertEquals(3.0d, scriptEngine.execute("return 1.0f+2.0d;"));
		assertEquals(-1.0d, scriptEngine.execute("return 1.0f-2.0d;"));
		assertEquals(1.5d, scriptEngine.execute("return 1.0d*2.5d-1.0d;"));
		assertEquals(0.5d, scriptEngine.execute("return 1.0d/2;"));
	}

	public void testLogic1() throws Exception {
		// 逻辑运算：与、或、非、异或
		assertEquals(true, scriptEngine.execute("return !false;"));
		assertEquals(false, scriptEngine.execute("return !true;"));
		assertEquals(~2, scriptEngine.execute("return ~2;"));
		assertEquals(128 & 129, scriptEngine.execute("return 128&129;"));
		assertEquals(128 | 129, scriptEngine.execute("return 128|129;"));
		assertEquals(15 ^ 2, scriptEngine.execute("return 15^2;"));
	}

	public void testLogic2() throws Exception {

		assertEquals(false, scriptEngine.execute("return 1=='1';"));
		assertEquals(false, scriptEngine.execute("return 1==0 && 1==1 ;"));
		assertEquals(false, scriptEngine.execute("return 1==\"1\";"));
		assertEquals(true, scriptEngine.execute("return 1==int(\"1\");"));
		assertEquals(true, scriptEngine.execute(" return 1==0 || 1==1 ;"));
		assertEquals(true,
				scriptEngine.execute(" return 1==1 || 6>7 || 8!=8 ;"));
		assertEquals(false,
				scriptEngine
						.execute("return 1==1 && a!=null && a.length()>8 ;")); // 短路与,不执行a.length()>8
		assertEquals(true,
				scriptEngine
						.execute("return 1==0 || a==null || a.length()>8 ;")); // 短路或,不执行a.length()>8
		
		assertEquals(true, scriptEngine.execute(" list1=[1,2,3]; list2=[1,2,3]; return list1==list2;"));
	}

	public void testLogic3() throws Exception {
		// 大小判断
		assertEquals(true, scriptEngine.execute("return  1==1;"));
		assertEquals(false, scriptEngine.execute("return  1==0;"));
		assertEquals(true, scriptEngine.execute("return  1!=0;"));
		assertEquals(false, scriptEngine.execute("return   0!=0;"));
		assertEquals(true, scriptEngine.execute("return   200>199;"));
		assertEquals(true, scriptEngine.execute("return   200>=200;"));
		assertEquals(false, scriptEngine.execute("return   200<199;"));
		assertEquals(true, scriptEngine.execute("return   200<=200;"));
		assertEquals(true, scriptEngine.execute("return   true || false;"));
		assertEquals(false, scriptEngine.execute("return  false && true;"));
	}
	

	public void testThreeElement() throws Exception {
		// 三元表达式
		assertEquals("yes",
				scriptEngine.execute("return 1+1>=1?\"yes\":\"no\";"));
		assertEquals("no", scriptEngine.execute("return 1+1<1?\"yes\":\"no\";"));
		assertEquals(true, scriptEngine.execute("return 2==5-3?true:false;"));
		assertEquals(false, scriptEngine.execute("return 2!=5-3?true:false;"));
		//三元表达式嵌套
		assertEquals('c', scriptEngine.execute("a=10;b=20; return a==20?'a':(b==10?'b':'c');"));
		assertEquals('b', scriptEngine.execute("a=10;b=10; return a==20?'a':(b==10?'b':'c');"));
		assertEquals('a', scriptEngine.execute("a=20;b=20; return a==20?'a':(b==10?'b':'c');"));
	}

	public void testMove() throws Exception {
		// 移位运算
		assertEquals(8 >> 2, scriptEngine.execute("return  8>>2; "));
		assertEquals(8 << 2, scriptEngine.execute("return  8<<2; "));
		assertEquals(-121 >>> 4, scriptEngine.execute("return -121 >>> 4; "));
	}

	public void testSelf() throws Exception {
		// 自增和自减
		ScriptContext context = new DefaultScriptContext();
		context.put("i", 1);
		assertEquals(1, scriptEngine.execute("return  i++ ;", context));
		assertEquals(2, context.get("i"));

		context.put("i", 1);
		assertEquals(2, scriptEngine.execute("return  ++i ;", context));
		assertEquals(2, context.get("i"));

		context.put("i", 3);
		assertEquals(3, scriptEngine.execute("return  i-- ;", context));
		assertEquals(2, context.get("i"));

		context.put("i", 3);
		assertEquals(2, scriptEngine.execute("return  --i ;", context));
		assertEquals(2, context.get("i"));
	}
}
