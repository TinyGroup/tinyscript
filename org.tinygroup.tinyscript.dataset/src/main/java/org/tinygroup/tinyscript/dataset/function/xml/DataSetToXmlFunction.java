package org.tinygroup.tinyscript.dataset.function.xml;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 序表转换xml
 * @author yancheng11334
 *
 */
public class DataSetToXmlFunction extends AbstractScriptFunction{

	public String getNames() {
		return "toXml";
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
				return convertXmlStr(dataSet);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}

		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}
	
	private String convertXmlStr(AbstractDataSet dataSet) throws Exception{
		XmlNode root = new XmlNode("dataSet");
		String recordName = dataSet.getName()==null?"record":dataSet.getName();
		for(int i=0;i<dataSet.getRows();i++){
			XmlNode record = new XmlNode(recordName);
			int row = dataSet.getShowIndex(i);
			for(int j=0;j<dataSet.getFields().size();j++){
				Field field = dataSet.getFields().get(j);
				Object value = dataSet.getData(row, dataSet.getShowIndex(j));
				if(value!=null){
					record.setAttribute(field.getName(),value.toString());
				}else{
					record.setAttribute(field.getName(),"");
				}
			}
			root.addNode(record);
		}
		return root.toString();
	}

}
