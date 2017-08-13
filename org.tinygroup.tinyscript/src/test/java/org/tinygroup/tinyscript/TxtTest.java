package org.tinygroup.tinyscript;

import org.tinygroup.tinyscript.ComputeEngine;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.impl.DefaultComputeEngine;

import junit.framework.TestCase;

/**
 * 增加对tinyscript文本的读取函数
 * @author yancheng11334
 *
 */
public class TxtTest extends TestCase {

	public void testTxt() throws Exception{
		ComputeEngine engine = new DefaultComputeEngine();
		DataSet dataSet = (DataSet) engine.execute("return readTxt(\"src/test/resources/StockRecords.txt\");");
		assertNotNull(dataSet);
		
		assertEquals(3, dataSet.getColumns());
		assertEquals("CODE", dataSet.getFields().get(0).getName());
		assertEquals("DT", dataSet.getFields().get(1).getName());
		assertEquals("CL", dataSet.getFields().get(2).getName());
		
		//判断记录数
		assertEquals(16170, dataSet.getRows());
	}
}
