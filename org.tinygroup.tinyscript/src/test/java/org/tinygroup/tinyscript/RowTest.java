package org.tinygroup.tinyscript;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.ComputeEngine;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.impl.DefaultComputeEngine;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;

public class RowTest extends TestCase {


	public void testRow()  throws Exception {
		ComputeEngine engine = new DefaultComputeEngine();
		ScriptContext context = new DefaultScriptContext();
		
        DataSet orderDs  = (DataSet) engine.execute("return readTxt(\"src/test/resources/testOrder.txt\");");
	
		context.put("orderDs", orderDs);
		orderDs.absolute(3);
		assertEquals("3", engine.execute("return orderDs.cursorRow().getData(\"ID\");", context));
		orderDs.absolute(3);
		assertEquals("2", engine.execute("return orderDs.cursorRow(-1).getData(\"ID\");", context));
		orderDs.absolute(3);
		assertEquals("4", engine.execute("return orderDs.cursorRow(1).getData(\"ID\");", context));
		orderDs.absolute(3);
		assertEquals("4", engine.execute("return orderDs.nextRow().getData(\"ID\");", context));
		orderDs.absolute(3);
		assertEquals("4", engine.execute("return orderDs.nextRow(1).getData(\"ID\");", context));
		orderDs.absolute(3);
		assertEquals("2", engine.execute("return orderDs.previewRow().getData(\"ID\");", context));
		orderDs.absolute(3);
		assertEquals("2", engine.execute("return orderDs.previewRow(1).getData(\"ID\");", context));
		
		assertEquals("1", engine.execute("return orderDs.firstRow().getData(\"ID\");", context));
		assertEquals("2", engine.execute("return orderDs.firstRow(1).getData(\"ID\");", context));
		assertEquals("5", engine.execute("return orderDs.lastRow().getData(\"ID\");", context));
		assertEquals("4", engine.execute("return orderDs.lastRow(-1).getData(\"ID\");", context));
	}

}
