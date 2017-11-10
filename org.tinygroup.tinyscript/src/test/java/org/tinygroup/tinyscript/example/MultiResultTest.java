package org.tinygroup.tinyscript.example;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptClassInstance;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.impl.DefaultTinyScriptEngine;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.ScriptUtil;

import junit.framework.TestCase;

/**
 * 测试返回多个结果
 * @author yancheng11334
 *
 */
public class MultiResultTest extends TestCase{

	private ScriptEngine engine ;
	private ScriptContext context ;
	
	protected void setUp() throws Exception {
		engine = new DefaultTinyScriptEngine();
		context = new DefaultScriptContext();
		
		String content = FileUtil.readFileContent(new File("src/test/resources/multiresult.tsf"), "utf-8");
		ScriptSegment scriptSegment = ScriptUtil.getDefault().createScriptSegment(engine, null, content);
		engine.addScriptSegment(scriptSegment);
	}
	
	/**
	 * 脚本方法不支持一次返回多个结果，但是可以通过赋值多个结果到脚本类实例的属性，再获取脚本类实例的属性
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void testMultiResult() throws Exception {
		//返回脚本类实例
		ScriptClassInstance obj = (ScriptClassInstance) engine.execute("m = new Example7(); m.createMultiResult(); return m;", context);
		assertNotNull(obj);
		
		//获取类实例的属性
		List result1 = (List) obj.getField("result1");
		List result2 = (List) obj.getField("result2");
		Map result3 = (Map) obj.getField("result3");
		assertNotNull(result1);
		assertNotNull(result2);
		assertNotNull(result3);
		
		//判断结果
		assertEquals(5, result1.size());
		assertEquals(true, result1.contains(1));
		
		assertEquals(3, result2.size());
		assertEquals(true, result2.contains("R"));
		
		assertEquals(3, result3.size());
		assertEquals(true, result3.containsKey("key1"));
	}
}
