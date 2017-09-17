package org.tinygroup.tinyscript;

import junit.framework.TestCase;

import java.util.List;

import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.impl.DefaultTinyScriptEngine;

public class ExcelTest extends TestCase {

	public void testXls() throws Exception{
		ScriptEngine engine = new DefaultTinyScriptEngine();
		
		@SuppressWarnings("unchecked")
		List<DataSet> dataSet = (List<DataSet>) engine.execute("return readExcel(\"src/test/resources/hello.xls\");");
		assertNotNull(dataSet);
		assertEquals(3, dataSet.get(0).getColumns());
		assertEquals(5, dataSet.get(0).getRows());
		assertEquals(true, dataSet.get(0).next());
		assertEquals("0.0", (String)dataSet.get(0).getData("name"));
		assertEquals("111.0", dataSet.get(0).getData("age"));
		assertEquals(true,dataSet.get(0).absolute(6));
		assertEquals("", dataSet.get(0).getData("descrption"));
		
		dataSet.get(0).setIndexFromOne(false);
		assertEquals(true,dataSet.get(0).absolute(5));
		assertEquals("", dataSet.get(0).getData("descrption"));
	}
	
	public void testXlsx() throws Exception{
		ScriptEngine engine = new DefaultTinyScriptEngine();
		
		@SuppressWarnings("unchecked")
		List<DataSet> dataSet = (List<DataSet>) engine.execute("return readExcel(\"src/test/resources/hello.xlsx\");");
		assertNotNull(dataSet);
		assertEquals(3, dataSet.get(0).getColumns());
		assertEquals(5, dataSet.get(0).getRows());
		assertEquals(true, dataSet.get(0).next());
		assertEquals("0.0", (String)dataSet.get(0).getData("name"));
		assertEquals("111.0", dataSet.get(0).getData("age"));
		assertEquals(true,dataSet.get(0).absolute(6));
		assertEquals("", dataSet.get(0).getData("descrption"));
		
		dataSet.get(0).setIndexFromOne(false);
		assertEquals(true,dataSet.get(0).absolute(5));
		assertEquals("", dataSet.get(0).getData("descrption"));
	}
}
