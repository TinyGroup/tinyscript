package org.tinygroup.tinyscript;

import java.util.List;

import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.impl.DefaultTinyScriptEngine;

import junit.framework.TestCase;

/**
 * 排列组合等数学相关测试用例
 * @author yancheng11334
 *
 */
public class MathTest extends TestCase {

	public void testCombination() throws Exception{
		ScriptEngine  engine = new DefaultTinyScriptEngine();
		
		//列出全部数据
		List<Object[]> result = (List<Object[]>) engine.execute("elements = [1,2,3,4]; list = new java.util.ArrayList(); elements.combine((record) -> {list.add(record.toArray());}); return list;");
		assertNotNull(result);
		assertEquals(15, result.size());
		printResult(result);
		
		//输出长度为3的组合
		System.out.println("length==3 start!");
		engine.execute("elements = [1,2,3,4]; elements.combine((record) -> { if(record.size()==3){System.out.println(record);} });");
		System.out.println("length==3 end!");
	}
	
	public void testPermutation() throws Exception{
		ScriptEngine  engine = new DefaultTinyScriptEngine();
		
		//列出全部数据
		List<Object[]> result = (List<Object[]>) engine.execute("elements = [\"R\",\"G\",\"B\"]; list = new java.util.ArrayList();  elements.permute(4,(record)->{list.add(record.toArray());}); return list;");
		assertNotNull(result);
		assertEquals(81, result.size());
		printResult(result);
		
		//列出开头是G,结尾是B的排列
		System.out.println("GB start!");
		engine.execute("elements = [\"R\",\"G\",\"B\"]; elements.permute(4,(record) -> { if(record[1]==\"G\" && record[4]==\"B\"){System.out.println(record);} });");
		System.out.println("GB end!");
	}
	
	public void testAllPermutation() throws Exception{
		ScriptEngine  engine = new DefaultTinyScriptEngine();
		
		//列出全部记录
		List<Object[]> result = (List<Object[]>) engine.execute("elements = [\"R\",\"G\",\"B\"]; list = new java.util.ArrayList();  elements.permuteAll((record)->{list.add(record.toArray());}); return list;");
		assertNotNull(result);
		assertEquals(6, result.size());
		printResult(result);
		
		//列出开头是G,结尾是B的排列
		System.out.println("GB start!");
		engine.execute("elements = [\"R\",\"G\",\"B\"]; elements.permuteAll((record) -> { if(record[1]==\"G\" && record[3]==\"B\"){System.out.println(record);} });");
		System.out.println("GB end!");
	}
	
	private void printResult(List<Object[]> result){
		for(int i=0;i<result.size();i++){
			Object[] objs = result.get(i);
			for(int j=0;j<objs.length;j++){
			   System.out.print(" "+objs[j]);
			}
			System.out.println();
		}
	}
}
