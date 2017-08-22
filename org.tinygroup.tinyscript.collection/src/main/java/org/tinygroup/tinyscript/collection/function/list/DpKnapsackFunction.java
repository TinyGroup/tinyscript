package org.tinygroup.tinyscript.collection.function.list;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.ScriptResult;

public class DpKnapsackFunction extends AbstractScriptFunction {

	private Integer[] items;// 存放每种物品的数量

	@Override
	public String getNames() {
		// TODO Auto-generated method stub
		return "DPknapsack";
	}

	@Override
	public String getBindingTypes() {
		return "java.util.List";
	}

	@Override
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		// TODO Auto-generated method stub
		if (parameters == null || parameters.length <= 3) {
			throw new ScriptException("sort函数的参数 错误!");
		}
		List<?> list = (List<?>) parameters[0];
		int size = (Integer) parameters[1];
		Map<Object, Object> lastresult = new HashMap<Object, Object>();
		List<Object> result = null;

		if (checkParameters(parameters, 4)) {// 无限背包
			try {
				LambdaFunction lambdaFunction = (LambdaFunction) parameters[3];
				Object v = lambdaFunction.execute(context, null).getResult();
				int[] count = new int[list.size() + 1];
				int[] w = (int[]) ConvertToArray(parameters[2], int.class);
				for (int i = 1; i < count.length; i++) {
					count[i] = size / w[i];
				}
				result = DpKnapsackResult(w, (double[]) ConvertToArray(v, double.class), size, count);

			} catch (Exception e) {
				throw new ScriptException("sort函数执行发生异常:", e);
			}
		} else if (checkParameters(parameters, 5)) {// 混合背包和多重背包
			try {
				LambdaFunction lambdaFunction = (LambdaFunction) parameters[4];
				Object v = lambdaFunction.execute(context, null).getResult();
				int[] count = (int[]) ConvertToArray(parameters[3], int.class);
				int[] w = (int[]) ConvertToArray(parameters[2], int.class);
				for (int i = 1; i < count.length; i++) {
					if (count[i] == -1)
						count[i] = size / w[i];
				}
				result = DpKnapsackResult(w, (double[]) ConvertToArray(v, double.class), size, count);
			} catch (Exception e) {
				throw new ScriptException("sort函数执行发生异常:", e);
			}
		} else if (checkParameters(parameters, 6)) {// 用户定义规则（目前是主件和附件）
			try {
				int[] rule = (int[]) ConvertToArray(parameters[4], int.class);// 主件和附件规则
				LambdaFunction lambdaFunction = (LambdaFunction) parameters[5];
				Object v = lambdaFunction.execute(context, null).getResult();
				result = DpKnapsackResult((int[]) ConvertToArray(parameters[2], int.class),
						(double[]) ConvertToArray(v, double.class), size,
						(int[]) ConvertToArray(parameters[3], int.class), rule);
			} catch (Exception e) {
				throw new ScriptException("sort函数执行发生异常:", e);
			}
		}
		lastresult.put("result", result.get(0));
		for (int i = 1; i < result.size(); i++) {
			lastresult.put(list.get(i - 1), result.get(i));
		}
		return lastresult;
	}

	private List<Object> DpKnapsackResult(int[] w, double[] v, int bagSize, int[] count, Object... rules) {
		double result[][] = new double[w.length][bagSize + 1];
		// 计算最优解
		DpKnapsackResult(result, w.length - 1, bagSize, w, count, v, rules);
		// 根据最优解查找最优方案
		items = new Integer[w.length];
		FindResult(w.length - 1, bagSize, result, w, v, count);

		List<Object> list = new ArrayList<Object>();
		Collections.addAll(list, items);
		list.set(0, result[w.length - 1][bagSize]);
		return list;
	}

	/**
	 * 解决背包问题
	 * @param f 最优值二维表
	 * @param N 物品件数
	 * @param V 最大容量
	 * @param w 重量
	 * @param count 物品限制件数
	 * @param value 物品价值
	 * @param rules 自定义规则(主件和附件)
	 */
	private void DpKnapsackResult(double f[][], int N, int V, int[] w, int[] count, double value[],
			Object... rules) {
		int nCount = 0;
		for (int i = 0; i <= N; i++) {
			f[i][0] = 0;
		}
		for (int v = 0; v <= V; v++) {
			f[0][v] = 0;
		}
		for (int i = 1; i <= N; i++) {
			for (int v = w[i]; v <= V; v++) {
				f[i][v] = 0;
				nCount = Math.min(count[i], v / w[i]);
				for (int k = 0; k <= nCount; k++) {
					if (rules != null && rules.length > 0) {
						int[] rule = (int[]) rules[0];
						if (rule[i] == 0) {//考虑到主件和附件的情况
							if (w[i] <= v) {
								f[i][v] = Math.max(f[i][v], f[i - 1][v - k * w[i]] + k * value[i]);
							}
						} else {
							if (w[i] + w[rule[i]] <= v) {
								f[i][v] = Math.max(f[i][v], f[i - 1][v - k * w[i]] + k * value[i]);
							}
						}
					} else {
						f[i][v] = Math.max(f[i][v], f[i - 1][v - k * w[i]] + k * value[i]);
					}
				}
			}
		}
	}

	
	/**
	 * 将数组或列表转为背包问题需要的数组
	 * @param array  需要转化的对象有可能是数组也有可能是list
	 * @param clazz  需要转化的数组类型
	 * @return
	 * @throws ScriptException
	 */
	private Object ConvertToArray(Object array, Class<?> clazz) throws ScriptException {
		Object obj = null;
		if (array instanceof List) {
			obj = Array.newInstance(clazz, ((List<?>) array).size() + 1);
			for (int i = 1; i < Array.getLength(obj); i++) {
				if (clazz == int.class || clazz == Integer.class) {
					Array.set(obj, i, Integer.parseInt(((List<?>) array).get(i - 1) + ""));
				} else {
					Array.set(obj, i, Double.parseDouble(((List<?>) array).get(i - 1) + ""));
				}
			}
		} else if (array.getClass().isArray()) {
			obj = Array.newInstance(clazz, Array.getLength(array) + 1);
			System.arraycopy(array, 0, obj, 1, Array.getLength(array));
		} else {
			throw new ScriptException("转换数组发生异常");
		}
		return obj;
	}

	/**
	 * @param i 
	 * @param j 供递归调用的下标
	 * @param f 存贮最佳结果的二维表
	 * @param w 重量
	 * @param v 价值
	 * @param count 限制数量
	 */
	private void FindResult(int i, int j, double[][] f, int[] w, double[] v, int count[]) {
		if (i > 0) {
			if (f[i][j] == f[i - 1][j]) {
				items[i] = 0;// 全局变量，标记未被选中
				FindResult(i - 1, j, f, w, v, count);
			} else if (j - w[i] >= 0) {
				int min = Math.min(j / w[i], count[i]);
				int temp = 0;
				for (int k = 1; k <= min; k++) {
					if (f[i - 1][j - k * w[i]] == f[i][j] - k * v[i]) {
						temp = k;
						break;
					}
				}
				items[i] = temp;// 标记已被选中
				FindResult(i - 1, j - temp * w[i], f, w, v, count);// 回到装包之前的位置
			}
		}
	}

}
