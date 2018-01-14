package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.GroupDataSet;
import org.tinygroup.tinyscript.dataset.impl.MultiLevelGroupDataSet;
import org.tinygroup.tinyscript.function.date.DateEnum;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

public class DataSetGroupDateFunction extends AbstractGroupFunction {

	@Override
	public String getNames() {
		return "groupDate";
	}

	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (parameters.length >= 2) {
				DateEnum datePart = DateEnum.valueOf(((getValue(parameters[1]).toString()).toUpperCase()));
				DynamicDataSet dataSet = (DynamicDataSet) getValue(parameters[0]);
				String[] fields = new String[parameters.length - 2];
				for (int i = 0; i < fields.length; i++) {
					String expression = convertExpression(getExpression(parameters[i + 2]));
					fields[i] = (String) getScriptEngine().execute(expression, context);
				}
				return groups(dataSet, fields, datePart);
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
	private GroupDataSet groups(DynamicDataSet dataSet, String[] fields, DateEnum datePart) throws Exception {
		try {
			if (dataSet instanceof MultiLevelGroupDataSet) {
				// 某一级序表进行分组
				MultiLevelGroupDataSet multiLevelGroupDataSet = (MultiLevelGroupDataSet) dataSet;
				List<MultiLevelGroupDataSet> subDataSetList = multiLevelGroupDataSet.getUnGroups();
				for (MultiLevelGroupDataSet subDataSet : subDataSetList) {
					List<DynamicDataSet> list = group(subDataSet.getSource(), fields, datePart);
					subDataSet.setGroups(list);
				}
				updateAggregateResult(subDataSetList, multiLevelGroupDataSet.getAggregateResultList());
				return multiLevelGroupDataSet;
			} else {
				// 首次分组
				List<DynamicDataSet> list = group(dataSet, fields, datePart);
				MultiLevelGroupDataSet multiLevelGroupDataSet = new MultiLevelGroupDataSet(dataSet, list);
				return multiLevelGroupDataSet;
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	@Override
	protected Object createKey(int showRow, int[] showColumns, AbstractDataSet dataSet, Object... params)
			throws Exception {
		DateEnum datePart = (DateEnum) params[0];
		Calendar calendar = Calendar.getInstance();
		List<Object> keys = new ArrayList<Object>();
		for (int showColumn : showColumns) {
			calendar.setTime((Date) dataSet.getData(showRow, showColumn));
			keys.add(calendar.get(datePart.getCalendarId()));
		}
		return keys;

	}

}
