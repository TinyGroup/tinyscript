package org.tinygroup.tinyscript.function.math;

import java.math.BigDecimal;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * 四舍五入函数
 * @author yancheng11334
 *
 */
public class MathRoundFunction extends AbstractMathCollectionFunction{

	public String getNames() {
		return "round";
	}

	protected int getParameterCount() {
		return 2;
	}

	protected Object computeItem(Object... parameters) throws ScriptException {
		if(parameters[0] instanceof Float){
			int s = ExpressionUtil.convertInteger(parameters[1]);
			BigDecimal number = new BigDecimal((Float)parameters[0]);
			return number.setScale(s, BigDecimal.ROUND_HALF_UP).floatValue(); 
		}else if(parameters[0] instanceof Double){
			int s = ExpressionUtil.convertInteger(parameters[1]);
			BigDecimal number = new BigDecimal((Double)parameters[0]);
			return number.setScale(s, BigDecimal.ROUND_HALF_UP).doubleValue(); 
		}else if(parameters[0] instanceof BigDecimal){
			int s = ExpressionUtil.convertInteger(parameters[1]);
			BigDecimal number = (BigDecimal) parameters[0];
			return number.setScale(s, BigDecimal.ROUND_HALF_UP); 
		}else {
		   //非浮点数原样返回
		   return parameters[0];
		}
	}

}
