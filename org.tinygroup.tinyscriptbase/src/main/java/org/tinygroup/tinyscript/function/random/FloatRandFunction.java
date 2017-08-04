package org.tinygroup.tinyscript.function.random;

import java.util.Random;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * float类型的随机数
 * @author yancheng11334
 *
 */
public class FloatRandFunction extends AbstractRandFunction<Float>{

	public String getNames() {
		return "randFloat";
	}

	protected Float rand(Object limit) throws ScriptException {
		Random rom = new Random();
		if(limit==null){
		   //返回[0.0-1.0)的float值
		   return rom.nextFloat();
		}else{
		   float f = ExpressionUtil.convertFloat(limit);
		   //返回[0.0-f)的float值
		   return rom.nextFloat()*f;
		}
	}

}
