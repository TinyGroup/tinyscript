package org.tinygroup.tinyscript;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.impl.DefaultTinyScriptEngine;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;

public class UpdateTest extends TestCase {

	public void  testMatch() throws Exception {
		
		ScriptEngine engine = new DefaultTinyScriptEngine();
		ScriptContext context = new DefaultScriptContext();
		
		DataSet orderDs  = (DataSet) engine.execute("a = \"WEIGHT\"; return readTxt(\"src/test/resources/testOrder.txt\").insertColumn(1,\"price\").int(a);");
		context.put("orderDs", orderDs);
		
		engine.execute("return orderDs.update(\"price\",\"WEIGHT*150\");",context);
		
		assertEquals(900, orderDs.getData(1, 1));
		assertEquals(450, orderDs.getData(2, 1));
		assertEquals(150, orderDs.getData(3, 1));
	}
	
	public void  testUpdateField() throws Exception {
		ScriptEngine engine = new DefaultTinyScriptEngine();
		ScriptContext context = new DefaultScriptContext();
		
		DataSet mailDs  = (DataSet) engine.execute("return readTxt(\"src/test/resources/mailCharge.txt\").long(\"MINVAL\",\"MAXVAL\");", context);
		context.put("mailDs", mailDs);
		
		//合并字段
		DataSet result = (DataSet) engine.execute("return mailDs.updateField((MINVAL,MAXVAL)->{f1=MINVAL+MAXVAL;f2=MINVAL*2;f3=MINVAL*4;},{\"f1\"},{\"MINVAL\",\"MAXVAL\"});",context);
		assertEquals(4, result.getColumns());
		
		mailDs  = (DataSet) engine.execute("return readTxt(\"src/test/resources/mailCharge.txt\").long(\"MINVAL\",\"MAXVAL\");", context);
		context.put("mailDs", mailDs);
		//拆分字段
		result = (DataSet) engine.execute("return mailDs.updateField(()->{d=new java.util.Date(); day=d.getDay(); hour=d.getHours(); },{\"day\",\"hour\"},null);",context);
		assertEquals(7, result.getColumns());
	}

}
