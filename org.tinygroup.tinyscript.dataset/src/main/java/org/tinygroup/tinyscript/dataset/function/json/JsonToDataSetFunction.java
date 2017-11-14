package org.tinygroup.tinyscript.dataset.function.json;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * json转换序表
 * @author yancheng11334
 *
 */
public class JsonToDataSetFunction extends AbstractScriptFunction{

	public String getNames() {
		return "jsonToDataSet";
	}
	
	public String getBindingTypes() {
		return String.class.getName()+","+JSONArray.class.getName();
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 1)) {
				if(parameters[0] instanceof String){
					JSONArray array = JSON.parseArray((String)parameters[0]);
					return toDataSet(array);
				}else if(parameters[0] instanceof JSONArray){
					JSONArray array = (JSONArray) parameters[0];
					return toDataSet(array);
				}
			} 
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));

		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}
	
	private DataSet toDataSet(JSONArray array) throws Exception{
		int rows = array.size();
		List<Field> fields = new ArrayList<Field>();
		Object[][] dataArray = new Object[rows][];
		for(int i=0;i<rows;i++){
			JSONObject object = array.getJSONObject(i);
			if(fields.isEmpty()){
			   //执行字段的初始化
			   for(String key:object.keySet()){
				   Field field = new Field(key,key,"Object");
				   fields.add(field);
			   }
			}
			if(fields.size()!=object.size()){
			   throw new ScriptException(ResourceBundleUtil.getResourceMessage("dataset", "dataset.convert.nomatch.columns", fields.size(),object.size()));
			}else{
			   dataArray[i] = new Object[fields.size()];
			   for(int j=0;j<fields.size();j++){
				   Field field = fields.get(j);
				   dataArray[i][j] = object.get(field.getName());
			   }
			}
			
		}
		return DataSetUtil.createDynamicDataSet(fields, dataArray,getScriptEngine().isIndexFromOne());
	}

}
