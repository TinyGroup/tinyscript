package org.tinygroup.tinyscript;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.ComputeEngine;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.impl.DefaultComputeEngine;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;

public class ReplaceTest extends TestCase {
	
    public void testReplace() throws Exception {
    	ComputeEngine engine = new DefaultComputeEngine();
		ScriptContext context = new DefaultScriptContext();
		
    	DataSet orderDs1  = (DataSet) engine.execute("return readTxt(\"src/test/resources/Orders.txt\").insertColumn(2,\"item_qty\");");
		context.put("orderDs1", orderDs1);
		
		DataSet orderDs2  = (DataSet) engine.execute("return readTxt(\"src/test/resources/OrderDetails.txt\");");
		context.put("orderDs2", orderDs2);
		
		orderDs1 = (DataSet) engine.execute("return orderDs1.replace(orderDs2,\"item_qty\");", context); //测试替换相同字段
		assertEquals(true, orderDs1.getData(11, 2)== orderDs2.getData(11, 3));
		orderDs1 = (DataSet) engine.execute("left=\"item_qty\"; right=\"item_id\"; return orderDs1.replace(orderDs2,left,right);", context); //测试替换部同字段
		assertEquals(true, orderDs1.getData(11, 2)== orderDs2.getData(11, 2));
    }

}
