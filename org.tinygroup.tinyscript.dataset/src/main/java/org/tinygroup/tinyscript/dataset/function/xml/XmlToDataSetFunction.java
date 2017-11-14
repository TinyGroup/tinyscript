package org.tinygroup.tinyscript.dataset.function.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

/**
 * xml转换序表
 * @author yancheng11334
 *
 */
public class XmlToDataSetFunction extends AbstractScriptFunction{

	public String getNames() {
		return "xmlToDataSet";
	}
	
	public String getBindingTypes() {
		return String.class.getName()+","+XmlNode.class.getName();
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 1)) {
				if(parameters[0] instanceof String){
					XmlNode root = new XmlStringParser().parse((String)parameters[0]).getRoot();
					return toDataSet(root);
				}else if(parameters[0] instanceof XmlNode){
					XmlNode root = (XmlNode) parameters[0];
					return toDataSet(root);
				}
			} 
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));

		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}
	
	private DataSet toDataSet(XmlNode node) throws Exception{
		int rows = node.getSubNodes().size();
		List<Field> fields = new ArrayList<Field>();
		Object[][] dataArray = new Object[rows][];
		String dataSetName = null;
		for(int i=0;i<rows;i++){
			XmlNode subNode = node.getSubNodes().get(i);
			Map<String,String> map = subNode.getAttributes();
			if(fields.isEmpty()){
			   //执行字段的初始化
			   for(String key:map.keySet()){
				   Field field = new Field(key,key,"Object");
				   fields.add(field);
			   }
			   dataSetName = node.getNodeName();
			}
			
			if(fields.size()!=map.size()){
				throw new ScriptException(ResourceBundleUtil.getResourceMessage("dataset", "dataset.convert.nomatch.columns", fields.size(),map.size()));
			}else{
				dataArray[i] = new Object[fields.size()];
				for(int j=0;j<fields.size();j++){
					Field field = fields.get(j);
					dataArray[i][j] = map.get(field.getName());
				}
			}
		}
		DynamicDataSet dataSet = DataSetUtil.createDynamicDataSet(fields, dataArray,getScriptEngine().isIndexFromOne());
		dataSet.setName(dataSetName);
		return dataSet;
	}

}
