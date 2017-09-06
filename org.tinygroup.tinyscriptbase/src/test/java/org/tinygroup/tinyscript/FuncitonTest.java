package org.tinygroup.tinyscript;

import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

/**
 * 测试脚本引擎的内置函数
 * 
 * @author yancheng11334
 * 
 */
public class FuncitonTest extends TestCase {

	private ScriptEngine scriptEngine;

	protected void setUp() throws Exception {
		scriptEngine = new DefaultScriptEngine();
		scriptEngine.start();
	}

	public void testOperator() throws Exception {
		ScriptContext context = new DefaultScriptContext();
		// 测试引擎内置函数
		scriptEngine.addScriptFunction(new TimeFunction());
		scriptEngine.addScriptFunction(new ScaleFunction());

		// 非绑定的函数
		assertEquals(1000L, scriptEngine.execute("return time() ;", context));
		// 绑定对象的函数
		context.put("a", 10);
		assertEquals(50, scriptEngine.execute("return a.scale(5) ;", context));
	}

	public void testBaseMath() throws Exception {
		// 测试math相关函数
		assertEquals(3245.54d, scriptEngine.execute("return abs(-3245.54d) ;"));
		assertEquals(986, scriptEngine.execute("return abs(-986) ;"));
		assertEquals(100, scriptEngine.execute("return max(-1,5.6,100,45) ;"));
		assertEquals(-1, scriptEngine.execute("return min(-1,5.6,100,45) ;"));
		assertEquals(2.5d, scriptEngine.execute("return avg(1,2,3,4) ;"));
		assertEquals(10.9d, scriptEngine.execute("return sum(1,2,3,4.9d) ;"));
		assertEquals(2.0d, scriptEngine.execute("return sqrt(4) ;"));
		assertEquals(2, scriptEngine.execute("return int(sqrt(4));"));
	}

	public void testConvert() throws Exception {
		// 测试四类转换
		assertEquals(1, scriptEngine.execute("return int(1L);"));
		assertEquals(1, scriptEngine.execute("return int(1f);"));
		assertEquals(1, scriptEngine.execute("return int(1d);"));
		assertEquals(3333L, scriptEngine.execute("return long(3333);"));
		assertEquals(3333L, scriptEngine.execute("return long(3333.5f);"));
		assertEquals(3333L, scriptEngine.execute("return long(3333.6d);"));
		assertEquals(-88f, scriptEngine.execute("return float(-88);"));
		assertEquals(-88f, scriptEngine.execute("return float(-88L);"));
		assertEquals(-88.9f, scriptEngine.execute("return float(-88.9d);"));
		assertEquals(732d, scriptEngine.execute("return double(732);"));
		assertEquals(888734d, scriptEngine.execute("return double(888734L);"));
		assertEquals(56.45334d,
				scriptEngine.execute("return double(56.45334f);"));
		
		//补充日期转换
		Date date = new Date(0);
		assertEquals(date, scriptEngine.execute("return date(0L);"));
		assertEquals(date, scriptEngine.execute("return date(\"1970/1/1 08:00:00\",\"yyyy/MM/dd hh:mm:ss\");"));
	}
	
	public void testMath2() throws Exception {
		//测试统计函数
		
		//全距(极差)
		assertEquals(0,scriptEngine.execute("list=[3]; return range(list);"));
		assertEquals(8,scriptEngine.execute("list=[-1,2,3,4,5,7]; return range(list);"));
		assertEquals(14.0d,scriptEngine.execute("list=[3.0d,4.0d,-10.0d,3.0d]; return range(list);"));
		
		//中位数
		assertEquals(7,scriptEngine.execute("list=[2,10,4,8,7]; return median(list);"));
		assertEquals(5.5d,scriptEngine.execute("list=[2,10,4,8,7,3]; return median(list);"));
		
		//方差
		assertEquals(536d,scriptEngine.execute("list=[50,100,100,60,50]; return varp(list);"));
		assertEquals(3.6d,scriptEngine.execute("list=[73,70,75,72,70]; return varp(list);"));
		
		//标准差(均方差)
		assertEquals(Math.sqrt(536d),scriptEngine.execute("list=[50,100,100,60,50]; return stdevp(list);"));
		assertEquals(Math.sqrt(3.6d),scriptEngine.execute("list=[73,70,75,72,70]; return stdevp(list);"));
		
		//测试集合元素的聚合
		List result = (List) scriptEngine.execute("num1=[1,2,3]; num2=[1,2,3,4]; num3=[-1,1]; list=[num1,num2,num3];return avg(list);");
		assertEquals(2d,result.get(0));
		assertEquals(2.5d,result.get(1));
		assertEquals(0d,result.get(2));
		
		result = (List) scriptEngine.execute("array={{1,2,3},{1,2,3,4},{-1,1}};return avg(array);");
		assertEquals(2d,result.get(0));
		assertEquals(2.5d,result.get(1));
		assertEquals(0d,result.get(2));
		
		//测试集合元素对一般数学函数的操作
		result = (List) scriptEngine.execute("return pow([1,2,3,4],3);");
		assertEquals(1d,result.get(0));
		assertEquals(8d,result.get(1));
		assertEquals(27d,result.get(2));
		assertEquals(64d,result.get(3));
		
		result = (List) scriptEngine.execute("return pow([1,2,3,4],[1,1,3,3]);");
		assertEquals(1d,result.get(0));
		assertEquals(2d,result.get(1));
		assertEquals(27d,result.get(2));
		assertEquals(64d,result.get(3));
	}
	
	//测试四舍五入
	public void testRound() throws Exception {
		//浮点数按位四舍五入
		assertEquals(3.47f,scriptEngine.execute("return round(3.46583284f,2);"));
		assertEquals(3.46f,scriptEngine.execute("return round(3.46483284f,2);"));
		assertEquals(8.094d,scriptEngine.execute("return round(8.0944d,3);"));
		assertEquals(8.095d,scriptEngine.execute("return round(8.0945d,3);"));
		//整数原样返回
		assertEquals(1,scriptEngine.execute("return round(2-1,2);"));
		assertEquals(10L,scriptEngine.execute("return round(10L,5);"));
		
		//集合方式处理浮点数
		List result = (List) scriptEngine.execute("return round([3.467f,-56.009d],1);");  //保留相同小数位
		assertEquals(3.5f,result.get(0));
		assertEquals(-56.0d,result.get(1));
		result = (List) scriptEngine.execute("return round([3.467f,-56.009d],[1,2]);");   //保留不同小数位
		assertEquals(3.5f,result.get(0));
		assertEquals(-56.01d,result.get(1));
	}

	public void testTrigonometric() throws Exception {
		// 测试三角函数
		assertEquals(3.141592653589793d,
				scriptEngine.execute("return acos(-1) ;"));
		assertEquals(0.0d, scriptEngine.execute("return acos(cos(0)) ;"));
		assertEquals(-1.5707963267948966d,
				scriptEngine.execute("return asin(-1) ;"));
		assertEquals(0.0d, scriptEngine.execute("return asin(sin(0)) ;"));
		assertEquals(0.7853981633974483d,
				scriptEngine.execute("return atan(1) ;"));
		assertEquals(0.0d, scriptEngine.execute("return atan(tan(0)) ;"));
	}
}
