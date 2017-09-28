package org.tinygroup.tinyscript.collection.function.list;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractDpKnapsackFunction;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

public class DpKnapsackFunction extends AbstractDpKnapsackFunction {

	@Override
	public String getBindingTypes() {
		return "java.util.List";
	}

	@Override
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		if (parameters == null || parameters.length <= 3) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", "dpKnapsack"));
		}

		List<Object> result = null;

		List<?> list = (List<?>) parameters[0];
		int bagSize = (Integer) parameters[1];
		int[] weight = (int[]) convertToArray(parameters[2], int.class);

		if (checkParameters(parameters, 4)) {// 无限背包
			try {
				int[] maxCount = getCount(null, weight, bagSize);

				Object value = parameters[3];
				if (value instanceof LambdaFunction) {
					value = ((LambdaFunction) parameters[3]).execute(context).getResult();
				}

				result = dpKnapsackResult(weight, (double[]) convertToArray(value, double.class), bagSize, maxCount);

			} catch (Exception e) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", "dpKnapsack"), e);
			}
		} else if (checkParameters(parameters, 5)) {// 混合背包和多重背包
			try {
				int[] maxCount = getCount(parameters[3], weight, bagSize);

				Object value = parameters[4];
				if (value instanceof LambdaFunction) {
					value = ((LambdaFunction) parameters[4]).execute(context).getResult();
				}

				result = dpKnapsackResult(weight, (double[]) convertToArray(value, double.class), bagSize, maxCount);
			} catch (Exception e) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", "dpKnapsack"), e);
			}
		} else if (checkParameters(parameters, 6)) {// 用户定义规则
			try {
				int[] maxCount = getCount(parameters[3], weight, bagSize);

				Object value = parameters[4];
				if (value instanceof LambdaFunction) {
					value = ((LambdaFunction) parameters[4]).execute(context).getResult();
				}

				LambdaFunction lambdaFunction = (LambdaFunction) parameters[5];

				result = dpKnapsackResult(weight, (double[]) convertToArray(value, double.class), bagSize, maxCount,
						list, context, lambdaFunction);
			} catch (Exception e) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", "dpKnapsack"), e);
			}
		}
		return getLastResult(result, list);

	}

	@Override
	protected List<Object> getLastResult(List<?> result, Object list) {
		List<Object> lastResult = new ArrayList<Object>();// 需要打印的结果
		Map<String, Object> resultValue = new HashMap<String, Object>();
		resultValue.put("result", result.get(0));
		int index = 0;
		for (int i = 1; i < result.size(); i++) {
			if ((Integer) result.get(i) == 0) {
				((List<?>) list).remove(i - 1 - index);
				index++;
			}
		}
		Iterator<?> it = result.subList(1, result.size()).iterator();
		while (it.hasNext()) {
			if ((Integer) it.next() == 0) {
				it.remove();
			}
		}
		lastResult.add(resultValue);
		lastResult.add(list);
		lastResult.add(result.subList(1, result.size()));
		return lastResult;

	}

	/**
	 * 将数组或列表转为背包问题需要的数组
	 * 
	 * @param array
	 *            需要转化的对象有可能是数组也有可能是list
	 * @param clazz
	 *            需要转化的数组类型
	 * @return
	 * @throws ScriptException
	 */
	@Override
	public Object convertToArray(Object array, Class<?> clazz) throws ScriptException {
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
			throw new ScriptException(
					ResourceBundleUtil.getDefaultMessage("function.parameter.error", "convertToArray"));
		}
		return obj;
	}

}
