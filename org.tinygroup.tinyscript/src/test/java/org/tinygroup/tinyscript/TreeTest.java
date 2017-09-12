package org.tinygroup.tinyscript;

import java.util.Map;

import junit.framework.TestCase;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.tinyrunner.Runner;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.impl.DefaultTinyScriptEngine;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.tree.DataNode;

/**
 * 测试树型结构
 * @author yancheng11334
 *
 */
public class TreeTest extends TestCase {

    private ScriptContext baseContext = new DefaultScriptContext();
	
	protected void setUp() throws Exception {
		Runner.init("application.xml", null);
		baseContext.put("dynamicDataSource", BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBean("dynamicDataSource"));
	}
	
	public void testBase() throws Exception{
		ScriptEngine  engine = new DefaultTinyScriptEngine();
		ScriptContext context = new DefaultScriptContext();
		
		DataNode node = (DataNode) engine.execute(" node = createTree(\"0\",\"root\").addNode(\"10\",\"自然科学\").addNode(\"20\",\"社会科学\"); node.getChild(\"10\").addNode(\"11\",\"物理\"); return node;", context);
		assertNotNull(node);
		
		//基本属性
		assertEquals(2, node.getChildren().size());
		assertEquals(false, node.isLeaf());
		assertEquals(true, node.isRoot());
		assertEquals("root", node.getValue());
		
		//子节点属性
		assertEquals("自然科学", node.getChild(0).getValue());
		assertEquals("社会科学", node.getChild(1).getValue());
		
		assertEquals(false, node.getChild(0).isLeaf());
		assertEquals(true, node.getChild(1).isLeaf());
		
		assertEquals("物理", node.findNode("11").getValue());
		node.getChild(0).removeNode("11");
		assertEquals(null, node.findNode("11"));
		
	}
	
	/**
	 * 测试从数据集转换为tree
	 * <br>表结构需要满足存在菜单id和父菜单id，并且根节点的父菜单id为null
	 * @throws Exception
	 */
	public void testConvert() throws Exception{
		ScriptEngine engine = new DefaultTinyScriptEngine();
		DataSet dataSet = (DataSet) engine.execute("return dynamicDataSource.query(\"select * from menu\");",baseContext);
		assertNotNull(dataSet);
		
		ScriptContext context = new DefaultScriptContext();
		context.put("d", dataSet);
		
		DataNode node = (DataNode) engine.execute("return d.toTree(\"id\",\"parent_id\");", context);
		assertNotNull(node);
		
		//根节点
		assertEquals(false, node.isLeaf());
		assertEquals(true, node.isRoot());
		assertEquals("1", node.getName());
		
		//一级节点
		assertEquals(false, node.getChild(0).isLeaf());
		assertEquals("10", node.getChild(0).getName());
		assertEquals("自然科学", ((Map)node.getChild(0).getValue()).get("TITLE"));
		assertEquals(false, node.getChild(1).isLeaf());
		assertEquals("20", node.getChild(1).getName());
		assertEquals("社会科学", ((Map)node.getChild(1).getValue()).get("TITLE"));
		assertEquals(false, node.getChild(2).isLeaf());
		assertEquals("30", node.getChild(2).getName());
		assertEquals("玄学", ((Map)node.getChild(2).getValue()).get("TITLE"));
		
		//二级节点
		DataNode first = node.getChild(0);
		assertEquals(true, first.getChild(0).isLeaf());
		assertEquals("11", first.getChild(0).getName());
		assertEquals("物理", ((Map)first.getChild(0).getValue()).get("TITLE"));
		assertEquals(true, first.getChild(1).isLeaf());
		assertEquals("12", first.getChild(1).getName());
		assertEquals("化学", ((Map)first.getChild(1).getValue()).get("TITLE"));
		assertEquals(true, first.getChild(2).isLeaf());
		assertEquals("13", first.getChild(2).getName());
		assertEquals("生物", ((Map)first.getChild(2).getValue()).get("TITLE"));
	}
}
