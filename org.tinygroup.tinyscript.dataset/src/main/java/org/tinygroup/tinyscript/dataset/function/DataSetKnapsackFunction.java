package org.tinygroup.tinyscript.dataset.function;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.impl.DefaultDataSetColumn;
import org.tinygroup.tinyscript.dataset.impl.SimpleDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractDpKnapsackFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

public class DataSetKnapsackFunction extends AbstractDpKnapsackFunction {

	@Override
	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	@Override
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		if (parameters == null || parameters.length <= 3) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
		}

		SimpleDataSet dataSet = (SimpleDataSet) parameters[0];

		int bagSize = (Integer) parameters[1];
		int[] weight = (int[]) convertToArray(parameters[2], int.class);

		List<Object> result = null;

		context = setColData(dataSet, context);
		if (checkParameters(parameters, 4)) {
			try {
				int[] maxCount = getCount(null, weight, bagSize);

				result = dpKnapsackResult(weight, (double[]) convertToArray(parameters[3], double.class), bagSize,
						maxCount);
			} catch (Exception e) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
			}
		} else if (checkParameters(parameters, 5)) {
			try {
				int[] maxCount;
				if (parameters[4] instanceof LambdaFunction) {
					maxCount = getCount(null, weight, bagSize);
					result = dpKnapsackResult(weight, (double[]) convertToArray(parameters[3], double.class), bagSize,
							maxCount, dataSet, context, parameters[4]);
				} else {
					maxCount = getCount(parameters[3], weight, bagSize);
					result = dpKnapsackResult(weight, (double[]) convertToArray(parameters[4], double.class), bagSize,
							maxCount);
				}

			} catch (Exception e) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
			}
		} else if (checkParameters(parameters, 6)) {
			try {
				int[] maxCount = getCount(parameters[3], weight, bagSize);
				LambdaFunction lambdaFunction = (LambdaFunction) parameters[5];

				result = dpKnapsackResult(weight, (double[]) convertToArray(parameters[4], double.class), bagSize,
						maxCount, dataSet, context, lambdaFunction);
			} catch (Exception e) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
			}
		} else {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
		}

		return getLastResult(result, dataSet);

	}

	@Override
	protected Object convertToArray(Object array, Class<?> clazz) throws ScriptException {
		int length;
		DefaultDataSetColumn setColumn = (DefaultDataSetColumn) array;
		Object obj = null;
		boolean isIndexFromOne = setColumn.isIndexFromOne();
		try {
			length = setColumn.getRows();
			obj = Array.newInstance(clazz, length + 1);
			for (int i = 1; i < length + 1; i++) {
				if (clazz == int.class || clazz == Integer.class) {
					Array.set(obj, i, Integer.parseInt(setColumn.getData(isIndexFromOne ? i : i - 1) + ""));
				} else {
					Array.set(obj, i, Double.parseDouble(setColumn.getData(isIndexFromOne ? i : i - 1) + ""));
				}
			}
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getResourceMessage("dataset", "dataset.convertarray.error"));
		}

		return obj;
	}

	private ScriptContext setColData(AbstractDataSet dataSet, ScriptContext context) throws ScriptException {
		ScriptContext subContext = new DefaultScriptContext();
		subContext.setParent(context);
		try {
			for (int j = 0; j < dataSet.getColumns(); j++) {
				Field field = dataSet.getFields().get(j);
				subContext.put(field.getName(), DataSetUtil.createDataSetColumn(dataSet, dataSet.getShowIndex(j)));
			}
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getResourceMessage("dataset", "dataset.addcontext.error"));
		}
		subContext.put("isIndexFromOne", dataSet.isIndexFromOne());
		return subContext;
	}

	@Override
	protected List<Object> getLastResult(List<?> result, Object obj) throws ScriptException {
		List<Object> lastResult = new ArrayList<Object>();// 需要打印的结果
		Map<String, Object> resultValue = new HashMap<String, Object>();
		resultValue.put("result", result.get(0));

		SimpleDataSet dataSet;
		int index = 0;

		try {
			dataSet = (SimpleDataSet) ((SimpleDataSet) obj).clone();
			for (int i = 1; i < result.size(); i++) {
				if ((Integer) result.get(i) == 0) {
					dataSet.deleteRow(dataSet.isIndexFromOne() ? i - index : i - index - 1);
					index++;
				}
			}
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getResourceMessage("dataset", "dataset.row.delete.error"));
		}

		Iterator<?> it = result.subList(1, result.size()).iterator();
		while (it.hasNext()) {
			if ((Integer) it.next() == 0) {
				it.remove();
			}
		}
		lastResult.add(resultValue);
		lastResult.add(dataSet);
		lastResult.add(result.subList(1, result.size()));
		return lastResult;
	}

}
