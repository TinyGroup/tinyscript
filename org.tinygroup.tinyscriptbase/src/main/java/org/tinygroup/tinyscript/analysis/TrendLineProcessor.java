package org.tinygroup.tinyscript.analysis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.ExpressionUtil;

/**
 * 线性趋势线模型
 * @author yancheng11334
 *
 */
public class TrendLineProcessor implements AnalysisModelProcessor{

	public String getName() {
		return "trendLine";
	}

	/**
	 * 根据最小二乘法计算趋势线公式 y=ax+b 的a,b值，
     * 并根据算出来的a,b值计算出每个x对应的y值，
     * 即为趋势线的y值，返回y值数组
	 */
	public List<Object> analyse(List<Object> dataList, ScriptContext context,
			Object... configs) throws ScriptException {
		double xs =0d;  //∑x
		double ys =0d;  //∑y
		double xxs =0d; //∑xx
		double xys =0d; //∑xy
		int num = dataList.size();
		int scale = 2; //默认保留2位精度
		if(configs!=null && configs.length>0){
		    scale = ExpressionUtil.convertInteger(configs[0]);
		}
		for(int i=0;i<num;i++){
			double y = ExpressionUtil.convertDouble(dataList.get(i));
			xs += (i+1);
			ys += y;
			xxs += (i+1)*(i+1);
			xys += (i+1)*y;
		}
		
		//计算a,b值
		//a=(NΣxy-ΣxΣy)/(NΣx^2-(Σx)^2) 
		//b=y(平均)-a*x（平均）
		double a = (num*xys - xs*ys)/(num*xxs-xs*xs);
		double b = ys/num - a*xs/num;
		
		//重新计算y
		List<Object> result = new ArrayList<Object>();
		for(int i=0;i<num;i++){
		    double  y = new BigDecimal(a*(i+1)+b).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		    result.add(y);
		}
		return result;
	}

}
