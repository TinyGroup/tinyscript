package org.tinygroup.tinyscript.tree;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.tree.impl.DataNodeUtil;
import org.tinygroup.tinyscript.tree.impl.TreeDataNode;

/**
 * 测试TreeDataNode
 * @author yancheng11334
 *
 */
public class TreeDataNodeTest extends TestCase{

	/**
	 * 测试组装及转换
	 * @throws Exception
	 */
	public void testJson() throws Exception{
		TreeDataNode tree = new TreeDataNode();
		tree.put("msg", "ok").put("code", 0);
		
		TreeDataNode data = new TreeDataNode();
		tree.addNode("data", data);
		String[] companys = {"华为","三星","苹果"};
		for(int i=0;i<companys.length;i++){
			TreeDataNode node = new TreeDataNode();
			node.put("name", companys[i]).put("id", i+1);
			data.addNode("company", node);
		}
		assertEquals("ok", tree.get("msg"));
		assertEquals(0, tree.get("code"));
		assertEquals(1, tree.getChildren().size());
		assertEquals(3, data.getChildren().size());
		
		String jsonStr = DataNodeUtil.toJson(tree);
		System.out.println(jsonStr);
		tree = DataNodeUtil.readJson(jsonStr);
		System.out.println(DataNodeUtil.toJson(tree));
	}
	
	public void testEmpty()  throws Exception{
		TreeDataNode tree = new TreeDataNode();
		tree.put("msg", "ok").put("code", 0);
	    assertNull(tree.get("data"));
		tree.addNode("data", new TreeDataNode());
		assertNotNull(tree.get("data"));
		System.out.println(DataNodeUtil.toJson(tree));
	}
}
