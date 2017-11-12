package org.tinygroup.tinyscript;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.impl.DefaultTinyScriptEngine;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;

public class UpdateTest extends TestCase {

	public void testMatch() throws Exception {

		ScriptEngine engine = new DefaultTinyScriptEngine();
		ScriptContext context = new DefaultScriptContext();

		AbstractDataSet orderDs = (AbstractDataSet) engine
				.execute("return readTxt(\"src/test/resources/testOrder.txt\");");
		context.put("orderDs", orderDs);
		int insertColumnIndex = orderDs.isIndexFromOne() ? 1 : 0;
		orderDs = (AbstractDataSet) engine.execute(
				"a = \"WEIGHT\"; return orderDs.insertColumn(" + insertColumnIndex + ",\"price\").int(a);", context);
		engine.execute("return orderDs.update(\"price\",\"WEIGHT*150\");", context);

		assertEquals(900, orderDs.getData(orderDs.getShowIndex(0), orderDs.getShowIndex(0)));
		assertEquals(450, orderDs.getData(orderDs.getShowIndex(1), orderDs.getShowIndex(0)));
		assertEquals(150, orderDs.getData(orderDs.getShowIndex(2), orderDs.getShowIndex(0)));
	}

	public void testUpdateField() throws Exception {
		ScriptEngine engine = new DefaultTinyScriptEngine();
		ScriptContext context = new DefaultScriptContext();

		DataSet mailDs = (DataSet) engine
				.execute("return readTxt(\"src/test/resources/mailCharge.txt\").long(\"MINVAL\",\"MAXVAL\");", context);
		context.put("mailDs", mailDs);

		// 合并字段
		DataSet result = (DataSet) engine.execute(
				"return mailDs.updateField((MINVAL,MAXVAL)->{f1=MINVAL+MAXVAL;f2=MINVAL*2;f3=MINVAL*4;},{\"f1\"},{\"MINVAL\",\"MAXVAL\"});",
				context);
		assertEquals(4, result.getColumns());

		mailDs = (DataSet) engine
				.execute("return readTxt(\"src/test/resources/mailCharge.txt\").long(\"MINVAL\",\"MAXVAL\");", context);
		context.put("mailDs", mailDs);
		// 拆分字段
		result = (DataSet) engine.execute(
				"return mailDs.updateField(()->{d=new java.util.Date(); day=d.getDay(); hour=d.getHours(); },{\"day\",\"hour\"},null);",
				context);
		assertEquals(7, result.getColumns());
	}

}
