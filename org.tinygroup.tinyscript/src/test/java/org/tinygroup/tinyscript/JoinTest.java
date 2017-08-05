package org.tinygroup.tinyscript;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.ComputeEngine;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.impl.DefaultComputeEngine;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;

public class JoinTest  extends TestCase {

	
	public void testJoin() throws Exception {
		ComputeEngine engine = new DefaultComputeEngine();
		DataSet orders = (DataSet) engine.execute("return readTxt(\"src/test/resources/Orders.txt\");");
		DataSet details = (DataSet) engine.execute("return readTxt(\"src/test/resources/OrderDetails.txt\");");
		
		ScriptContext context = new DefaultScriptContext();
		context.put("orders", orders);
		context.put("details", details);
		
		DataSet joinResult = (DataSet) engine.execute("return orders.join(details,\"order_id = order_id\").copy().deleteColumn(\"order_id\");",context);
		
		//验证字段(自定义展示字段)
		assertEquals("customer_id", joinResult.getFields().get(0).getName());
		assertEquals("order_id", joinResult.getFields().get(1).getName());
		assertEquals("item_id", joinResult.getFields().get(2).getName());
		assertEquals("item_qty", joinResult.getFields().get(3).getName());
		
		//验证结果
		assertEquals("5", joinResult.getData(11, 1));  //下标10的记录
		assertEquals("20010", joinResult.getData(11, 2));  //下标10的记录
		assertEquals("1005", joinResult.getData(11, 3));  //下标10的记录
		assertEquals("10", joinResult.getData(11, 4));  //下标10的记录
		
		assertEquals("5", joinResult.getData(1001, 1));  //下标1000的记录
		assertEquals("21000", joinResult.getData(1001, 2));  //下标1000的记录
		assertEquals("1021", joinResult.getData(1001, 3));  //下标1000的记录
		assertEquals("7", joinResult.getData(1001, 4));  //下标1000的记录
		
		//验证字段(默认展示全部字段)
		joinResult = (DataSet) engine.execute("return orders.join(details,\"order_id\"+\"=\"+\"order_id\");",context);
		
		assertEquals("customer_id", joinResult.getFields().get(0).getName());
		assertEquals("order_id", joinResult.getFields().get(1).getName());
		assertEquals("order_id", joinResult.getFields().get(2).getName());
		assertEquals("item_id", joinResult.getFields().get(3).getName());
		assertEquals("item_qty", joinResult.getFields().get(4).getName());
		
		
		//验证字段
		joinResult = (DataSet) engine.execute("return orders.join(details, order_id = order_id);",context);
		assertEquals(1001, joinResult.getRows());
		
	    //结合filter函数，实现条件组合
	    joinResult = (DataSet) engine.execute("return orders.join(details, order_id = order_id ).filter(int(order_id) > 20005 && int(order_id) < 20015);",context);
	    assertEquals(9, joinResult.getRows());
	}
}
