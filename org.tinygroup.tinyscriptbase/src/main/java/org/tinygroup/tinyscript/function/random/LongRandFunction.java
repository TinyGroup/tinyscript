package org.tinygroup.tinyscript.function.random;

import java.util.Random;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * long类型的随机数
 * @author yancheng11334
 *
 */
public class LongRandFunction extends AbstractRandFunction<Long>{

	public String getNames() {
		return "randLong";
	}

	protected Long rand(Object limit) throws ScriptException {
		Random rom = new Random();
		if(limit==null){
		   return Math.abs(rom.nextLong());
		}else{
		   long n = ExpressionUtil.convertLong(limit);
		   return (long)(rom.nextDouble()*n);
		}
	}

}
