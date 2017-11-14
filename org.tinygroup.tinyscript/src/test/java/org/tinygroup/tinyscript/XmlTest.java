package org.tinygroup.tinyscript;

import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.impl.DefaultTinyScriptEngine;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

import junit.framework.TestCase;

/**
 * 测试xml相关
 * @author yancheng11334
 *
 */
public class XmlTest extends TestCase {

	public void testDataSet() throws Exception{
		ScriptEngine engine = new DefaultTinyScriptEngine();
		
		//验证xml串
		String str = (String)engine.execute("ds = readTxt(\"src/test/resources/mailCharge.txt\"); return ds.toXml();");
		assertNotNull(str);
		
		XmlNode root = new XmlStringParser().parse(str).getRoot();
		assertEquals("dataSet", root.getNodeName());
		assertEquals(5, root.getSubNodes().size());
		
		//验证String转换ds
		ScriptContext  context = new DefaultScriptContext();
	    context.put("xml", str);
	    DataSet ds = (DataSet) engine.execute("return xml.xmlToDataSet();",context);
		assertEquals(5, ds.getRows());
		ds.absolute(1);
		assertEquals("COST",ds.getData("FIELD"));
		
		//验证XmlNode转换ds
		context.put("root", root);
		ds = (DataSet) engine.execute("return root.xmlToDataSet();",context);
		assertEquals(5, ds.getRows());
		ds.absolute(1);
		assertEquals("COST",ds.getData("FIELD"));
	}
}
