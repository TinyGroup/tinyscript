package org.tinygroup.tinyscript;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tinygroup.tinyrunner.Runner;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.GroupDataSet;
import org.tinygroup.tinyscript.impl.DefaultTinyScriptEngine;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;

public class GroupTest  {
	
	@BeforeClass
	public static void doBeforeClass() throws Exception {
		Runner.init("application.xml", null);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testGroups() throws Exception {
		//测试分组
		ScriptEngine engine = new DefaultTinyScriptEngine();
		ScriptContext context = new DefaultScriptContext();
		
		DataSet dataSet = (DataSet) engine.execute("return dynamicDataSource.query(\"select * from test2\");");
		Assert.assertNotNull(dataSet);
		
		context.put("d", dataSet);
		
		//分组之后的结果集
		List<DataSet> result = (List<DataSet>) engine.execute("d.group(\"age\").getGroups();", context);
		
		context.put("r", result);
		Assert.assertEquals(5,result.size());
		
		//20岁组
		Assert.assertEquals(1,engine.execute("return r[1].getRows();",context));
		Assert.assertEquals("莫小二",engine.execute("return r[1].getData(1, 2);",context));
		Assert.assertEquals(20,engine.execute("return r[1].getData(1, 3);",context));
		
		//30岁组
		Assert.assertEquals(2,engine.execute("return r[2].getRows();",context));
		Assert.assertEquals("张大名",engine.execute("return r[2].getData(1, 2);",context));
		Assert.assertEquals(30,engine.execute("return r[2].getData(1, 3);",context));
		Assert.assertEquals("邱少云",engine.execute("return r[2].getData(2, 2);",context));
		Assert.assertEquals(30,engine.execute("return r[2].getData(2, 3);",context));
		
		//78岁组
		Assert.assertEquals(1,engine.execute("return r[3].getRows();",context));
		Assert.assertEquals("李德宏",engine.execute("return r[3].getData(1, 2);",context));
		Assert.assertEquals(78,engine.execute("return r[3].getData(1, 3);",context));
		
		//45岁组
		Assert.assertEquals(3,engine.execute("return r[4].getRows();",context));
		Assert.assertEquals("赵方毅",engine.execute("return r[4].getData(1, 2);",context));
		Assert.assertEquals(45,engine.execute("return r[4].getData(1, 3);",context));
		Assert.assertEquals("冯若依曼",engine.execute("return r[4].getData(2, 2);",context));
		Assert.assertEquals(45,engine.execute("return r[4].getData(2, 3);",context));
		Assert.assertEquals("赵真",engine.execute("return r[4].getData(3, 2);",context));
		Assert.assertEquals(45,engine.execute("return r[4].getData(3, 3);",context));
		
		result = (List<DataSet>) engine.execute(" name =\"age\"; d.group(name,\"sex\").getGroups();", context);
		Assert.assertEquals(7,result.size());
	}
	
	@Test
	public void testGroup() throws Exception{
		ScriptEngine engine = new DefaultTinyScriptEngine();
		ScriptContext context = new DefaultScriptContext();
		
		DataSet dataSet = (DataSet) engine.execute("return dynamicDataSource.query(\"select * from test2\");");
		Assert.assertNotNull(dataSet);
		
		context.put("d", dataSet);
		
		DataSet result = null;
		
		//测试第一种分组
		result = (DataSet)engine.execute("d.group(\"id\");", context);
		Assert.assertEquals(4,result.getColumns());
		Assert.assertEquals(10,result.getRows());
		
		result = (DataSet)engine.execute("d.group(\"age\");", context);
		Assert.assertEquals(4,result.getColumns());
		Assert.assertEquals(5,result.getRows());
		
		//测试第二种分组
		result = (DataSet)engine.execute("d.group(\"id\").select(\"fullname\",\"age\");", context);
		Assert.assertEquals(2,result.getColumns());
		Assert.assertEquals(10,result.getRows());
		
		result = (DataSet)engine.execute("d.group(\"age\",\"sex\").select(\"id\",\"fullname\");", context);
		Assert.assertEquals(2,result.getColumns());
		Assert.assertEquals(7,result.getRows());
		
		//测试第三种分组
		result = (DataSet)engine.execute("d.group(\"sex\").minGroup(\"age\").maxGroup(\"age\");", context);
		Assert.assertEquals(6,result.getColumns());
		Assert.assertEquals(2,result.getRows());
		//Assert.assertEquals("min(age)",result.getFields().get(0).getName());
		//Assert.assertEquals("max(age)",result.getFields().get(1).getName());
		Assert.assertEquals(12,result.getData(1, 5));
		Assert.assertEquals(78,result.getData(1, 6));
		Assert.assertEquals(12,result.getData(2, 5));
		Assert.assertEquals(45,result.getData(2, 6));
		
		//测试第四种分组
		result = (DataSet)engine.execute("d.group(\"sex\").minGroup(\"age\").maxGroup(\"age\").countGroup(\"id\");", context);
		Assert.assertEquals(7,result.getColumns());
		Assert.assertEquals(2,result.getRows());
		
		Assert.assertEquals("莫小二",result.getData(1, 2));
		Assert.assertEquals(20,result.getData(1, 3));
		Assert.assertEquals(12,result.getData(1, 5));
		Assert.assertEquals(78,result.getData(1, 6));
		Assert.assertEquals(4,result.getData(1, 7));
		
		Assert.assertEquals("张大名",result.getData(2, 2));
		Assert.assertEquals(30,result.getData(2, 3));
		Assert.assertEquals(12,result.getData(2, 5));
		Assert.assertEquals(45,result.getData(2, 6));
		Assert.assertEquals(6,result.getData(2, 7));
		
		//测试目前支持的五种聚合函数
		result = (DataSet)engine.execute("d.group(\"sex\").countGroup(\"id\").sumGroup(\"age\").avgGroup(\"age\");", context);
		Assert.assertEquals(7,result.getColumns());
		Assert.assertEquals(2,result.getRows());
		
		Assert.assertEquals(4,result.getData(1, 5));
		Assert.assertEquals(155,result.getData(1, 6));
		Assert.assertEquals(155d/4,result.getData(1, 7));
		
		Assert.assertEquals(6,result.getData(2, 5));
		Assert.assertEquals(174,result.getData(2, 6));
		Assert.assertEquals(174d/6,result.getData(2, 7));
	}
	
	@Test
	public void testGroupStaged() throws Exception{
		ScriptEngine engine = new DefaultTinyScriptEngine();
		ScriptContext context = new DefaultScriptContext();
		
		DataSet dataSet = (DataSet) engine.execute("return dynamicDataSource.query(\"select * from test2\");");
		Assert.assertNotNull(dataSet);
		
		context.put("d", dataSet);
		
		GroupDataSet groupDataSet = (GroupDataSet) engine.execute("return d.groupStaged(AGE<=18,AGE>=18 && AGE <60 , AGE>=60);", context);
		List<DynamicDataSet> results = groupDataSet.getGroups();
		Assert.assertEquals(3,results.size());
		Assert.assertEquals(3,results.get(0).getRows());  //未成年人
		Assert.assertEquals(6,results.get(1).getRows());  //成年人
		Assert.assertEquals(1,results.get(2).getRows());  //老人
		
		groupDataSet = (GroupDataSet) engine.execute(" case1=\"AGE<=18\"; case2=\"AGE>=18 && AGE <60 \"; case3=\"AGE>=60\"; return d.groupStaged(case1,case2,case3);", context);
		results = groupDataSet.getGroups();
		Assert.assertEquals(3,results.size());
	}
	
	@Test
	public void testGroupDynamic() throws Exception{
		ScriptEngine engine = new DefaultTinyScriptEngine();
		ScriptContext context = new DefaultScriptContext();
		
		DataSet dataSet = (DataSet) engine.execute("return dynamicDataSource.query(\"select * from test2\");");
		Assert.assertNotNull(dataSet);
		
		context.put("d", dataSet);
		
		GroupDataSet groupDataSet = (GroupDataSet) engine.execute("return d.groupDynamic(ID%5);", context);
		List<DynamicDataSet> results = groupDataSet.getGroups();
		Assert.assertEquals(5,results.size()); //10条记录均匀分配到5组
		Assert.assertEquals(2,results.get(0).getRows());  
		Assert.assertEquals(2,results.get(1).getRows());  
		Assert.assertEquals(2,results.get(2).getRows());  
		Assert.assertEquals(2,results.get(3).getRows());  
		Assert.assertEquals(2,results.get(4).getRows());  
		
		 groupDataSet = (GroupDataSet) engine.execute("return d.groupDynamic(\"ID%5\");", context);
		 results = groupDataSet.getGroups();
		 Assert.assertEquals(5,results.size()); //10条记录均匀分配到5组
	}
}
