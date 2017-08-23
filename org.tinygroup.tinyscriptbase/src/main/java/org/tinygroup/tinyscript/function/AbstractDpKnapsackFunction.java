package org.tinygroup.tinyscript.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.interpret.LambdaFunction;

public abstract class AbstractDpKnapsackFunction extends DynamicNameScriptFunction {

	@Override
	public boolean exsitFunctionName(String name) {
		if (name.equals("dpKnapsack"))
			return true;
		return false;
	}

	public List<Object> dpKnapsackResult(int[] weight, double[] value, int bagSize, int[] maxCount,
			Object... parameters) throws Exception {
		double result[][] = new double[weight.length][bagSize + 1];
		// 计算最优解
		dpKnapsackResult(result, weight.length - 1, bagSize, weight, maxCount, value, parameters);
		// 根据最优解查找最优方案
		Integer[] items = new Integer[weight.length];
		FindResult(weight.length - 1, bagSize, result, weight, value, maxCount, items);

		List<Object> list = new ArrayList<Object>();
		Collections.addAll(list, items);
		list.set(0, result[weight.length - 1][bagSize]);
		return list;
	}

	/**
	 * @param result
	 *            存放最优值的二维表
	 * @param types
	 *            物品种类个数
	 * @param bagSize
	 *            背包容量
	 * @param weight
	 *            重量
	 * @param count
	 *            每个物品的的最大数量
	 * @param value
	 *            价值
	 * @param parameters
	 *            如果有用户自定义剪枝则在该变量中
	 * @throws Exception
	 */
	private void dpKnapsackResult(double result[][], int types, int bagSize, int[] weight, int[] maxCount,
			double value[], Object... parameters) throws Exception {
		ScriptContext context = null;
		LambdaFunction pruneFunction = null;
		if (parameters.length > 0) {
			context = (ScriptContext) parameters[1];
			pruneFunction = (LambdaFunction) parameters[2];
		}

		for (int i = 0; i <= types; i++) {
			result[i][0] = 0;
		}
		for (int v = 0; v <= bagSize; v++) {
			result[0][v] = 0;
		}
		for (int i = 1; i <= types; i++) {
			for (int v = weight[i]; v <= bagSize; v++) {
				result[i][v] = 0;
				int nCount = Math.min(maxCount[i], v / weight[i]);
				for (int k = 0; k <= nCount; k++) {
					if (parameters.length > 0) {
						if (executePrune(pruneFunction, context, parameters[0], i - 1, bagSize))// 用户自定义剪枝的情况
							result[i][v] = Math.max(result[i][v], result[i - 1][v - k * weight[i]] + k * value[i]);
					} else
						result[i][v] = Math.max(result[i][v], result[i - 1][v - k * weight[i]] + k * value[i]);
				}
			}
		}
	}

	/**
	 * @param i
	 * @param j
	 *            供递归调用的下标
	 * @param f
	 *            存贮最佳结果的二维表
	 * @param w
	 *            重量
	 * @param v
	 *            价值
	 * @param count
	 *            限制数量
	 * @param items
	 *            标记每个物品的数量状态
	 */
	protected void FindResult(int i, int j, double[][] f, int[] w, double[] v, int count[], Integer[] items) {
		if (i > 0) {
			if (f[i][j] == f[i - 1][j]) {
				items[i] = 0;// 全局变量，标记未被选中
				FindResult(i - 1, j, f, w, v, count, items);
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
				FindResult(i - 1, j - temp * w[i], f, w, v, count, items);// 回到装包之前的位置
			}
		}
	}

	/**
	 * @param obj
	 *            需要转换的参数可能是数字也有可能是数组(比如1转换为[0,1,1...]
	 * @param weight
	 *            物品重量
	 * @param size
	 *            背包容量
	 * @return
	 * @throws ScriptException
	 */
	protected int[] getCount(Object obj, int[] weight, int size) throws ScriptException {
		Object maxCount = obj;
		if (obj == null || (obj instanceof Integer) && (Integer) obj == -1) {// 为空时是完全背包
			maxCount = new int[weight.length];
			for (int i = 1; i < ((int[]) maxCount).length; i++) {
				((int[]) maxCount)[i] = size / weight[i];
			}
		} else if (maxCount instanceof Integer) {
			int temp = (Integer) maxCount;
			maxCount = new int[weight.length];
			Arrays.fill((int[]) maxCount, temp);
			((int[]) maxCount)[0] = 0;
		} else {
			maxCount = (int[]) convertToArray(obj, int.class);
			for (int i = 1; i < ((int[]) maxCount).length; i++) {
				if (((int[]) maxCount)[i] == -1)
					((int[]) maxCount)[i] = size / weight[i];
			}
		}
		return (int[]) maxCount;
	}

	abstract protected List<Object> getLastResult(List<?> result, Object obj) throws ScriptException;

	abstract protected Object convertToArray(Object array, Class<?> clazz) throws ScriptException;

	abstract protected boolean executePrune(LambdaFunction pruneFunction, ScriptContext context, Object... parameters)
			throws ScriptException;

}
