package org.tinygroup.tinyscript.dataset.function.json;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 序表转换json
 * @author yancheng11334
 *
 */
public class DataSetToJsonFunction extends AbstractScriptFunction{

	public String getNames() {
		return "toJson";
	}
	
	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 1)) {
				AbstractDataSet dataSet = (AbstractDataSet) parameters[0];
				return convertJsonStr(dataSet);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}

		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}
	
	private String convertJsonStr(AbstractDataSet dataSet) throws Exception{
		JSONArray array = new JSONArray();
		for(int i=0;i<dataSet.getRows();i++){
			JSONObject object = new JSONObject();
			int row = dataSet.getShowIndex(i);
			for(int j=0;j<dataSet.getFields().size();j++){
				Field field = dataSet.getFields().get(j);
				object.put(field.getName(), dataSet.getData(row, dataSet.getShowIndex(j)));
			}
			array.add(object);
		}
		return array.toJSONString();
	}

}
