package org.tinygroup.tinyscript;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.tinyrunner.Runner;
import org.tinygroup.tinyscript.config.ScriptComponentConfig;
import org.tinygroup.tinyscript.config.ScriptFlowConfig;
import org.tinygroup.tinyscript.config.ScriptFlowNodeConfig;
import org.tinygroup.tinyscript.config.ScriptInputParameterConfig;
import org.tinygroup.tinyscript.config.ScriptOutputParameterConfig;

public class ScriptFlowTest {

	@BeforeClass
	public static void doBeforeClass() throws Exception {
		Runner.init("application.xml", null);
	}
	
	@Test
	public void testComponent() throws Exception {
		ScriptFlowManager manager = BeanContainerFactory.getBeanContainer(ScriptFlowTest.class.getClassLoader()).getBean("scriptFlowManager");
		Assert.assertNotNull(manager);
		
		ScriptComponentConfig component = manager.getScriptComponentConfig("c000001");
		
		//验证组件基本信息
		Assert.assertEquals("c000001", component.getId());
		Assert.assertEquals("加法组件", component.getName());
		Assert.assertEquals("组件注释信息，&&&", component.getDescription());
		
		//验证输入参数
		Assert.assertEquals(2, component.getInputList().size());
		ScriptInputParameterConfig input = component.getInputList().get(0); 
		Assert.assertEquals("a", input.getName());
		Assert.assertEquals(null,input.getType());
		Assert.assertEquals("被加数",input.getDescription());
		
		//验证输出参数
		Assert.assertEquals(1, component.getOutputList().size());
		ScriptOutputParameterConfig output = component.getOutputList().get(0);
		Assert.assertEquals(null, output.getType());
		Assert.assertEquals("false", output.getNullable());
		Assert.assertEquals("加法运算结果", output.getDescription());
		
		//执行组件
		ScriptFlowExecutor executor = BeanContainerFactory.getBeanContainer(ScriptFlowTest.class.getClassLoader()).getBean("scriptFlowExecutor");
		
		//构建运行环境参数
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("a", 10L);
		map.put("b", 5L);
		
		//执行单个脚本组件
		Assert.assertEquals(15L, executor.executeComponent("c000001", map));
		
	}
	
	@Test
	public void testFlow() throws Exception {
		ScriptFlowManager manager = BeanContainerFactory.getBeanContainer(ScriptFlowTest.class.getClassLoader()).getBean("scriptFlowManager");
		Assert.assertNotNull(manager);
		
		ScriptFlowConfig flow = manager.getScriptFlowConfig("f00001");
		Assert.assertEquals("f00001", flow.getId());
		Assert.assertEquals("四则运算", flow.getName());
		
		//获取顶层节点
		ScriptFlowNodeConfig rootNode = flow.getRootNode();
		Assert.assertNotNull(rootNode);
		Assert.assertEquals("n000003", rootNode.getNodeId());
		Assert.assertEquals("c000002", rootNode.getComponentId());
		
		//获取依赖节点
		List<ScriptFlowNodeConfig> nodes = flow.getDependentNodeList(rootNode.getNodeId());
		Assert.assertEquals(2, nodes.size());
		
		//执行组件
		ScriptFlowExecutor executor = BeanContainerFactory.getBeanContainer(ScriptFlowTest.class.getClassLoader()).getBean("scriptFlowExecutor");
		
		//构建运行环境参数
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(1);
		parameters.add(2);
		parameters.add(3);
		parameters.add(4);
		
		//执行流程
		Assert.assertEquals(21, executor.executeFlow("f00001", parameters));
	}
}
