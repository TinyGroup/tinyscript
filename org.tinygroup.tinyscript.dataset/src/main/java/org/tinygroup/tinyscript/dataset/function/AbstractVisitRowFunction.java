package org.tinygroup.tinyscript.dataset.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DataSetRow;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 抽象访问数据集行对象的函数
 * 
 * @author yancheng11334
 *
 */
public abstract class AbstractVisitRowFunction extends AbstractScriptFunction {

	public String getBindingTypes() {
		return DynamicDataSet.class.getName();
	}

	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (parameters.length == 1 && parameters[0] instanceof DataSet) {
				// 默认index为0
				return visit((DynamicDataSet) parameters[0], getDefaultIndex());
			} else if (parameters.length == 2 && parameters[0] instanceof DataSet) {
				Object value = DataSetUtil.getValue(parameters[1], context);
				if (value != null && value instanceof Integer) {
					int index = (Integer) value;
					// 用户输入的下标需要检查范围合法性
					checkIndex(index);
					return visit((DynamicDataSet) parameters[0], index);
				} else {
					throw new ScriptException(ResourceBundleUtil.getResourceMessage("dataset",
							"dataset.parameter.interror", getNames(), parameters[1]));
				}

			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}

		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	/**
	 * 访问行对象
	 * 
	 * @param index
	 * @return
	 * @throws ScriptException
	 */
	protected abstract DataSetRow visit(DynamicDataSet dataSet, int index) throws ScriptException;

	/**
	 * 获得默认值
	 * 
	 * @return
	 */
	protected abstract int getDefaultIndex();

	/**
	 * 根据业务场景判断下标是否合法
	 * 
	 * @param index
	 * @throws ScriptException
	 */
	protected void checkIndex(int index) throws Exception {

	}

}
