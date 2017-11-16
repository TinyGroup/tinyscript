package org.tinygroup.tinyscript;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.GroupDataSet;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.impl.DefaultTinyScriptEngine;

public class SubTest extends TestCase {

	public void testSub() throws Exception {
		ScriptEngine engine = new DefaultTinyScriptEngine();
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
		
		orderDs1.setIndexFromOne(false);
		orderDs3  = (DataSet) engine.execute("return orderDs1.sub(1);",context);
		context.put("orderDs3", orderDs3);
		assertEquals(4, orderDs3.getColumns());
		assertEquals(4, orderDs3.getRows());
	}
	
	public void testSubGroup() throws Exception {
		ScriptEngine engine = new DefaultTinyScriptEngine();
		ScriptContext context = new DefaultScriptContext();
		
		DataSet ds = (DataSet) engine.execute("return readTxt(\"src/test/resources/mailCharge.txt\"); ");
		context.put("ds", ds);
		
		GroupDataSet ds0 = (GroupDataSet) engine.execute("gs = ds.group(\"FIELD\");  return gs;",context);
		assertEquals(5, ds0.getColumns());
		assertEquals(2, ds0.getRows());
		assertEquals(2, ds0.getGroups().size());
		assertEquals(1, ds0.getGroups().get(0).getRows());
		assertEquals(4, ds0.getGroups().get(1).getRows());
		
		GroupDataSet ds1 = (GroupDataSet) engine.execute("gs = ds.group(\"FIELD\");  return gs.sub(1,2);",context);
		assertEquals(5, ds1.getColumns());
		assertEquals(2, ds1.getRows());
		assertEquals(2, ds1.getGroups().size());
		
		GroupDataSet ds2 = (GroupDataSet) engine.execute("gs = ds.group(\"FIELD\");  return gs.subGroup(1,1);",context);
		assertEquals(5, ds2.getColumns());
		assertEquals(2, ds2.getRows());
		assertEquals(2, ds2.getGroups().size());
		assertEquals(1, ds2.getGroups().get(0).getRows());
		assertEquals(1, ds2.getGroups().get(1).getRows());
	}

}
