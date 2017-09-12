package org.tinygroup.tinyscript;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.impl.DefaultTinyScriptEngine;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;

/**
 * 测试重命令列
 * @author yancheng11334
 *
 */
public class RenameTest extends TestCase {

	
    public void testRename() throws Exception {
    	ScriptEngine engine = new DefaultTinyScriptEngine();
		ScriptContext context = new DefaultScriptContext();
		
		DataSet orderDs1  = (DataSet) engine.execute("return readTxt(\"src/test/resources/testOrder.txt\");");
		context.put("orderDs1", orderDs1);
		DataSet orderDs2  = (DataSet) engine.execute("return orderDs1.copy();",context);
		context.put("orderDs2", orderDs2);
		
		assertEquals("ID", engine.execute("return orderDs1.field(1);",context)); 
		assertEquals("order_id", engine.execute("orderDs1.rename(0,\"order_id\"); return orderDs1.field(1);",context));   //按下标修改
		assertEquals("order_id2", engine.execute("orderDs1.rename(\"order_id\",\"order_id2\"); return orderDs1.field(1);",context));   //按名称修改
		
		assertEquals("ID", engine.execute("return orderDs2.field(1);",context)); //修改字段不影响克隆体
		engine.setIndexFromOne(false);
		assertEquals("ID", engine.execute("return orderDs2.field(0);",context));
    }
}
