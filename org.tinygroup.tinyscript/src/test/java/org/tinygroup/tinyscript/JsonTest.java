package org.tinygroup.tinyscript;

import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.impl.DefaultTinyScriptEngine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import junit.framework.TestCase;

/**
 * 测试Json相关
 * @author yancheng11334
 *
 */
public class JsonTest extends TestCase {

	public void testDataSet() throws Exception{
		ScriptEngine engine = new DefaultTinyScriptEngine();
		
		//验证json串
		String str = (String)engine.execute("ds = readTxt(\"src/test/resources/mailCharge.txt\"); return ds.toJson();");
		assertNotNull(str);
		
		JSONArray array = JSON.parseArray(str);
		assertEquals(5, array.size());
		
		//验证String转换ds
		ScriptContext  context = new DefaultScriptContext();
		context.put("json", str);
		DataSet ds = (DataSet) engine.execute("return json.jsonToDataSet();",context);
		assertEquals(5, ds.getRows());
		assertEquals("COST",ds.getData(1, 1));
		
		//验证JSONArray转换ds
		context.put("array", array);
		ds = (DataSet) engine.execute("return array.jsonToDataSet();",context);
		assertEquals(5, ds.getRows());
		assertEquals("COST",ds.getData(1, 1));
	}
}
