package org.tinygroup.tinyscript.tree;

import java.io.File;

import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.tinyscript.tree.xml.DocumentXmlNode;

import junit.framework.TestCase;

public class XmlTest extends TestCase{

	/**
	 * 测试xml树
	 * @throws Exception
	 */
	public void testDocument() throws Exception{
		String xml = FileUtil.readFileContent(new File("src/test/resources/CS8_seal.xml"), "utf-8");
		assertNotNull(xml);
		
		DataNode dataNode = new DocumentXmlNode(xml);
		assertEquals(false, dataNode.isLeaf());
		assertEquals(true, dataNode.isRoot());
		assertEquals("Overlay", dataNode.getName());
		String value = (String) dataNode.getValue();
		assertTrue(value.startsWith("<Overlay version=\"1.0.1\">") && value.endsWith("</Overlay>"));
		
		//测试删除
		DataNode n1 = dataNode.findNode("svg");
		assertNotNull(n1);
		n1.getParent().removeNode(n1); //不能直接通过dataNode删除
		n1 = dataNode.findNode("svg");
		assertNull(n1);
		
		//测试添加
		DataNode n2 = dataNode.findNode("xxxxx");
		assertNull(n2);
		dataNode.addNode("xxxxx", "1234567");
		n2 = dataNode.findNode("xxxxx");
		assertNotNull(n2);
		
		assertEquals("xxxxx", n2.getName());
		assertEquals("1234567",n2.getValue());
		n2.setValue("@w3c");
		assertEquals("@w3c",n2.getValue());
	}
}
