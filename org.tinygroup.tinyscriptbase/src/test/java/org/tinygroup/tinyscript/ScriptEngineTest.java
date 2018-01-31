package org.tinygroup.tinyscript;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.tinyscript.assignvalue.AssignValueProcessor;
import org.tinygroup.tinyscript.assignvalue.AssignValueUtil;
import org.tinygroup.tinyscript.config.FunctionConfig;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.impl.DefaultScriptEngine;
import org.tinygroup.tinyscript.interpret.AttributeUtil;
import org.tinygroup.tinyscript.interpret.ScriptUtil;
import org.tinygroup.tinyscript.objectitem.ObjectItemUtil;
import org.tinygroup.tinyscript.objectitem.ObjectSingleItemProcessor;

public class ScriptEngineTest extends TestCase{

    private ScriptEngine scriptEngine;
	
	protected void setUp() throws Exception {
		scriptEngine = new DefaultScriptEngine();
		scriptEngine.start();
    }
	
	
	public void testNewObject() throws Exception{
		assertEquals(123, scriptEngine.execute("a=new java.lang.Integer(\"123\");  return a ;")); //有参构造函数
		
		//TODO 如果构造函数包含简单类型，如int，boolean目前还不支持
		//测试java的class
		assertEquals(new Student(), scriptEngine.execute("s=new org.tinygroup.tinyscript.Student();  return s ;")); //无参构造函数
		assertEquals(new Student("yc",20,true), scriptEngine.execute("s=new org.tinygroup.tinyscript.Student(\"yc\",20,true);  return s ;")); //有参构造函数
		
	}
	
	public void testIdentifier() throws Exception{
		ScriptContext context = new DefaultScriptContext();
		context.put("s", "1234567# ");
		
		//测试对象方法
		assertEquals(8,scriptEngine.execute("return s.trim().length() ;", context));
		
		Student student = new Student();
		student.setAge(10);
		student.setName("Jam");
		context.put("student", student);
		
		//测试对象属性
		assertEquals("Jam",scriptEngine.execute("return student.name ;", context));
		assertEquals(10,scriptEngine.execute("return student.age ;", context));
		assertEquals(false,scriptEngine.execute("return student.registered ;", context));
		
		
		
		//测试tiny脚本的class
		String text = FileUtil.readFileContent(new File("src/test/resources/class.tsf"), "utf-8");
		ScriptSegment segment = ScriptUtil.getDefault().createScriptSegment(scriptEngine, null, text);
		
		scriptEngine.addScriptSegment(segment);
		
		assertEquals(11,scriptEngine.execute("math=new abc.def.NewMath(); return math.add(9,2);", context));
		
		scriptEngine.removeScriptSegment(segment);
		
		
	}
	
	
	

	
	public void testLoadScript() throws Exception{
		String text = FileUtil.readFileContent(new File("src/test/resources/class.tsf"), "utf-8");
		ScriptSegment segment = ScriptUtil.getDefault().createScriptSegment(scriptEngine, null, text);
		assertEquals("abc.def", segment.getPackage());
		
		//判断主键
		assertEquals("abc.def.NewMath", segment.getSegmentId());
		
		List<String> importList = segment.getImportList();
		assertEquals("org.tinygroup.commons.Math", importList.get(0));
		assertEquals("org.tinygroup.ext.*", importList.get(1));
		
		ScriptClass scriptClass = segment.getScriptClass();
		assertEquals("NewMath",scriptClass.getClassName());
		
		ScriptClassField scriptField = scriptClass.getScriptField("num");
		assertEquals("num",scriptField.getFieldName());
		assertEquals(99,scriptField.getValue());
		
		ScriptClassMethod scriptMethod = scriptClass.getScriptMethod("nomethod");
		assertNull(scriptMethod);
		
		scriptMethod = scriptClass.getScriptMethod("add2");
		assertNotNull(scriptMethod);
		assertEquals("add2",scriptMethod.getMethodName());	
		
	}
	
	//测试脚本类和代码段同时存在
	public void testClassAndSegment()  throws Exception{
		assertEquals(5,scriptEngine.execute("class ABC { num=5;  getNum(){return this.num;} }  abc = new ABC(); return abc.getNum();"));
	}
	
	//验证赋值扩展
	public void testAssignValue()  throws Exception{
		//片段,简单赋值
		assertEquals(103,scriptEngine.execute("a=0;b=3;c=100; d= b*(a+1)+c; return d; "));
		
		//片段,java对象
		assertEquals(10000L,scriptEngine.execute("d = new java.util.Date(10000L); return d.getTime();"));
		
		//片段和类混用,类属性
		assertEquals(6,scriptEngine.execute("class ABC { num=5;  getNum(){ num =6; return this.num;} }  abc = new ABC(); return abc.getNum();"));
		
		//片段和类混用,形参优先级高于类属性
		assertEquals(80,scriptEngine.execute("class ABC { num=5;  getNum(num){return num;} }  abc = new ABC(); return abc.getNum(80);"));
		
		//片段和类混用,通过this引用类属性
	    assertEquals(5,scriptEngine.execute("class ABC { num=5;  getNum(num){return this.num;} }  abc = new ABC(); return abc.getNum(80);"));
	    
	    //模拟赋值扩展处理器
	    ThreeAssignValueProcessor  processor = new ThreeAssignValueProcessor();
	    
	    //未扩展之前
	    assertEquals("abc",scriptEngine.execute("abc = \"abc\"; return abc;"));
	    
	    //执行扩展
	    AssignValueUtil.addAssignValueProcessor(processor);
	    assertEquals("123",scriptEngine.execute("abc = \"abc\"; return abc;"));
	    
	    //删除扩展
	    AssignValueUtil.removeAssignValueProcessor(processor);
	    assertEquals("abc",scriptEngine.execute("abc = \"abc\"; return abc;"));
	}
	
	//测试对象下标的扩展
	public void testObjectItem() throws Exception {
		 //string自动转char[]
		 assertEquals('a',scriptEngine.execute("array = \"abc\"; return array[0];"));
		 
		 ScriptContext context = new DefaultScriptContext();
		 
		 //测试简单类型数组
		 context.put("is", new int[1]);
		 assertEquals(0,scriptEngine.execute("return is[0];",context));
		 
		 context.put("bs", new boolean[2]);
		 assertEquals(false,scriptEngine.execute("return bs[1];",context));
		 
		 //测试List按数组展示
		 assertEquals("中国",scriptEngine.execute("ss = [\"俄罗斯\",\"中国\",\"美国\"] ;  return ss[1];"));
		 
		 //测试扩展
		 ObjectItemUtil.addObjectItemProcessor(new StudentItemProcessor());
		 
		 //假设有需求需要通过下标方式实现访问对象属性
		 assertEquals("John",scriptEngine.execute("student = new org.tinygroup.tinyscript.Student(\"John\",20,true); return student[\"name\"];"));
		 assertEquals(20,scriptEngine.execute("student = new org.tinygroup.tinyscript.Student(\"John\",20,true); return student[\"age\"];"));
		 assertEquals(true,scriptEngine.execute("student = new org.tinygroup.tinyscript.Student(\"John\",20,true); return student[\"registered\"];"));
		 
	}
	
	//测试方法配置项
	public void testFuncitonConfig() throws Exception {
		//无绑定的函数
		List<FunctionConfig> configs = scriptEngine.getFunctionConfigs(null);
		assertEquals(true,configs.size()>=41);
		
		//绑定对象的函数
		configs = scriptEngine.getFunctionConfigs(new Date());
		assertEquals(true,configs.size()>=2);
	}
	
	/**
	 * 测试执行脚本类方法
	 * @throws Exception
	 */
	public void testExecute() throws Exception {
		String script = "class MathClass{ sum(a,b,c){ return a+b+c; } }";
		ScriptSegment segment = ScriptUtil.getDefault().createScriptSegment(scriptEngine, null, script);
		scriptEngine.addScriptSegment(segment);
		
		//执行有序参数运算
		assertEquals(7, scriptEngine.execute("MathClass", "sum",1,2,4));
		
		//执行map参数运算
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("a", 1);
		map.put("b", 2);
		map.put("c", 8);
		assertEquals(11, scriptEngine.execute(map,"MathClass", "sum"));
		
	}
	
	class ThreeAssignValueProcessor implements AssignValueProcessor{
        //扩展的匹配条件
		public boolean isMatch(String name, ScriptContext context) {
			return name.equals("abc");
		}

		//执行扩展的业务逻辑
		public void process(String name, Object value, ScriptContext context)
				throws Exception {
			context.put(name, "123");
		}
	}
	
	class StudentItemProcessor extends ObjectSingleItemProcessor{
      
		//扩展的匹配条件
		public boolean isMatch(Object obj, Object item) {
			return obj instanceof Student;
		}

		//扩展的业务逻辑
		public Object process(ScriptContext context, Object obj,
				Object item) throws Exception {
			return AttributeUtil.getAttribute(obj, item.toString());
		}

		public void assignValue(ScriptContext context, Object value,
				Object obj, Object item) throws Exception {
			
		}
		
	}
}
