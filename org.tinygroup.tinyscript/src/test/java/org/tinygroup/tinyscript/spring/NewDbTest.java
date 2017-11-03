package org.tinygroup.tinyscript.spring;

import java.io.File;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.tinyrunner.Runner;
import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptEngineFactory;
import org.tinygroup.tinyscript.dataset.DataSet;

public class NewDbTest {

	@BeforeClass
	public static void doBeforeClass() throws Exception {
		Runner.init("application.xml", null);
	}
	
	@Test
	public void testScript()  throws Exception {
		//获取spring方式的脚本引擎
		ScriptEngine engine = ScriptEngineFactory.createByBean("bean2");
		//加载脚本
		String script = FileUtil.readFileContent(new File("src/test/resources/other/sql3.tinyscript"), "utf-8");
		//获取序表对象
		DataSet dataSet = (DataSet) engine.execute(script);
		
		Assert.assertNotNull(dataSet);
	}
}
