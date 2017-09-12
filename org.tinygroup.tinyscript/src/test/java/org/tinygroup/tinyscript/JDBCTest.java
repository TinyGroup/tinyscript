package org.tinygroup.tinyscript;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tinygroup.tinyrunner.Runner;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DataSetRow;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.impl.DefaultTinyScriptEngine;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;

/**
 * 测试jdbc方式操作数据库
 * 
 * @author yancheng11334
 * 
 */
public class JDBCTest {

	@BeforeClass
	public static void doBeforeClass() throws Exception {
		Runner.init("application.xml", null);
	}

	@Test
	public void testBase() throws Exception {
		ScriptEngine engine = new DefaultTinyScriptEngine();
		DataSource source = (DataSource) engine
				.execute("return dynamicDataSource;");
		Assert.assertNotNull(source);

		// 测试优化后的sql操作(自动释放连接)
		Assert.assertEquals(
				1,
				engine.execute("return dynamicDataSource.execute(\"DELETE FROM test1 WHERE  id=4\");"));
		Assert.assertEquals(
				1,
				engine.execute("return dynamicDataSource.execute(\"INSERT INTO test1 (id,fullname,state) VALUES (4,'Violet Torlay','Montala')\");"));
	}

	// 测试原生写法
	@Test
	public void testSource() throws Exception {
		ScriptEngine engine = new DefaultTinyScriptEngine();

		engine.execute("conn = dynamicDataSource.getConnection(); statement = conn.createStatement(); statement.executeUpdate(\"DELETE FROM test1 WHERE  id=4\"); if(statement!=null){statement.close();}; if(conn!=null){conn.close();};");
		engine.execute("conn = dynamicDataSource.getConnection(); statement = conn.createStatement(); statement.executeUpdate(\"INSERT INTO test1 (id,fullname,state) VALUES (4,'Violet Torlay','Montala')\"); if(statement!=null){statement.close();}; if(conn!=null){conn.close();};");
	}
	
	//测试数据集
	@Test
	public void testDataSet() throws Exception {
		ScriptEngine engine = new DefaultTinyScriptEngine();
		DataSet dataSet = (DataSet) engine.execute("return dynamicDataSource.query(\"select * from test1\");");
		Assert.assertNotNull(dataSet);
		
		ScriptContext context = new DefaultScriptContext();
		context.put("d", dataSet);
		
		//测试DataSet
		Assert.assertEquals(4,engine.execute("return d.getRows();", context));    //4条记录
		Assert.assertEquals(3,engine.execute("return d.getColumns();", context)); //3个字段
		
		Assert.assertEquals("ID",engine.execute("return d.getFields().get(0).getName();", context)); //H2数据库字段大写
		
		
		Assert.assertEquals("New York",engine.execute("return d.getData(3,3);", context));
		engine.execute("d.setData(3,3,\"Beijing\");", context);
		Assert.assertEquals("Beijing",engine.execute("return d.getData(3,3);", context));
		
		dataSet.setIndexFromOne(false);
		Assert.assertEquals("Beijing",engine.execute("return d.getData(2,2);", context));
		dataSet.setIndexFromOne(true);
		
		//测试DataSetRow
		DataSetRow dRow = (DataSetRow) engine.execute("return d[3];", context);
		Assert.assertEquals(3,dRow.getData(1));
		
		Assert.assertEquals(3,engine.execute("return d[3][1];", context));
		engine.execute("d[3][1] = 55;", context);
		Assert.assertEquals(55,engine.execute("return d[3][\"id\"];", context));
		engine.execute("d[3][\"id\"] = 3;", context);
		Assert.assertEquals(3,engine.execute("return d[3][1];", context));
		
		//测试DataColumn(必须采用先行后列的规范写法)
		Assert.assertEquals("Tom Smith",engine.execute("return d[1][\"fullname\"];", context));
		engine.execute("d[1][\"fullname\"] = \"work\" ;", context);
		Assert.assertEquals("work",engine.execute("return d[1][\"fullname\"];", context));
		
		//增加一行记录
		engine.execute("d.insertRow(3);", context);
		Assert.assertEquals(null,engine.execute("return d[3][1];", context));
		Assert.assertEquals(3,engine.execute("return d[4][1];", context));
		
		//删除一行记录
		engine.execute("d.deleteRow(3);", context);
		Assert.assertEquals(3,engine.execute("return d[3][1];", context));
		
		//行记录赋值
		Assert.assertEquals(false,engine.execute("return d[3][1]== d[2][1];", context));
		engine.execute("d[2]=d[3];", context);
		Assert.assertEquals(true,engine.execute("return d[3][1]== d[2][1];", context));
		
		//列记录
		Field f4 = new Field("sex","性别","");
		context.put("f", f4);
		engine.execute("d.insertColumn(3,f); d.fill(3,\"male\");", context);
		Assert.assertEquals(4,engine.execute("return d.getColumns();", context));
		
		Assert.assertEquals("male",engine.execute("return d[2][3];", context));
		Assert.assertEquals("male",engine.execute("return d[3][3];", context));
		
		engine.execute("d[\"sex\"].fill(\"female\");", context);
		Assert.assertEquals("female",engine.execute("return d[2][3];", context));
		Assert.assertEquals("female",engine.execute("return d[3][3];", context));
		
		engine.execute("d.sex.fill(\"male\");", context);
		Assert.assertEquals("male",engine.execute("return d[2][3];", context));
		Assert.assertEquals("male",engine.execute("return d[3][3];", context));
		
		engine.execute("d.deleteColumn(3);", context);
		Assert.assertEquals(3,engine.execute("return d.getColumns();", context));
		
		
	}

	@Test
	public void testDataSet2() throws Exception {
		ScriptEngine engine = new DefaultTinyScriptEngine();
		ScriptContext context = new DefaultScriptContext();
		
		//测试过滤
		Assert.assertEquals(2,engine.execute("nd = dynamicDataSource.query(\"select * from test1\"); return nd.filter(()->{return STATE==\"New York\" || FULLNAME==\"Tom Smith\";}).getRows();", context));
		Assert.assertEquals(2,engine.execute("nd = dynamicDataSource.query(\"select * from test1\"); nd.remove(()->{return STATE==\"New York\" || FULLNAME==\"Tom Smith\";}); return nd.getRows();", context));
		Assert.assertEquals(false,engine.execute("nd = dynamicDataSource.query(\"select * from test1\"); nd2 = nd.filter(()->{return STATE==\"New York\" || FULLNAME==\"Tom Smith\";}); return nd2.getRows() == nd.getRows();", context));
	}
	
	//测试基于excel单元格下标方式访问数据集
	@Test
	public void testDataSet3() throws Exception {
		ScriptEngine engine = new DefaultTinyScriptEngine();
		ScriptContext context = new DefaultScriptContext();
		
		DataSet dataSet = (DataSet) engine.execute("return dynamicDataSource.query(\"select * from test1\");");
		Assert.assertNotNull(dataSet);
		context.put("d", dataSet);
		
		Assert.assertEquals(1,engine.execute("return d[\"A1\"];",context));
		Assert.assertEquals("Tom Smith",engine.execute("return d[\"b1\"];",context));
		Assert.assertEquals(2,engine.execute("return d[\"a2\"];",context));
		Assert.assertEquals("Michael Jones",engine.execute("return d[\"B2\"];",context));
		
		engine.execute("d[\"A1\"] = 100;",context);
		Assert.assertEquals(100,engine.execute("return d[\"A1\"];",context));
	}
	
	//测试基于excel单元格下标方式访问数据集
	@Test
	public void testDataSet4() throws Exception {
		ScriptEngine engine = new DefaultTinyScriptEngine();
		ScriptContext context = new DefaultScriptContext();
		
		DataSet dataSet = (DataSet) engine.execute("return dynamicDataSource.query(\"select * from test1\");");
		Assert.assertNotNull(dataSet);
		context.put("d", dataSet);
		
		Assert.assertEquals(1,engine.execute("return d.A1;",context));
		Assert.assertEquals("Tom Smith",engine.execute("return d.b1;",context));
		Assert.assertEquals(2,engine.execute("return d.a2;",context));
		Assert.assertEquals("Michael Jones",engine.execute("return d.B2;",context));
		
		engine.execute("d.A1 = 100;",context);
		Assert.assertEquals(100,engine.execute("return d.A1;",context));
		
	}
	
	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testCopy() throws Exception {
		
		ScriptEngine engine = new DefaultTinyScriptEngine();
		ScriptContext context = new DefaultScriptContext();
		
		//测试序列
		List list = new ArrayList();
		list.add("1");
		list.add("2");
		list.add("3");
		context.put("list", list);
		List list2 = (List)engine.execute("list2 = list.copy(); list2.remove(0); return list2;",context);
		
		Assert.assertEquals(3,list.size());
		Assert.assertEquals(2,list2.size());
		
		//测试序表
		DataSet d1 = (DataSet) engine.execute("return dynamicDataSource.query(\"select * from test2\");");
		context.put("d1", d1);
		DataSet d2 = (DataSet) engine.execute("return d1.copy().deleteColumn(4);",context);
		context.put("d2", d2);
		Assert.assertEquals(4,d1.getColumns());
		Assert.assertEquals(3,d2.getColumns());
		Assert.assertEquals(78,engine.execute("return d1[3][3];",context));
		engine.execute("d1[3][3] = 80;",context);
		Assert.assertEquals(80,engine.execute("return d1[3][3];",context));
		Assert.assertEquals(78,engine.execute("return d2[3][3];",context));  
	}
	
	@Test
	public void testSelect() throws Exception {
		ScriptEngine engine = new DefaultTinyScriptEngine();
		
		DataSet dataSet = (DataSet) engine.execute("return dynamicDataSource.query(\"select * from test2\");");
		Assert.assertNotNull(dataSet);
		
		Assert.assertEquals(4,dataSet.getColumns());
		
		engine.setIndexFromOne(false);
		Assert.assertEquals(2,engine.execute("return dynamicDataSource.query(\"select * from test2\").select(\"id\",\"age\").getColumns();"));  //测试字段名
		Assert.assertEquals(3,engine.execute("return dynamicDataSource.query(\"select * from test2\").select(1,2,3).getColumns();"));     //测试字段下标
		Assert.assertEquals(2,engine.execute("return dynamicDataSource.query(\"select * from test2\").select(1,\"age\").getColumns();"));     //测试字段名、字段下标混用
		
		Assert.assertEquals(2,engine.execute("return dynamicDataSource.query(\"select * from test2\").select(\"id\",\"id\",\"age\").getColumns();"));  //测试重复字段名
		Assert.assertEquals(2,engine.execute("return dynamicDataSource.query(\"select * from test2\").select(1,1,1,2).getColumns();"));     //测试重复字段下标
		Assert.assertEquals(1,engine.execute("return dynamicDataSource.query(\"select * from test2\").select(0,\"id\").getColumns();"));     //测速重复混用
		
		DataSet ds2 = (DataSet) engine.execute("return dynamicDataSource.query(\"select * from test2\").select(0);");
		Assert.assertEquals("ID",ds2.getFields().get(0).getName()); 
		
		engine.setIndexFromOne(true);
		ds2 = (DataSet) engine.execute("return dynamicDataSource.query(\"select * from test2\").select(1);");
		Assert.assertEquals("ID",ds2.getFields().get(0).getName()); 
	}
	
	@Test
	public void testSort() throws Exception {
		//测试序表排序
		
		ScriptEngine engine = new DefaultTinyScriptEngine();
		ScriptContext context = new DefaultScriptContext();
		
		DataSet dataSet = (DataSet) engine.execute("return dynamicDataSource.query(\"select * from test2\");");
		Assert.assertNotNull(dataSet);
		//排序前，按id顺序
		Assert.assertEquals("莫小二",dataSet.getData(1, 2));
		Assert.assertEquals("张大名",dataSet.getData(2, 2));
		Assert.assertEquals("李德宏",dataSet.getData(3, 2));
		
		//执行排序
		context.put("d", dataSet);
		engine.execute("d.sort(\" id desc\");", context);
		Assert.assertEquals("王三",dataSet.getData(1, 2));
		Assert.assertEquals("邱少云",dataSet.getData(2, 2));
		Assert.assertEquals("赵真",dataSet.getData(3, 2));
		
		//执行排序
		engine.execute("d.sort(\"age desc,sex asc\");", context);
		Assert.assertEquals("李德宏",dataSet.getData(1, 2));
		Assert.assertEquals("Tom",dataSet.getData(8, 2));
		Assert.assertEquals("John",dataSet.getData(9, 2));
		Assert.assertEquals("王三",dataSet.getData(10, 2));
	}
	
}
