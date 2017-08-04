package org.tinygroup.tinyscript.function.random;

import java.util.Random;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * double类型的随机数
 * @author yancheng11334
 *
 */
public class DoubleRandFunction extends AbstractRandFunction<Double>{

	public String getNames() {
		return "randDouble";
	}

	protected Double rand(Object limit) throws ScriptException {
		Random rom = new Random();
		if(limit==null){
		   //返回[0.0-1.0)的double值
		   return rom.nextDouble();
		}else{
		   double d = ExpressionUtil.convertDouble(limit);
		   //返回[0.0-d)的double值
		   return rom.nextDouble()*d;
		}
	}

	

}
