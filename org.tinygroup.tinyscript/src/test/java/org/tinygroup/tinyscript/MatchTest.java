package org.tinygroup.tinyscript;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.ComputeEngine;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.impl.DefaultComputeEngine;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;

public class MatchTest extends TestCase {
	
	public void  testMatch() throws Exception {
		ComputeEngine engine = new DefaultComputeEngine();
		ScriptContext context = new DefaultScriptContext();
		
		DataSet ruleDs  = (DataSet) engine.execute("return readTxt(\"src/test/resources/mailCharge.txt\");");
		DataSet orderDs  = (DataSet) engine.execute("return readTxt(\"src/test/resources/testOrder.txt\");");
		
		context.put("ruleDs", ruleDs);
		context.put("orderDs", orderDs);
		
		//匹配花费规则
		DataSet result1 = (DataSet) engine.execute(" costRuleDs = ruleDs.filter(()->{return FIELD == \"COST\";}).sort(\"MINVAL\"); return orderDs.match(costRuleDs,COST > MINVAL);",context);
		assertEquals(1, result1.getRows());
		assertEquals(9, result1.getColumns());
		assertEquals("COST",result1.getData(1, 6));
		assertEquals("JOSH3",result1.getData(1, 2));
		
		//匹配重量规则
		DataSet result2 = (DataSet) engine.execute(" weightRuleDs = ruleDs.filter(()->{return FIELD == \"WEIGHT\";}).sort(\"MINVAL\"); return orderDs.match(weightRuleDs,\"WEIGHT >= MINVAL \"+\"&& WEIGHT <MAXVAL\");",context);
		assertEquals(4, result2.getRows());
		assertEquals(9, result2.getColumns());
		assertEquals("WEIGHT",result2.getData(1, 6));
		assertEquals("DRAKE",result2.getData(1, 2));
		assertEquals("1",result2.getData(1, 7));
		assertEquals("5",result2.getData(1, 8));
	}

}
