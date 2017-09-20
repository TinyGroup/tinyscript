package org.tinygroup.tinyscript.tree;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.tinyscript.tree.json.FastJsonNode;

/**
 * 测试json树
 * @author yancheng11334
 *
 */
public class JsonTest extends TestCase{

	public void testFastJson() throws Exception{
		String jsonStr = FileUtil.readFileContent(new File("src/test/resources/json1.txt"), "utf-8");
		assertNotNull(jsonStr);
	
		List<DataNode> children = null;
		
		//测试root节点
		DataNode dataNode = new FastJsonNode(jsonStr);
		assertEquals(false, dataNode.isLeaf());
		assertEquals(true, dataNode.isRoot());
		assertEquals(null, dataNode.getName());
		assertEquals(null, dataNode.getValue());
		
		children = dataNode.getChildren();
		assertEquals(14, children.size());
		
		//测试JsonObject节点
	    DataNode objNode = dataNode.findNode("albumArray");
	    assertEquals(false, objNode.isLeaf());
		assertEquals(false, objNode.isRoot());
		assertEquals("albumArray", objNode.getName());
		assertEquals(null, objNode.getValue());
		
		children = objNode.getChildren();
		assertEquals(1, children.size());
		
		DataNode result = objNode.getChild("007");
		assertNull(result);
				
		objNode.addNode("007", "123456789");
		result = objNode.getChild("007");
		assertNotNull(result);
		
		children = objNode.getChildren();
		assertEquals(2, children.size());
		
		objNode.removeNode("007");
		children = objNode.getChildren();
		assertEquals(1, children.size());
		
		//测试JsonArray节点
		DataNode arrayNode = dataNode.findNode("albumIdList");
		assertEquals(false, arrayNode.isLeaf());
		assertEquals(false, arrayNode.isRoot());
		assertEquals("albumIdList", arrayNode.getName());
		assertEquals(null, arrayNode.getValue());
		
		children = arrayNode.getChildren();
		assertEquals(2, children.size());
		
		result = arrayNode.getChild(1);
		assertEquals("{\"totalidnum\":2001,\"idlist\":[\"319281602\"]}",result.toString());
		result = arrayNode.getChild("albumIdList0");
		assertEquals("{\"totalidnum\":2000,\"idlist\":[\"319281600\"]}",result.toString());
		
		
		//测试一般节点
		DataNode leafNode = dataNode.findNode("year");
		assertEquals(true, leafNode.isLeaf());
		assertEquals(false, leafNode.isRoot());
		assertEquals("year", leafNode.getName());
		assertEquals("2007", leafNode.getValue());
	}
}
