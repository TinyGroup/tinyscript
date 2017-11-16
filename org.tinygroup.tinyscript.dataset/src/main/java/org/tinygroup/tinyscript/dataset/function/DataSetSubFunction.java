package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.GroupDataSet;
import org.tinygroup.tinyscript.dataset.impl.MultiLevelGroupDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 拆分数据集
 * 
 * @author yancheng11334
 *
 */
public class DataSetSubFunction extends AbstractScriptFunction {

	public String getNames() {
		return "sub";
	}

	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 2) && parameters[1] instanceof Integer) {
				AbstractDataSet dataSet = (AbstractDataSet) parameters[0];
				int beginIndex = (Integer) parameters[1];
				int endIndex = dataSet.getRows() - 1;
				return sub(dataSet, dataSet.getActualIndex(beginIndex), endIndex);

			} else if (checkParameters(parameters, 3) && parameters[1] instanceof Integer
					&& parameters[2] instanceof Integer) {
				AbstractDataSet dataSet = (AbstractDataSet) parameters[0];
				int beginIndex = (Integer) parameters[1];
				int endIndex = (Integer) parameters[2];
				return sub(dataSet, dataSet.getActualIndex(beginIndex), dataSet.getActualIndex(endIndex));
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	// 本参数是实际下标
	private DataSet sub(AbstractDataSet dataSet, int beginIndex, int endIndex) throws Exception {
		if (beginIndex < 0 || endIndex > dataSet.getRows()  || beginIndex > endIndex) {
			throw new ScriptException(ResourceBundleUtil.getResourceMessage("dataset", "dataset.row2.outofindex",
					getNames(), beginIndex, endIndex));
		}
		
		if(dataSet instanceof GroupDataSet){
			//对分组序表进行处理
			MultiLevelGroupDataSet multiLevelGroupDataSet = (MultiLevelGroupDataSet) dataSet;
			if(multiLevelGroupDataSet.isGrouped()){
				//分组之后的父序表信息采用子表合并而成，因此只需要调整子表个数即可
				//List的subList方法不包含endIndex节点，需要+1
				List<DynamicDataSet> subs = new ArrayList<DynamicDataSet>(multiLevelGroupDataSet.getGroups().subList(beginIndex, endIndex+1));
				return new MultiLevelGroupDataSet(multiLevelGroupDataSet.getSource(),subs);
			}else{
				DynamicDataSet newDs = DataSetUtil.createDynamicDataSet(multiLevelGroupDataSet.getSource(),beginIndex,endIndex);
				return new MultiLevelGroupDataSet(newDs);
			}
		}else{
		   //对一般序表的进行处理
		   return DataSetUtil.createDynamicDataSet(dataSet,beginIndex,endIndex);
		}
		
	}
	

}
