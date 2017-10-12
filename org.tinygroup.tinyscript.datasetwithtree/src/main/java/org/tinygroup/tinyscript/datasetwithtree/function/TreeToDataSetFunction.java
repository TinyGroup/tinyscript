package org.tinygroup.tinyscript.datasetwithtree.function;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.impl.SimpleDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.tree.DataNode;
import org.tinygroup.tinyscript.tree.impl.DefaultDataNode;

public class TreeToDataSetFunction extends AbstractScriptFunction {

	@Override
	public String getNames() {
		return "toDataSet";
	}

	@Override
	public String getBindingTypes() {
		return DataNode.class.getName();
	}

	@Override
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 1)) {
				DefaultDataNode rootNode = (DefaultDataNode) getValue(parameters[0]);
				return toDataSet(rootNode);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private DataSet toDataSet(DefaultDataNode dataNode) throws Exception {

		List<Field> fields = createFields(dataNode);
		List<List<Object>> dataList = new ArrayList<List<Object>>();

		Queue<DefaultDataNode> queue = new LinkedList<DefaultDataNode>();
		queue.add(dataNode);
		while (!queue.isEmpty()) {		
			DefaultDataNode node = queue.poll();
			Map currentRow = (Map) node.getValue();
			List<Object> rowData = new ArrayList<Object>();
			
			for (String key : (Set<String>) currentRow.keySet()) {
				rowData.add(currentRow.get(key));
			}
			dataList.add(rowData);
			if (!node.isLeaf()) {
				for (DataNode childNode : node.getChildren()) {
					queue.add((DefaultDataNode) childNode);
				}
			}
		}

		SimpleDataSet dataSet = (SimpleDataSet) DataSetUtil.createDynamicDataSet(fields, convertToArray(dataList));
		return dataSet;
	}

	private Object[][] convertToArray(List<List<Object>> dataList) {
		Object[][] dataArray = new Object[dataList.size()][];
		for (int i = 0; i < dataList.size(); i++) {
			dataArray[i] = dataList.get(i).toArray();
		}
		return dataArray;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Field> createFields(DefaultDataNode dataNode) {
		List<Field> fields = new ArrayList<Field>();
		Map currentRow = (Map) dataNode.getValue();
		for (String key : (Set<String>) currentRow.keySet()) {
			Field field = new Field();
			field.setName(key);
			field.setTitle(key);
			field.setType(currentRow.get(key).getClass().getSimpleName());
			fields.add(field);
		}
		return fields;
	}

}
