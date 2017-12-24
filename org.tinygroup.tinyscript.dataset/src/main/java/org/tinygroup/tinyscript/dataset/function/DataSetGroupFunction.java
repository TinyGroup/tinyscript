package org.tinygroup.tinyscript.dataset.function;

import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.GroupDataSet;
import org.tinygroup.tinyscript.dataset.impl.MultiLevelGroupDataSet;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 数据集分组聚合的算法
 * 
 * @author yancheng11334
 *
 */
public class DataSetGroupFunction extends AbstractGroupFunction {

	public String getNames() {
		return "group";
	}

	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (parameters.length >= 2) {
				DynamicDataSet dataSet = (DynamicDataSet) getValue(parameters[0]);
				String[] fields = new String[parameters.length - 1];
				for (int i = 0; i < fields.length; i++) {
					String expression = convertExpression(getExpression(parameters[i + 1]));
					fields[i] = (String) getScriptEngine().execute(expression, context);
				}
				return groups(dataSet, fields);
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
	 * 多级分组
	 * 
	 * @param dataSet
	 * @param fields
	 * @return
	 * @throws Exception
	 */
	private GroupDataSet groups(DynamicDataSet dataSet, String[] fields) throws Exception {
		try {
			if (dataSet instanceof MultiLevelGroupDataSet) {
				// 某一级序表进行分组
				MultiLevelGroupDataSet multiLevelGroupDataSet = (MultiLevelGroupDataSet) dataSet;
				List<MultiLevelGroupDataSet> subDataSetList = multiLevelGroupDataSet.getUnGroups();
				for (MultiLevelGroupDataSet subDataSet : subDataSetList) {
					List<DynamicDataSet> list = group(subDataSet.getSource(), fields);
					subDataSet.setGroups(list);
				}
				updateAggregateResult(subDataSetList, multiLevelGroupDataSet.getAggregateResultList());
				return multiLevelGroupDataSet;
			} else {
				// 首次分组
				List<DynamicDataSet> list = group(dataSet, fields);
				MultiLevelGroupDataSet multiLevelGroupDataSet = new MultiLevelGroupDataSet(dataSet, list);
				return multiLevelGroupDataSet;
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

}
