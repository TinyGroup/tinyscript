package org.tinygroup.tinyscript;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.ComputeEngine;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.impl.DefaultComputeEngine;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;

public class UpdateTest extends TestCase {

	public void  testMatch() throws Exception {
		
		ComputeEngine engine = new DefaultComputeEngine();
		ScriptContext context = new DefaultScriptContext();
		
		DataSet orderDs  = (DataSet) engine.execute("a = \"WEIGHT\"; return readTxt(\"src/test/resources/testOrder.txt\").insertColumn(1,\"price\").convert(a,\"int\");");
		context.put("orderDs", orderDs);
		
		engine.execute("return orderDs.update(\"price\",\"WEIGHT*150\");",context);
		
		assertEquals(900, orderDs.getData(1, 1));
		assertEquals(450, orderDs.getData(2, 1));
		assertEquals(150, orderDs.getData(3, 1));
	}

}
