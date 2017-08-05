package org.tinygroup.tinyscript;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.ComputeEngine;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.impl.DefaultComputeEngine;

public class ExcelTest extends TestCase {

	public void testXls() throws Exception{
		ComputeEngine engine = new DefaultComputeEngine();
		
		DataSet dataSet = (DataSet) engine.execute("return readExcel(\"src/test/resources/hello.xls\");");
		assertNotNull(dataSet);
		assertEquals(3, dataSet.getColumns());
		assertEquals(5, dataSet.getRows());
		assertEquals(true, dataSet.next());
		assertEquals("0.0", (String)dataSet.getData("name"));
		assertEquals("111.0", dataSet.getData("age"));
		assertEquals(true,dataSet.absolute(6));
		assertEquals("", dataSet.getData("descrption"));
		
		dataSet.setIndexFromOne(false);
		assertEquals(true,dataSet.absolute(5));
		assertEquals("", dataSet.getData("descrption"));
	}
	
	public void testXlsx() throws Exception{
		ComputeEngine engine = new DefaultComputeEngine();
		
		DataSet dataSet = (DataSet) engine.execute("return readExcel(\"src/test/resources/hello.xlsx\");");
		assertNotNull(dataSet);
		assertEquals(3, dataSet.getColumns());
		assertEquals(5, dataSet.getRows());
		assertEquals(true, dataSet.next());
		assertEquals("0.0", (String)dataSet.getData("name"));
		assertEquals("111.0", dataSet.getData("age"));
		assertEquals(true,dataSet.absolute(6));
		assertEquals("", dataSet.getData("descrption"));
		
		dataSet.setIndexFromOne(false);
		assertEquals(true,dataSet.absolute(5));
		assertEquals("", dataSet.getData("descrption"));
	}
}
