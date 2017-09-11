package org.tinygroup.tinyscript.dataset.function;

import java.util.LinkedList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.impl.SimpleDataSet;
import org.tinygroup.tinyscript.interpret.LambdaFunction;

public class DataSetUnionFunction extends AbstractDataSetOperateFunction {

	@Override
	public String getNames() {
		return "unite";
	}

	@Override
	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	@Override
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException("unite函数的参数为空!");
			} else if (checkParameters(parameters, 3)) {

				if (parameters[0].getClass() != parameters[1].getClass()) {
					throw new ScriptException("参数类型类型不一致不支持集合运算");
				}

				DataSet dataSet1 = (DataSet) parameters[0];
				DataSet dataSet2 = (DataSet) parameters[1];

				if (!checkField(dataSet1, dataSet2)) {
					throw new ScriptException("序表参数不一致");
				}

				if (dataSet1 instanceof SimpleDataSet) {
					return operate((SimpleDataSet) dataSet1, (SimpleDataSet) dataSet2, parameters[2], context);
				} else {
					throw new ScriptException("不支持的序表类型");
				}
			} else {
				throw new ScriptException("unite函数的参数格式不正确!");
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException("unite函数执行错误!", e);
		}
	}

	@Override
	protected DataSet operate(SimpleDataSet dataSet1, SimpleDataSet dataSet2, Object pks, ScriptContext context)
			throws Exception {
		List<Object[]> resultArray = new LinkedList<Object[]>();
		for (int i = 0; i < dataSet2.getRows(); i++) {
			resultArray.add(dataSet2.getDataArray()[i]);
		}

		if (pks instanceof List || pks instanceof String) {
			List<Integer> pksIndex = showPkIndex(dataSet1, pks);
			for (int i = 1; i <= dataSet1.getRows(); i++) {
				if (checkRowData(dataSet1.getDataArray()[i - 1], dataSet2.getDataArray(), pksIndex) == -1) {
					resultArray.add(dataSet1.getDataArray()[i - 1]);
				}
			}
		} else if (pks instanceof LambdaFunction) {
			for (int i = 1; i <= dataSet1.getRows(); i++) {
				if (checkRowData(dataSet1, dataSet2, i, (LambdaFunction) pks, context) == -1) {
					resultArray.add(dataSet1.getDataArray()[i - 1]);
				}

			}
		}

		Object[][] newArray = new Object[resultArray.size()][];
		for (int i = 0; i < newArray.length; i++) {
			newArray[i] = resultArray.get(i);
		}

		return new SimpleDataSet(dataSet1.getFields(), newArray, dataSet1.isIndexFromOne());
	}

}
