package org.tinygroup.tinyscript.function.random;

import java.util.Random;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * int类型的随机数
 * @author yancheng11334
 *
 */
public class IntRandFunction extends AbstractRandFunction<Integer>{

	public String getNames() {
	   return "randInt";
	}

	protected Integer rand(Object limit) throws ScriptException {
		Random rom = new Random();
		if(limit==null){
		   return rom.nextInt(Integer.MAX_VALUE);
		}else{
		   int n = ExpressionUtil.convertInteger(limit);
		   //返回[0-n)的int值
		   return rom.nextInt(n);
		}
	}

}
