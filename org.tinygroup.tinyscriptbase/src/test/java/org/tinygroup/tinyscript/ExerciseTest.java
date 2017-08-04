package org.tinygroup.tinyscript;

import java.util.List;

import junit.framework.TestCase;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.tinyrunner.Runner;
import org.tinygroup.tinyscript.ScriptClassInstance;
import org.tinygroup.tinyscript.ScriptEngine;

/**
 * 具体的习题例子
 * @author yancheng11334
 *
 */
public class ExerciseTest extends TestCase{

    private ScriptEngine scriptEngine;
	
	protected void setUp() throws Exception {
		Runner.init("application.xml", null);
		scriptEngine = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader()).getBean("scriptEngine");
		scriptEngine.start();
    }
	
	public void testNumberList() throws Exception {
		scriptEngine.execute("obj = new abc.def.NumberList(); obj.print(100);");
	}
	
	public void testImport() throws Exception {
		//测试java的import
		Object obj = scriptEngine.execute("obj = new import1(); return obj.getFormat();");
		assertEquals(java.text.SimpleDateFormat.class,obj.getClass());
		
		obj = scriptEngine.execute("obj = new import1(); return obj.getList();");
		assertEquals(java.util.ArrayList.class,obj.getClass());
		
		//测试tiny脚本的import
		ScriptClassInstance classInstance = (ScriptClassInstance)scriptEngine.execute("obj = new import2(); return obj.getNewInteger();");
		assertEquals("NewInteger",classInstance.getScriptClass().getClassName());
	}
	
	public void testGetPrimeNumber() throws Exception {
		//筛选法计算质数
		List<Integer> list = (List<Integer>) scriptEngine.execute("nsc = new NoStructureCalculation(); return nsc.getPrimeNumber(20);");
		assertEquals(8, list.size()); 
		assertEquals(2, list.get(0).intValue());
		assertEquals(3, list.get(1).intValue());
		assertEquals(5, list.get(2).intValue());
		assertEquals(7, list.get(3).intValue());
		assertEquals(11, list.get(4).intValue());
		assertEquals(13, list.get(5).intValue());
		assertEquals(17, list.get(6).intValue());
		assertEquals(19, list.get(7).intValue());
	}
	
	public void testGetFibonacci() throws Exception {
		//计算菲波那契数列，验证递归与内部方法调用
		List<Integer> list = (List<Integer>)  scriptEngine.execute("nsc = new NoStructureCalculation(); return nsc.getFibonacci(20);");
		for(int i=2;i<list.size();i++){
		  assertEquals(list.get(i).intValue(), list.get(i-2).intValue()+list.get(i-1).intValue()); 
		}
	}
	
	public void testContinueInfo() throws Exception {
		//计算除法的最小循环节
		assertEquals("", scriptEngine.execute("nsc = new NoStructureCalculation(); return nsc.getContinueInfo(1,5);")); //无循环节
		assertEquals("3", scriptEngine.execute("nsc = new NoStructureCalculation(); return nsc.getContinueInfo(1,3);")); 
		assertEquals("7647058823529411", scriptEngine.execute("nsc = new NoStructureCalculation(); return nsc.getContinueInfo(13,17);")); 
	}
	
	public void testCheckBarcode() throws Exception {
		//计算机器码真伪
		assertEquals(false, scriptEngine.execute("nsc = new NoStructureCalculation(); return nsc.checkBarcode(null);"));  //null值
		assertEquals(false, scriptEngine.execute("nsc = new NoStructureCalculation(); return nsc.checkBarcode(\"123\");"));  //机器码不足12位
		assertEquals(false, scriptEngine.execute("nsc = new NoStructureCalculation(); return nsc.checkBarcode(\"1234567890123\");")); //伪造的机器码
		assertEquals(true, scriptEngine.execute("nsc = new NoStructureCalculation(); return nsc.checkBarcode(\"1122447766220\");")); //真实的机器码
	}
}
