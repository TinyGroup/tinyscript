package org.tinygroup.tinyscript.dataset.attribute;

import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.interpret.AttributeProcessor;

/**
 * 支持下标方式访问数据集列对象
 * @author yancheng11334
 *
 */
public class DataSetColumnAttributeProcessor implements AttributeProcessor{

	public boolean isMatch(Object object, Object name) {
		if(object instanceof DataSet && name instanceof String){
			DataSet dataSet = (DataSet) object;
			try {
				return DataSetUtil.getFieldIndex(dataSet, (String)name) >=0;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	public Object getAttribute(Object object, Object name) throws Exception {
		DataSet dataSet = (DataSet) object;
		return DataSetUtil.createDataSetColumn(dataSet, (String)name);
	}

}
