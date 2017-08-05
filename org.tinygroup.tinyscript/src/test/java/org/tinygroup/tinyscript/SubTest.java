package org.tinygroup.tinyscript;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.ComputeEngine;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.impl.DefaultComputeEngine;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;

public class SubTest extends TestCase {

	public void testSub() throws Exception {
		ComputeEngine engine = new DefaultComputeEngine();
		ScriptContext context = new DefaultScriptContext();
		
		DataSet orderDs1  = (DataSet) engine.execute("return readTxt(\"src/test/resources/testOrder.txt\");");
		context.put("orderDs1", orderDs1);
		assertEquals(4, orderDs1.getColumns());
		assertEquals(5, orderDs1.getRows());
		
		DataSet orderDs2  = (DataSet) engine.execute("return orderDs1.sub(1,3);",context);
		context.put("orderDs2", orderDs2);
		assertEquals(4, orderDs2.getColumns());
		assertEquals(3, orderDs2.getRows());
		
		DataSet orderDs3  = (DataSet) engine.execute("return orderDs1.sub(1);",context);
		context.put("orderDs3", orderDs3);
		assertEquals(4, orderDs3.getColumns());
		assertEquals(5, orderDs3.getRows());
		
		engine.setIndexFromOne(false);
		orderDs3  = (DataSet) engine.execute("return orderDs1.sub(1);",context);
		context.put("orderDs3", orderDs3);
		assertEquals(4, orderDs3.getColumns());
		assertEquals(4, orderDs3.getRows());
	}

}
