package org.tinygroup.tinyscript;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

/**
 * 脚本引擎的指令测试
 * 
 * @author yancheng11334
 * 
 */
public class DirectiveTest extends TestCase {

	private ScriptEngine scriptEngine;

	protected void setUp() throws Exception {
		scriptEngine = new DefaultScriptEngine();
		scriptEngine.start();
	}

	public void testIf() throws Exception {
		// 测试if指令
		assertEquals(60,
				scriptEngine.execute("score=100; if(score>90){ return 60; } "));
		assertEquals(
				"ok",
				scriptEngine
						.execute("score=100; if(score>90){ return \"ok\"; } else{ return \"no\"; }"));
		assertEquals(
				"no",
				scriptEngine
						.execute("score=30; if(score>90){ return \"ok\"; } else{ return \"no\"; }"));

		String rule = "if(score>=90){ return \"优秀\"; } elseif(score>=80){ return \"良好\"; } elseif(score>=60){ return \"及格\"; } else{ return \"不及格\"; }";
		assertEquals("优秀", scriptEngine.execute("score=95; " + rule));
		assertEquals("良好", scriptEngine.execute("score=80; " + rule));
		assertEquals("及格", scriptEngine.execute("score=75; " + rule));
		assertEquals("不及格", scriptEngine.execute("score=30; " + rule));
	}

	public void testDoWhile() throws Exception {
		// 测试do while指令
		assertEquals(
				55,
				scriptEngine
						.execute("total=0; i=10; do{total=total+i; i--; }while(i>0); return total;"));
		assertEquals(
				0,
				scriptEngine
						.execute("total=0; i=10; do{i--; continue; total=total+i;  }while(i>0); return total;"));
		assertEquals(
				10,
				scriptEngine
						.execute("total=0; i=10; do{total=total+i; i--;break; }while(i>0); return total;"));
	}

	public void testWhile() throws Exception {
		// 测试while指令
		assertEquals(
				55,
				scriptEngine
						.execute("total=0; i=10; while(i>0){total=total+i; i--;} return total;"));
		// 测试循环，total=total+i不会被执行
		assertEquals(
				0,
				scriptEngine
						.execute("total=0; i=10; while(i>0){i--; continue; total=total+i; } return total;"));
		// 测试中断
		assertEquals(
				10,
				scriptEngine
						.execute("total=0; i=10; while(i>0){total=total+i; i--;break; } return total;"));
	}

	public void testFor() throws Exception {
		// 测试分号格式的for循环
		Object execute = scriptEngine
				.execute("for(i=1;i<5;i++){} return i;");
		assertNull(
				execute);
		assertEquals(
				5,
				scriptEngine
						.execute("i=1;for(;i<5;i++){} return i;"));

		assertEquals(
				"a,b,",
				scriptEngine
						.execute("total=\"\"; for(char:\"ab\"){total=total+char+\",\";} return total;"));
		assertEquals(
				1 * 2 * 3 * 4 * 5 * 6 * 7 * 8 * 9 * 10,
				scriptEngine
						.execute("total=1; for(i=1;i<=10;i++){total=total*i;} return total;"));
		assertEquals(
				1,
				scriptEngine
						.execute("total=1; for(i=1;i<=10;i++){continue; total=total*i;} return total;"));

		assertEquals(
				120,
				scriptEngine
						.execute("total=1; for(i=1;i<=10;i++){total=total*i; break(total>30);} return total;"));

		// 测试冒号格式的for循环
		assertEquals(
				24,
				scriptEngine
						.execute("list=new java.util.ArrayList(); list.add(1); list.add(2);list.add(3);list.add(4); total=1; for(i:list){ total=total*i;} return total;"));

	}
	
	public void testTempVariable() throws Exception {
		//测试for循环的临时变量
		assertEquals(
				true,
				scriptEngine
						.execute("for(i=1;i<=10;i++){System.out.println(i);} return i==null;")); 
		
		assertEquals(
				100,
				scriptEngine
						.execute("i=100; for(i=1;i<=10;i++){System.out.println(i);} return i;")); 
	}
	//测试for支持多表达式
	public void testFor2() throws Exception {
		 //for(int i=1,j=1;i<=5||j<=5;i++,j++){System.out.println(i+j);}
		scriptEngine
		.execute("for(i=3,j=4;i<=5||j<=5;i++,j++){System.out.println(i+j);}");
	}

	public void testSwitch() throws Exception {
		// 测试switch
		assertEquals(
				3,
				scriptEngine
						.execute("a=1; b=20; switch(a){case 1:{b=3;} case 2:{b=5;} default:{b=10;}} return b;"));
		assertEquals(
				5,
				scriptEngine
						.execute("a=2; b=20; switch(a){case 1:{b=3; break;} case 2:{b=5;} default:{b=10;}} return b;"));
		assertEquals(
				10,
				scriptEngine
						.execute("a=0; b=20; switch(a){case 1:{b=3;} case 2:{b=5;} default:{b=10;}} return b;"));
	}

}
